// package src;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.spi.CurrencyNameProvider;

class Lexer {

    /*--------------------Global variables--------------------------*/
    static Character[] letters = {'a','b','c','d','e','f','g','h','i',
                    'j','k','l','m','n','o','p','q','r',
                    's','t','u','v','w','x','y','z',
                    'A','B','C','D','E','F','G','H','I',
                    'J','K','L','M','N','O','P','Q','R',
                    'S','T','U','V','W','X','Y','Z'};
    static List<Character> letterList;

    static Character[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    static List<Character> digitsList;

    static String[] reserved = {"==", "<>", "<", ">", "<=", ">=", 
                        "+", "-", "*", "/", "=", "|", "&",
                        "!", "(", ")", "{", "}", "[", "]",
                        ";", ",", ".", ":", "::", "->", "if",
                        "then", "else", "integer", "float", "void",
                        "public", "private", "func", "var", "struct",
                        "while", "read", "write", "return", "self",
                        "inherits", "let", "impl"};
    static List<String> reservedList;

    static String[] commentsReserved = {"//", "/*", "*/" , "//"};
    static List<String> reservedCommentList;

    static BufferFuncs buff;
    static int lineNumber = 1;

    /*---------------------------------------------------------------*/
    


    static String getId(String currentString) throws Exception {

        String idString = "";
        Character startChar = currentString.charAt(0);
        Character currentChar;

        // Step 1: Check and build id from currentString from createToken input
        // Check for [letter] alphanum*
        if (letterList.contains(startChar)) {
            // Check for letter [alphanum*]
            for (Character c : currentString.toCharArray()) {
                if (letterList.contains(c) || digitsList.contains(c) || c.equals('_')) {
                    currentChar = c;
                    idString += currentChar;
                }
                // Return currently made string if values not from alphanum* 
                // (e.g. 1abc: [Invalid identifier:1abc] OR [integer:1][id:abc])
                else {
                    return idString;
                }
            }
        }
        // Return null if value not from letter (INVALID start character)
        else {
            return idString; // Return null if lett
        }

        // Step 2: Build rest of string from new characters
        Character peekedChar = buff.peekNextChar();
        // Check for alphanum* or if space/newline
        while (!peekedChar.equals(' ') && !peekedChar.equals('	') && !peekedChar.equals('\n') && !peekedChar.equals('\r') && (letterList.contains(peekedChar) || digitsList.contains(peekedChar) || peekedChar.equals('_'))) {
            currentChar = buff.getNextChar(); // Increment buffer
            idString += currentChar; 
            peekedChar = buff.peekNextChar();
        }


        return idString;
    }


    static String getInteger(String currentString) throws Exception{

        Character startChar = currentString.charAt(0);
        String intString = "";
        Character currentChar;

        // Step 1: Check for valid starting character [nonzero] digit* | [0]
        if (!digitsList.contains(startChar)) {
            return intString;
        }
        else if (startChar.equals('0')) {
            intString += startChar;
            return intString;
        }

        // Step 2: Build rest of string from new characters nonzero [digit]* | [0]
        intString += startChar;
        Character peekedChar = buff.peekNextChar();

        while (!peekedChar.equals(' ') && !peekedChar.equals('	') && !peekedChar.equals('\n') && !peekedChar.equals('\r') && (digitsList.contains(peekedChar))) {
            // Get digits 
            currentChar = buff.getNextChar(); // Increment buffer
            intString += currentChar; 
            peekedChar = buff.peekNextChar();

            if (peekedChar.equals('.')) {
                currentChar = buff.getNextChar(); // Increment buffer
                intString += currentChar; // Add the fraction (.) to the type
                peekedChar = buff.peekNextChar(); // TODO: Implement check to see if character after . is valid
                
                if (!digitsList.contains(peekedChar)) {
                    // TODO: Do something if there is no number after . (e.g. 101.)
                }

                String fractionString = getFraction();
                String floatString = intString;
                floatString += fractionString;
                peekedChar = buff.peekNextChar();

                if (fractionString.charAt(fractionString.length() -1 ) == 'X' || peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r')) {
                    return floatString;
                }
            }
        }

        return intString;
    }

    static String getFraction() throws Exception{

        Character currentChar = buff.getNextChar();
        String floatString = "";

        // Step 1: Check if valid digit or if ending in: .digit* nonzero | [.0]
        if (!digitsList.contains(currentChar)) {
            return floatString;
        }

        else if (currentChar.equals('0')) {
            floatString += currentChar;
            return floatString;
        }

        // Step 2: Check if digit or nonzero: .[digit* nonzero] | .0
        Character peekedChar = buff.peekNextChar();
        floatString += currentChar;

        // Check for nonzero: .digit* [nonzero] | .0
        // Check for long stream of 0's, and if there is a digit after them (eg. 10.0001) 
        while (!peekedChar.equals(' ') && !peekedChar.equals('	') && !peekedChar.equals('\n') && !peekedChar.equals('\r') && (digitsList.contains(peekedChar))) {
            // Get digits 
            currentChar = buff.getNextChar(); // Increment buffer
            peekedChar = buff.peekNextChar();
            
            if (currentChar.equals('0') && peekedChar.equals('0')) {
                String tempZero = "";

                while (!peekedChar.equals(' ') && !peekedChar.equals('	') && !peekedChar.equals('\n') && !peekedChar.equals('\r')) {
                    tempZero += currentChar;
                    currentChar = buff.getNextChar();
                    peekedChar = buff.peekNextChar();

                    if (!peekedChar.equals('0') && digitsList.contains(peekedChar) && digitsList.contains(peekedChar)) {
                        tempZero += currentChar;
                        floatString += tempZero;
                        currentChar = buff.getNextChar();
                        peekedChar = buff.peekNextChar();
                        break;
                    }
                    else if ((peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r')) && currentChar.equals('0')) {
                        tempZero += currentChar;
                        tempZero += "X";
                        floatString += tempZero;
                        return floatString;
                    }
                }
            }

            // Check for: .digit* [nonzero] | .0
            // If ends in a 0, set as invalid
            else if (currentChar.equals('0') && (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r'))) {
                floatString += "X"; // Invalid!
                return floatString;
            }

            // IMMEDIATELY set invalid! Get rest of number and print out values
            else if (currentChar.equals('0') && peekedChar.equals('e')) {
                while (!peekedChar.equals(' ') && !peekedChar.equals('	') && !peekedChar.equals('\n') && !peekedChar.equals('\r')) {
                    floatString += currentChar;
                    currentChar = buff.getNextChar();
                    peekedChar = buff.peekNextChar();
                }
                floatString += currentChar + "X"; // Invalid!
                return floatString;
            }

            floatString += currentChar; 
        }


        return floatString;
    }






    static TokenType createToken() throws Exception {

        Character currentChar = buff.getNextChar();
        TokenType token = new TokenType(); // Initialize to [NAN, NAN, -1]
        // TODO: To fix
        if (currentChar == null) {
            return token;
        }

        // Cast character to string add to token object
        String tokenValue = Character.toString(currentChar);

        // New line, increment line count
        // Dont use \r return line, since that will increment the count again!
        if (currentChar.equals('\n')) {
            lineNumber++;
            return token; // TODO: Fix this
        }
        else if (currentChar.equals('=')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('=')) {
                tokenValue += peekedChar;
                token.setAll("eq", tokenValue, lineNumber);
                buff.getNextChar(); // Advance the buffer by a char
            }
            else {
                token.setAll("assign", tokenValue, lineNumber);
            }
        }
        else if (currentChar.equals('<')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('>')) {
                tokenValue += peekedChar;
                token.setAll("noteq", tokenValue, lineNumber);
                buff.getNextChar(); // Advance the buffer by a char
            }
            else if (peekedChar.equals('=')) {
                tokenValue += peekedChar;
                token.setAll("leq", tokenValue, lineNumber);
                buff.getNextChar(); // Advance the buffer by a char
            }
            else {
                token.setAll("lt", tokenValue, lineNumber);
            }
        }
        else if (currentChar.equals('>')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('=')) {
                tokenValue += peekedChar;
                token.setAll("geq", tokenValue, lineNumber);
                buff.getNextChar(); // Advance the buffer by a char
            }
            else {
                token.setAll("gt", tokenValue, lineNumber);
            }
        }
        else if (currentChar.equals('+')) {
            token.setAll("plus", tokenValue, lineNumber);
        }
        else if (currentChar.equals('-')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('>')) {
                tokenValue += peekedChar;
                token.setAll("arrow", tokenValue, lineNumber);
                buff.getNextChar(); // Advance the buffer by a char
            }
            else {
                token.setAll("minus", tokenValue, lineNumber);
            }
        }
        else if (currentChar.equals('*')) {
            token.setAll("mult", tokenValue, lineNumber);
        }
        // Comments and div
        else if (currentChar.equals('/')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('*')) {
                int imbricationCount = 1; // Keep track of number of /* in the comment
                int startLineCount = lineNumber;

                while(imbricationCount > 0) {
                    currentChar = buff.getNextChar();

                    // Create comment string
                    if (currentChar.equals('\n')) {
                        lineNumber++; // Increment line count
                        tokenValue += "\\n";
                    }
                    else if (currentChar.equals('\r')) { // Skip carriage return
                        continue;
                    }
                    else {
                        tokenValue += currentChar;
                    }

                    peekedChar = buff.peekNextChar(); // Check for */

                    if (currentChar.equals('/') && peekedChar.equals('*')) {
                        imbricationCount++;
                    }
                    else if (currentChar.equals('*') && peekedChar.equals('/')) {
                        imbricationCount--;
                        if (imbricationCount == 0) {
                            currentChar = buff.getNextChar();
                            tokenValue += currentChar;
                            token.setAll("blockcmt", tokenValue, startLineCount);
                        }
                    }
                }
            }
            else if (peekedChar.equals('/')) {
                while (!peekedChar.equals('\n') && !peekedChar.equals('\r')) {
                    // Create comment string
                    currentChar = buff.getNextChar();
                    tokenValue += currentChar;
                    peekedChar = buff.peekNextChar();
                }
                token.setAll("inlinecmt", tokenValue, lineNumber);
            }
            else {
                token.setAll("div", "/", lineNumber);
            }
        }

        else if (currentChar.equals('|')) {
            token.setAll("or", tokenValue, lineNumber);
        }
        else if (currentChar.equals('&')) {
            token.setAll("and", tokenValue, lineNumber);
        }
        else if (currentChar.equals('!')) {
            token.setAll("not", tokenValue, lineNumber);
        }
        else if (currentChar.equals('(')) {
            token.setAll("openpar", tokenValue, lineNumber);
        }
        else if (currentChar.equals(')')) {
            token.setAll("closepar", tokenValue, lineNumber);
        }
        else if (currentChar.equals('{')) {
            token.setAll("opencubr", tokenValue, lineNumber);
        }
        else if (currentChar.equals('}')) {
            token.setAll("closecubr", tokenValue, lineNumber);
        }
        else if (currentChar.equals('[')) {
            token.setAll("opensqbr", tokenValue, lineNumber);
        }
        else if (currentChar.equals(']')) {
            token.setAll("closesqbr", tokenValue, lineNumber);
        }
        else if (currentChar.equals(';')) {
            token.setAll("semi", tokenValue, lineNumber);
        }
        else if (currentChar.equals(',')) {
            token.setAll("comma", tokenValue, lineNumber);
        }
        else if (currentChar.equals('.')) {
            token.setAll("dot", tokenValue, lineNumber);
        }
        else if (currentChar.equals(':')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals(':')) {
                tokenValue += peekedChar;
                token.setAll("coloncolon", tokenValue, lineNumber);
                buff.getNextChar(); // Advance the buffer by a char
            }
            else {
                token.setAll("colon", tokenValue, lineNumber);
            }
        }
        // if, impl, integer, inherits
        else if (currentChar.equals('i')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('f')) {
                // if
                tokenValue += peekedChar;
                token.setAll(tokenValue, tokenValue, lineNumber);
                buff.getNextChar(); // Advance the buffer by a char
            }
            else if (peekedChar.equals('m')) {
                while (currentChar.equals('i') || currentChar.equals('m') || currentChar.equals('p') || currentChar.equals('l') && tokenValue.length() < 4) {

                    // Create string impl
                    currentChar = buff.getNextChar();
                    tokenValue += currentChar;
                    if (tokenValue.equals("impl")) {
                        
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r')) {
                            token.setAll(tokenValue, tokenValue, lineNumber);
                        }
                        else {
                            // TODO: Make ID Function
                        }
                    }
                }
            }
            else if (peekedChar.equals('n')) {
                // Create string integer
                currentChar = buff.getNextChar();
                tokenValue += currentChar;
                peekedChar = buff.peekNextChar();

                if (peekedChar.equals('t')) {
                    while (currentChar.equals('i') || currentChar.equals('n') || currentChar.equals('t') || currentChar.equals('e') || currentChar.equals('g') || currentChar.equals('r') && tokenValue.length() < 7) {

                        // Create string integer
                        currentChar = buff.getNextChar();
                        tokenValue += currentChar;
                        if (tokenValue.equals("integer")) {
    
                            // Check if end of word or if word continues and is actually an ID
                            peekedChar = buff.peekNextChar();
                            if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r')) {
                                token.setAll(tokenValue, tokenValue, lineNumber);
                            }
                            else {
                                // TODO: Make ID Function
                            }
                        }
                    }
                }

                else if (peekedChar.equals('h')) {
                    while (currentChar.equals('i') || currentChar.equals('n') || currentChar.equals('h') || currentChar.equals('e') || currentChar.equals('r') || currentChar.equals('t') || currentChar.equals('s') && tokenValue.length() < 8) {
    
                        // Create string inherits
                        currentChar = buff.getNextChar();
                        tokenValue += currentChar;
                        if (tokenValue.equals("inherits")) {
    
                            // Check if end of word or if word continues and is actually an ID
                            peekedChar = buff.peekNextChar();
                            if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r')) {
                                token.setAll(tokenValue, tokenValue, lineNumber);
                            }
                            else {
                                // TODO: Make ID Function
                            }
                        }
                    }
                }
            }
            else {
                // TODO: Make ID Function
            }
        }
        // then
        else if (currentChar.equals('t')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('h')) {
                while (currentChar.equals('t') || currentChar.equals('h') || currentChar.equals('e') || currentChar.equals('n') && tokenValue.length() < 4) {
                    // Create string then
                    currentChar = buff.getNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("then")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r')) {
                            token.setAll(tokenValue, tokenValue, lineNumber);
                        }
                        else {
                            // TODO: Make ID Function
                        }
                    }
                }
            }
            else {
                // TODO: Make ID Function
            }
        }
        // else
        else if (currentChar.equals('e')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('l')) {
                while (currentChar.equals('e') || currentChar.equals('l') || currentChar.equals('s') && tokenValue.length() < 4) {
                    // Create string else
                    currentChar = buff.getNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("else")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r')) {
                            token.setAll(tokenValue, tokenValue, lineNumber);
                        }
                        else {
                            // TODO: Make ID Function
                        }
                    }
                }
            }
            else {
                // TODO: Make ID Function
            }
        }
        // float and func
        else if (currentChar.equals('f')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('l')) {
                while (currentChar.equals('f') || currentChar.equals('l') || currentChar.equals('o') || currentChar.equals('a') || currentChar.equals('t') && tokenValue.length() < 5) {
                    // Create string float
                    currentChar = buff.getNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("float")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r')) {
                            token.setAll(tokenValue, tokenValue, lineNumber);
                        }
                        else {
                            // TODO: Make ID Function
                        }
                    }
                }
            }
            else if (peekedChar.equals('u')) {
                while (currentChar.equals('f') || currentChar.equals('u') || currentChar.equals('n') || currentChar.equals('c') && tokenValue.length() < 4) {
                    // Create string func
                    currentChar = buff.getNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("func")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r')) {
                            token.setAll(tokenValue, tokenValue, lineNumber);
                        }
                        else {
                            // TODO: Make ID Function
                        }
                    }
                }
            }
            else {
                // TODO: Make ID Function
            }
        }

        // void, var
        else if (currentChar.equals('v')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('o')) {
                while (currentChar.equals('v') || currentChar.equals('o') || currentChar.equals('i') || currentChar.equals('d') && tokenValue.length() < 4) {
                    // Create string void
                    currentChar = buff.getNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("void")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r')) {
                            token.setAll(tokenValue, tokenValue, lineNumber);
                        }
                        else {
                            // TODO: Make ID Function
                        }
                    }
                }
            }
            else if (peekedChar.equals('a')) {
                while (currentChar.equals('v') || currentChar.equals('a') || currentChar.equals('r') && tokenValue.length() < 3) {
                    // Create string var
                    currentChar = buff.getNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("var")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r')) {
                            token.setAll(tokenValue, tokenValue, lineNumber);
                        }
                        else {
                            // TODO: Make ID Function
                        }
                    }
                }
            }
            else {
                // TODO: Make ID Function
            }
        }

        //public, private
        else if (currentChar.equals('p')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('u')) {
                while (currentChar.equals('p') || currentChar.equals('u') || currentChar.equals('b') || currentChar.equals('l') || currentChar.equals('i') || currentChar.equals('c') && tokenValue.length() < 6) {
                    // Create string public
                    currentChar = buff.getNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("public")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r')) {
                            token.setAll(tokenValue, tokenValue, lineNumber);
                        }
                        else {
                            // TODO: Make ID Function
                        }
                    }
                }
            }
            else if (peekedChar.equals('r')) {
                while (currentChar.equals('p') || currentChar.equals('r') || currentChar.equals('i') || currentChar.equals('v') || currentChar.equals('a') || currentChar.equals('t') || currentChar.equals('e') && tokenValue.length() < 7) {
                    // Create string private
                    currentChar = buff.getNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("private")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r')) {
                            token.setAll(tokenValue, tokenValue, lineNumber);
                        }
                        else {
                            // TODO: Make ID Function
                        }
                    }
                }
            }
            else {
                // TODO: Make ID Function
            }
        }
        // struct, self
        else if (currentChar.equals('s')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('t')) {
                while (currentChar.equals('s') || currentChar.equals('t') || currentChar.equals('r') || currentChar.equals('u') || currentChar.equals('c') && tokenValue.length() < 6) {
                    // Create string struct
                    currentChar = buff.getNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("struct")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r')) {
                            token.setAll(tokenValue, tokenValue, lineNumber);
                        }
                        else {
                            // TODO: Make ID Function
                        }
                    }
                }
            }
            else if (peekedChar.equals('e')) {
                while (currentChar.equals('s') || currentChar.equals('e') || currentChar.equals('l') || currentChar.equals('f') && tokenValue.length() < 4) {
                    // Create string self
                    currentChar = buff.getNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("self")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r')) {
                            token.setAll(tokenValue, tokenValue, lineNumber);
                        }
                        else {
                            // TODO: Make ID Function
                        }
                    }
                }
            }
            else {
                // TODO: Make ID Function
            }
        }
        // while, write
        else if (currentChar.equals('w')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('h')) {
                while (currentChar.equals('w') || currentChar.equals('h') || currentChar.equals('i') || currentChar.equals('l') || currentChar.equals('e') && tokenValue.length() < 5) {
                    // Create string while
                    currentChar = buff.getNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("while")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r')) {
                            token.setAll(tokenValue, tokenValue, lineNumber);
                        }
                        else {
                            // TODO: Make ID Function
                        }
                    }
                }
            }
            else if (peekedChar.equals('r')) {
                while (currentChar.equals('w') || currentChar.equals('r') || currentChar.equals('i') || currentChar.equals('t') || currentChar.equals('e') && tokenValue.length() < 5) {
                    // Create string write
                    currentChar = buff.getNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("write")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r')) {
                            token.setAll(tokenValue, tokenValue, lineNumber);
                        }
                        else {
                            // TODO: Make ID Function
                        }
                    }
                }
            }
            else {
                // TODO: Make ID Function
            }
        }
        // read, return
        else if (currentChar.equals('r')) {
            currentChar = buff.getNextChar();
            tokenValue += currentChar;
            Character peekedChar = buff.peekNextChar();

            if (currentChar.equals('e')) {

                if (peekedChar.equals('a')) {
                    while (currentChar.equals('r') || currentChar.equals('e') || currentChar.equals('a') || currentChar.equals('d') && tokenValue.length() < 4) {
                        // Create string read
                        currentChar = buff.getNextChar();
                        tokenValue += currentChar;
    
                        if (tokenValue.equals("read")) {
                            // Check if end of word or if word continues and is actually an ID
                            peekedChar = buff.peekNextChar();
                            if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r')) {
                                token.setAll(tokenValue, tokenValue, lineNumber);
                            }
                            else {
                                // TODO: Make ID Function
                            }
                        }
                    }
                }
                else if (peekedChar.equals('t')) {
                    while (currentChar.equals('r') || currentChar.equals('e') || currentChar.equals('t') || currentChar.equals('u') || currentChar.equals('n') && tokenValue.length() < 6) {
                        // Create string return
                        currentChar = buff.getNextChar();
                        tokenValue += currentChar;
    
                        if (tokenValue.equals("return")) {
                            // Check if end of word or if word continues and is actually an ID
                            peekedChar = buff.peekNextChar();
                            if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r')) {
                                token.setAll(tokenValue, tokenValue, lineNumber);
                            }
                            else {
                                // TODO: Make ID Function
                            }
                        }
                    }
                }
                else {
                    // TODO: Make ID Function
                }
            }
            else {
                // TODO: Make ID Function
            }
        }
        // let
        else if (currentChar.equals('l')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('e')) {
                while (currentChar.equals('l') || currentChar.equals('e') || currentChar.equals('t') && tokenValue.length() < 3) {
                    // Create string let
                    currentChar = buff.getNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("let")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r')) {
                            token.setAll(tokenValue, tokenValue, lineNumber);
                        }
                        else {
                            // TODO: Make ID Function
                        }
                    }
                }
            }
            else {
                // TODO: Make ID Function
            }
        }




        // TODO: Set properly for ID
        else if (letterList.contains(currentChar)){
            String id = getId(tokenValue);
            System.out.println("ID: " + id);
        }
        // TODO: Implement integer and float
        else if (digitsList.contains(currentChar) && !currentChar.equals('0')) {
            String numString = getInteger(tokenValue);

            if (numString.contains("X")) {
                numString = new StringBuilder(numString).deleteCharAt(numString.length()-1).toString();
                System.out.println("Invalid: " + numString);
            }
            else if (numString.contains(".")) {
                System.out.println("Float: " + numString);
            }
            else {
                System.out.println("Integer: " + numString);
            }
        }

        // Move to next word after space
        else {
            Character peekedChar = buff.peekNextChar();
            String invalidString = "";
            invalidString += currentChar;
            // Increment only a single miswritten lexical element until the next space or newline
            while (!peekedChar.equals(' ') && !peekedChar.equals('	') && !peekedChar.equals('\n') && !peekedChar.equals('\r')) {
                currentChar = buff.getNextChar(); // Move buffer
                invalidString += currentChar;
                peekedChar = buff.peekNextChar();
            }
            System.out.println("Invalid: " + invalidString);
        }
        

        return token;
    }

    // Constructor
    public Lexer() {
        letterList = Arrays.asList(letters);
        digitsList = Arrays.asList(digits);
        reservedList = Arrays.asList(reserved);
        reservedCommentList = Arrays.asList(commentsReserved);
    }

    public static void main(String args[]) throws Exception {

        // Read filepath
        String filePath = args[0];
        // BufferFuncs buffer = new BufferFuncs(new FileReader(filePath));
        buff = new BufferFuncs(new FileReader(filePath));
        letterList = Arrays.asList(letters);
        digitsList = Arrays.asList(digits);
        reservedList = Arrays.asList(reserved);
        reservedCommentList = Arrays.asList(commentsReserved);


        // // Read input line by line
        // try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        //     String str = new String();

        //     while ((str = br.readLine()) != null) {
        //         System.out.println(str);
        //     }

        // } 
        // catch(IOException e) {
        //     System.out.println("Error reading file.");
        // }


        for (int i = 0; i < 18; i++) {
            buff.setReadLine();
            lineNumber++;
        }

        // Create character
        for (int i = 0; i < 30; i++) {
            TokenType token = createToken();
            token.printAll();
        }



        // while(true) {
        //     Character c = getNextChar(inputBuffer);
        // }

    }

}
