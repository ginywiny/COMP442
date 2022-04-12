// package src;
import java.io.*;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

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

    static String[] reservedWords = {"if",
    "then", "else", "integer", "float", "void",
    "public", "private", "func", "var", "struct",
    "while", "read", "write", "return", "self",
    "inherits", "let", "impl"};
    static List<String> reservedWordsList;


    static String[] operators = {"==", "<>", "<", ">", "<=", ">=", 
    "+", "-", "*", "/", "=", "|", "&",
    "!", "(", ")", "{", "}", "[", "]",
    ";", ",", ".", ":", "::", "->"};
    static List<String> operatorsList;


    static BufferFuncs buff;
    static int lineNumber = 1;
    static int prevLine = 1;
    static Boolean eofFlag;

    static Character INVALIDSIGN = '$';

    static FileReader inSrcFile;
    static FileWriter tokenFile;
    static FileWriter errorFile;
    static BufferedWriter bwToken;
    static BufferedWriter bwError;

    static String filePath;
    static String dirFilePathWithoutName;

    /*---------------------------------------------------------------*/
    

    static String getInvalidString(String currentString) throws Exception {

        String invalidString = currentString;
        Character currentChar = currentString.charAt(currentString.length() - 1); // Set current to final element from String
        Character peekedChar = buff.peekNextChar();

        // TODO: Include invalid chars in the loop as well???: ex. @ # $ ' \ ~ 
        // TODO: Do this by using !reservedList??
        while (!peekedChar.equals(' ') && !peekedChar.equals('	') && !peekedChar.equals('\n') && !peekedChar.equals('\r') && (letterList.contains(peekedChar) || digitsList.contains(peekedChar) || peekedChar.equals('_') || peekedChar.equals('.'))) {
            currentChar = buff.getNextChar();
            invalidString += currentChar;
            peekedChar = buff.peekNextChar();
        }
        invalidString += INVALIDSIGN; // Use as delimiter to set invalid variable!
        return invalidString;
    }


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
                    idString = getInvalidString(currentString);
                    return idString;
                }
            }
        }
        // Return full invalid string if value not from letter (INVALID start character)
        else {
            // TODO: Return invalid string
            idString = getInvalidString(currentString);
            return idString;
        }

        // Step 2: Build rest of string from new characters
        Character peekedChar = buff.peekNextChar();
        String peekedString = peekedChar.toString();

        // // A reservedword CANNOT be used as a keyword! Meaning that, if you have then*, this would be an INVALID ID, and NOT id and Mult
        // if (reservedWordsList.contains(idString) && !digitsList.contains(peekedChar) && !peekedChar.equals('_') && !letterList.contains(peekedChar) && !peekedChar.equals(' ') && !peekedChar.equals('	') && !peekedChar.equals('\n') && !peekedChar.equals('\r')) {
        //     while (!peekedChar.equals(' ') && !peekedChar.equals('	') && !peekedChar.equals('\n') && !peekedChar.equals('\r')) {
        //         currentChar = buff.getNextChar(); // Increment buffer
        //         idString += currentChar; 
        //         peekedChar = buff.peekNextChar();
        //     }
        //     idString += INVALIDSIGN; // Use as delimiter to set invalid variable!
        //     return idString;
        // }

        // If word contains illegal character
        // else if (!operatorsList.contains(peekedString) && !digitsList.contains(peekedChar) && !peekedChar.equals('_') && !letterList.contains(peekedChar) && !peekedChar.equals(' ') && !peekedChar.equals('	') && !peekedChar.equals('\n') && !peekedChar.equals('\r')) {
        if (!operatorsList.contains(peekedString) && !digitsList.contains(peekedChar) && !peekedChar.equals('_') && !letterList.contains(peekedChar) && !peekedChar.equals(' ') && !peekedChar.equals('	') && !peekedChar.equals('\n') && !peekedChar.equals('\r')) {
            while (!peekedChar.equals(' ') && !peekedChar.equals('	') && !peekedChar.equals('\n') && !peekedChar.equals('\r')) {
                currentChar = buff.getNextChar(); // Increment buffer
                idString += currentChar; 
                peekedChar = buff.peekNextChar();
            }
            idString += INVALIDSIGN; // Use as delimiter to set invalid variable!
            return idString;
        }
        else {
            // Check for alphanum* or if space/newline
            while (!peekedChar.equals(' ') && !peekedChar.equals('	') && !peekedChar.equals('\n') && !peekedChar.equals('\r') && (letterList.contains(peekedChar) || digitsList.contains(peekedChar) || peekedChar.equals('_'))) {
                currentChar = buff.getNextChar(); // Increment buffer
                idString += currentChar; 
                peekedChar = buff.peekNextChar();
            }
        }

        return idString;
    }


    static String getNumber(String currentString) throws Exception{

        Character startChar = currentString.charAt(0);
        String intString = "";
        Character currentChar;
        Character peekedChar = buff.peekNextChar();

        // Step 1: Check for valid starting character [nonzero] digit* | [0]
        if (!digitsList.contains(startChar) || (startChar.equals('0') && (!peekedChar.equals(' ') && !peekedChar.equals('	') && !peekedChar.equals('\n') && !peekedChar.equals('\r') && !operatorsList.contains(peekedChar.toString())))) {
            intString = getInvalidString(startChar.toString());
            return intString;
        }
        // If single digit [0-9]
        else if (!peekedChar.equals('.') && (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString()))) {
            intString += startChar;
            return intString;
        }

        // Step 2: Build rest of string from new characters nonzero [digit]* | [0]
        intString += startChar;

        while (!peekedChar.equals(' ') && !peekedChar.equals('	') && !peekedChar.equals('\n') && !peekedChar.equals('\r') && (digitsList.contains(peekedChar) || peekedChar.equals('.'))) {
            if (peekedChar.equals('.')) {
                currentChar = buff.getNextChar(); // Increment buffer
                intString += currentChar; // Add the fraction (.) to the type
                peekedChar = buff.peekNextChar(); // TODO: Implement check to see if character after . is valid
                
                if (!digitsList.contains(peekedChar)) {
                    // TODO: Confirm that this works
                    String invalidString = getInvalidString(intString);
                    return invalidString;
                }

                String fractionString = getFraction();
                String floatString = intString;
                floatString += fractionString;
                peekedChar = buff.peekNextChar();

                if (fractionString.charAt(fractionString.length() -1 ) == INVALIDSIGN || peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString())) {
                    return floatString;
                }
                else if (peekedChar.equals('e')) {
                    String exponentString = getExponent();
                    floatString += exponentString;
                    return floatString;
                }
                // else if (operatorsList.contains(peekedChar.toString()) && .)
            }

            // Get digits 
            currentChar = buff.getNextChar(); // Increment buffer
            intString += currentChar; 
            peekedChar = buff.peekNextChar();
        }
        if (!peekedChar.equals(' ') && !peekedChar.equals('	') && !peekedChar.equals('\n') && !peekedChar.equals('\r') && (!digitsList.contains(peekedChar) && !operatorsList.contains(peekedChar.toString()))) {
            intString = getInvalidString(startChar.toString());
            return intString;
        }

        return intString;
    }


    static String getExponent() throws Exception {

        // Step 1: Get e and move buffer
        Character currentChar = buff.getNextChar();
        Character peekedChar = buff.peekNextChar();
        String exponentString = "";
        exponentString += currentChar;

        // if (currentChar.equals('e') && (peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || digitsList.contains(peekedChar))) {
        if (currentChar.equals('e') && (peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString()))) {
            exponentString += INVALIDSIGN;
            return exponentString;
        }

        currentChar = buff.getNextChar();
        peekedChar = buff.peekNextChar();
        exponentString += currentChar;

        if (currentChar.equals('0') && digitsList.contains(peekedChar)) {
            while (!peekedChar.equals(' ') && !peekedChar.equals('	') && !peekedChar.equals('\n') && !peekedChar.equals('\r') && !operatorsList.contains(peekedChar.toString())) {
                currentChar = buff.getNextChar();
                peekedChar = buff.peekNextChar();
                exponentString += currentChar;
            }
            exponentString += INVALIDSIGN;
            return exponentString; 
        }

        // Step 2: Get +/- integer
        if (peekedChar.equals('-') || (digitsList.contains(peekedChar))) {
            while (!peekedChar.equals(' ') && !peekedChar.equals('	') && !peekedChar.equals('\n') && !peekedChar.equals('\r') && digitsList.contains(peekedChar)) {
                currentChar = buff.getNextChar();
                peekedChar = buff.peekNextChar();
                exponentString += currentChar;
            }
            if (!digitsList.contains(currentChar) && (peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || digitsList.contains(peekedChar))) {
                exponentString += INVALIDSIGN;
            }
        }
        // Set invalid if problem found
        if (peekedChar.equals('0') || (!operatorsList.contains(peekedChar.toString()) && !digitsList.contains(peekedChar) && !peekedChar.equals(' ') && !peekedChar.equals('	') && !peekedChar.equals('\n') && !peekedChar.equals('\r'))) {
            while (!peekedChar.equals(' ') && !peekedChar.equals('	') && !peekedChar.equals('\n') && !peekedChar.equals('\r')) {
                currentChar = buff.getNextChar();
                peekedChar = buff.peekNextChar();
                exponentString += currentChar;
            }
            exponentString += INVALIDSIGN;
        }
        return exponentString;
    }


    static String getFraction() throws Exception{

        Character currentChar = buff.getNextChar();
        String floatString = "";
        Character peekedChar = buff.peekNextChar();
        floatString += currentChar;

        // Step 1: Check if valid digit or if ending in: .digit* nonzero | [.0]
        if (!digitsList.contains(currentChar)) {
            floatString += INVALIDSIGN;
            return floatString;
        }

        // // End in .0 or .[1-9] 
        // else if ((operatorsList.contains(peekedChar.toString()) || peekedChar.equals('e')) && digitsList.contains(currentChar)) {
        //     floatString += currentChar;
        //     return floatString;
        // }

        // End in .0 or .[1-9] 
        else if (currentChar.equals('0')) {
            // floatString += currentChar;
            return floatString;
        }

        // Step 2: Check if digit or nonzero: .[digit* nonzero] | .0
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
                        tempZero += INVALIDSIGN;
                        floatString += tempZero;
                        return floatString;
                    }
                }
            }

            // Check for: .digit* [nonzero] | .0
            // If ends in a 0, set as invalid
            else if (currentChar.equals('0') && (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r'))) {
                floatString += currentChar;
                floatString += INVALIDSIGN; // Invalid!
                return floatString;
            }

            // IMMEDIATELY set invalid! Get rest of number and print out values
            else if (currentChar.equals('0') && peekedChar.equals('e')) {
                while (!peekedChar.equals(' ') && !peekedChar.equals('	') && !peekedChar.equals('\n') && !peekedChar.equals('\r')) {
                    floatString += currentChar;
                    currentChar = buff.getNextChar();
                    peekedChar = buff.peekNextChar();
                }
                floatString += currentChar + INVALIDSIGN; // Invalid!
                return floatString;
            }

            floatString += currentChar; 
        }
        
        return floatString;
    }






    // static TokenType createToken() throws Exception {
    TokenType createToken() throws Exception {

        lineNumber = buff.getLineNumber() + 1; // Indexed by 0, add 1
        Character currentChar = buff.getNextChar();
        TokenType token = new TokenType(); // Initialize to [NAN, NAN, -1]
        if (currentChar == null) {
            return token;
        }

        // Cast character to string add to token object
        String tokenValue = Character.toString(currentChar);

        // New line, increment line count
        // Dont use \r return line, since that will increment the count again!
        if (currentChar.equals('\n')) {
            return token;
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
                        tokenValue += "\\n";
                    }
                    else if (currentChar.equals('\r')) { // Skip carriage return
                        continue;
                    }
                    else {
                        tokenValue += currentChar;
                        // System.out.println("CURRENT: " + tokenValue);
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
                            return token;
                        }
                    }
                    // If comments not closed
                    if (buff.isEndOfFile() && imbricationCount > 0) {
                        return token;
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
        // IF, impl, integer, inherits
        else if (currentChar.equals('i')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('f')) {
                // if
                while (letterList.contains(currentChar) && tokenValue.length() < 2 && letterList.contains(peekedChar)) {
                    currentChar = buff.getNextChar();
                    peekedChar = buff.peekNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("if")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString())) {
                            // TODO: Test This
                            token.setAll(tokenValue, tokenValue, lineNumber);
                            return token;
                        }
                        //TODO: Test This
                        else {
                            String id = getId(tokenValue);
                            token.setAllWithoutId(id, lineNumber);
                            return token;
                        }
                    }
                }
                // TODO: Create safety check to see if ID is valid!!!!!!!
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
            else if (peekedChar.equals('m')) {
                while (letterList.contains(currentChar) && tokenValue.length() < 4 && letterList.contains(peekedChar)) {
                    // Create string impl
                    currentChar = buff.getNextChar();
                    peekedChar = buff.peekNextChar();
                    tokenValue += currentChar;
                    
                    if (tokenValue.equals("impl")) {

                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString())) {
                            // TODO: Test This
                            token.setAll(tokenValue, tokenValue, lineNumber);
                            return token;
                        }
                        //TODO: Test This
                        else {
                            String id = getId(tokenValue);
                            token.setAllWithoutId(id, lineNumber);
                            return token;
                        }
                    }
                }
                // TODO: Create safety check to see if ID is valid!!!!!!!
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
            else if (peekedChar.equals('n')) {
                // Create string integer
                currentChar = buff.getNextChar();
                peekedChar = buff.peekNextChar();
                tokenValue += currentChar;

                if (peekedChar.equals('t')) {
                    while (letterList.contains(currentChar) && tokenValue.length() < 7 && letterList.contains(peekedChar)) {

                        // Create string integer
                        currentChar = buff.getNextChar();
                        tokenValue += currentChar;
                        if (tokenValue.equals("integer")) {
    
                            // Check if end of word or if word continues and is actually an ID
                            peekedChar = buff.peekNextChar();
                            if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString())) {
                                // TODO: Test this
                                token.setAll(tokenValue, tokenValue, lineNumber);
                                return token;
                            }
                            else {
                                //TODO: Test This
                                String id = getId(tokenValue);
                                token.setAllWithoutId(id, lineNumber);
                                return token;
                            }
                        }
                    }
                    //TODO: Test This
                    String id = getId(tokenValue);
                    token.setAllWithoutId(id, lineNumber);
                    return token;
                }

                else if (peekedChar.equals('h')) {
                    while (letterList.contains(currentChar) && tokenValue.length() < 8 && letterList.contains(peekedChar)) {
                        // Create string inherits
                        currentChar = buff.getNextChar();
                        peekedChar = buff.peekNextChar();
                        tokenValue += currentChar;

                        if (tokenValue.equals("inherits")) {
    
                            // Check if end of word or if word continues and is actually an ID
                            peekedChar = buff.peekNextChar();
                            if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString())) {
                                // TODO: Test This
                                token.setAll(tokenValue, tokenValue, lineNumber);
                                return token;
                            }
                            else {
                                //TODO: Test This
                                String id = getId(tokenValue);
                                token.setAllWithoutId(id, lineNumber);
                                return token;
                            }
                        }
                        
                    }
                    //TODO: Test This
                    String id = getId(tokenValue);
                    token.setAllWithoutId(id, lineNumber);
                    return token;
                }
            }
            else {
                //TODO: Test This 
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
        } 
        // then
        else if (currentChar.equals('t')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('h')) {
                while (letterList.contains(currentChar) && tokenValue.length() < 4 && letterList.contains(peekedChar)) {
                    // Create string then
                    currentChar = buff.getNextChar();
                    peekedChar = buff.peekNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("then")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString())) {
                            // TODO: Test This
                            token.setAll(tokenValue, tokenValue, lineNumber);
                            return token;
                        }
                        else {
                            //TODO: Test This
                            String id = getId(tokenValue);
                            token.setAllWithoutId(id, lineNumber);
                            return token;
                        }
                    }
                }
                //TODO: Test This
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
            else {
                //TODO: Test This
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
        }
        // else
        else if (currentChar.equals('e')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('l')) {
                while (letterList.contains(currentChar) && tokenValue.length() < 4 && letterList.contains(peekedChar)) {
                    // Create string else
                    currentChar = buff.getNextChar();
                    peekedChar = buff.peekNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("else")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString())) {
                            // TODO: Test this
                            token.setAll(tokenValue, tokenValue, lineNumber);
                            return token;
                        }
                        else {
                            //TODO: Test This
                            String id = getId(tokenValue);
                            token.setAllWithoutId(id, lineNumber);
                            return token;
                        }
                    }
                }
            }
            else {
                //TODO: Test This
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
        }
        // float and func
        else if (currentChar.equals('f')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('l')) {
                while (letterList.contains(currentChar) && tokenValue.length() < 5 && letterList.contains(peekedChar)) {
                    // Create string float
                    currentChar = buff.getNextChar();
                    peekedChar = buff.peekNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("float")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString())) {
                            //TODO: Test this
                            token.setAll(tokenValue, tokenValue, lineNumber);
                            return token;
                        }
                        else {
                            //TODO: Test This
                            String id = getId(tokenValue);
                            token.setAllWithoutId(id, lineNumber);
                            return token;
                        }
                    }
                }
                //TODO: Test This
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
            else if (peekedChar.equals('u')) {
                while (letterList.contains(currentChar) && tokenValue.length() < 4 && letterList.contains(peekedChar)) {
                    // Create string func
                    currentChar = buff.getNextChar();
                    peekedChar = buff.peekNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("func")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString())) {
                            // TODO: Test this
                            token.setAll(tokenValue, tokenValue, lineNumber);
                            return token;
                        }
                        else {
                            //TODO: Test This
                            String id = getId(tokenValue);
                            token.setAllWithoutId(id, lineNumber);
                            return token;
                        }
                    }
                }
                //TODO: Test This
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
            else {
                //TODO: Test This
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
        }

        // void, var
        else if (currentChar.equals('v')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('o')) {
                while (letterList.contains(currentChar) && tokenValue.length() < 4 && letterList.contains(peekedChar)) {
                    // Create string void
                    currentChar = buff.getNextChar();
                    peekedChar = buff.peekNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("void")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString())) {
                            //TODO: Test this
                            token.setAll(tokenValue, tokenValue, lineNumber);
                            return token;
                        }
                        else {
                            //TODO: Test This
                            String id = getId(tokenValue);
                            token.setAllWithoutId(id, lineNumber);
                            return token;
                        }
                    }
                }
                //TODO: Test This
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
            else if (peekedChar.equals('a')) {
                while (letterList.contains(currentChar) && tokenValue.length() < 3 && letterList.contains(peekedChar)) {
                    // Create string var
                    currentChar = buff.getNextChar();
                    peekedChar = buff.peekNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("var")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString())) {
                            // TODO: Test this
                            token.setAll(tokenValue, tokenValue, lineNumber);
                            return token;
                        }
                        else {
                            //TODO: Test This
                            String id = getId(tokenValue);
                            token.setAllWithoutId(id, lineNumber);
                            return token;
                        }
                    }
                }
                //TODO: Test This
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
            else {
                //TODO: Test This
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
        }

        //public, private
        else if (currentChar.equals('p')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('u')) {
                while (letterList.contains(currentChar) && tokenValue.length() < 6 && letterList.contains(peekedChar)) {
                    // Create string public
                    currentChar = buff.getNextChar();
                    peekedChar = buff.peekNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("public")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString())) {
                            // TODO: Test this
                            token.setAll(tokenValue, tokenValue, lineNumber);
                            return token;
                        }
                        else {
                            //TODO: Test This
                            String id = getId(tokenValue);
                            token.setAllWithoutId(id, lineNumber);
                            return token;
                        }
                    }
                }
                //TODO: Test This
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
            else if (peekedChar.equals('r')) {
                while (letterList.contains(currentChar) && tokenValue.length() < 7 && letterList.contains(peekedChar)) {
                    // Create string private
                    currentChar = buff.getNextChar();
                    peekedChar = buff.peekNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("private")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString())) {
                            // TODO: Test this
                            token.setAll(tokenValue, tokenValue, lineNumber);
                            return token;
                        }
                        else {
                            //TODO: Test This
                            String id = getId(tokenValue);
                            token.setAllWithoutId(id, lineNumber);
                            return token;
                        }
                    }
                }
                //TODO: Test This
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
            else {
                //TODO: Test This
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
        }
        // struct, self
        else if (currentChar.equals('s')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('t')) {
                while (letterList.contains(currentChar) && tokenValue.length() < 6 && letterList.contains(peekedChar)) {
                    // Create string struct
                    currentChar = buff.getNextChar();
                    peekedChar = buff.peekNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("struct")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString())) {
                            // TODO: Test this
                            token.setAll(tokenValue, tokenValue, lineNumber);
                            return token;
                        }
                        else {
                            //TODO: Test This
                            String id = getId(tokenValue);
                            token.setAllWithoutId(id, lineNumber);
                            return token;
                        }
                    }
                }
                //TODO: Test This
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
            else if (peekedChar.equals('e')) {
                while (letterList.contains(currentChar) && tokenValue.length() < 4 && letterList.contains(peekedChar)) {
                    // Create string self
                    currentChar = buff.getNextChar();
                    peekedChar = buff.peekNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("self")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString())) {
                            // TODO: Test this
                            token.setAll(tokenValue, tokenValue, lineNumber);
                            return token;
                        }
                        else {
                            //TODO: Test This
                            String id = getId(tokenValue);
                            token.setAllWithoutId(id, lineNumber);
                            return token;
                        }
                    }
                }
                //TODO: Test This
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
            else {
                //TODO: Test This
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
        }
        // while, write
        else if (currentChar.equals('w')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('h')) {
                while (letterList.contains(currentChar) && tokenValue.length() < 5 && letterList.contains(peekedChar)) {
                    // Create string while
                    currentChar = buff.getNextChar();
                    peekedChar = buff.peekNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("while")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString())) {
                            // TODO: Test this
                            token.setAll(tokenValue, tokenValue, lineNumber);
                            return token;
                        }
                        else {
                            //TODO: Test This
                            String id = getId(tokenValue);
                            token.setAllWithoutId(id, lineNumber);
                            return token;
                        }
                    }
                }
                //TODO: Test This
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
            else if (peekedChar.equals('r')) {
                while (letterList.contains(currentChar) && tokenValue.length() < 5 && letterList.contains(peekedChar)) {
                    // Create string write
                    currentChar = buff.getNextChar();
                    peekedChar = buff.peekNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("write")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString())) {
                            // TODO: Test this
                            token.setAll(tokenValue, tokenValue, lineNumber);
                            return token;
                        }
                        else {
                            //TODO: Test This
                            String id = getId(tokenValue);
                            token.setAllWithoutId(id, lineNumber);
                            return token;
                        }
                    }
                }
                //TODO: Test This
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
            else {
                //TODO: Test This
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
        }
        // read, return
        else if (currentChar.equals('r')) {
            currentChar = buff.getNextChar();
            tokenValue += currentChar;
            Character peekedChar = buff.peekNextChar();

            if (currentChar.equals('e')) {

                if (peekedChar.equals('a')) {
                    while (letterList.contains(currentChar) && tokenValue.length() < 4 && letterList.contains(peekedChar)) {
                        // Create string read
                        currentChar = buff.getNextChar();
                        peekedChar = buff.peekNextChar();
                        tokenValue += currentChar;
    
                        if (tokenValue.equals("read")) {
                            // Check if end of word or if word continues and is actually an ID
                            peekedChar = buff.peekNextChar();
                            if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString())) {
                                // TODO: Test this
                                token.setAll(tokenValue, tokenValue, lineNumber);
                                return token;
                            }
                            else {
                                //TODO: Test This
                                String id = getId(tokenValue);
                                token.setAllWithoutId(id, lineNumber);
                                return token;
                            }
                        }
                    }
                    //TODO: Test This
                    String id = getId(tokenValue);
                    token.setAllWithoutId(id, lineNumber);
                    return token;
                }
                else if (peekedChar.equals('t')) {
                    while (letterList.contains(currentChar) && tokenValue.length() < 6 && letterList.contains(peekedChar)) {
                        // Create string return
                        currentChar = buff.getNextChar();
                        peekedChar = buff.peekNextChar();
                        tokenValue += currentChar;
    
                        if (tokenValue.equals("return")) {
                            // Check if end of word or if word continues and is actually an ID
                            peekedChar = buff.peekNextChar();
                            if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString())) {
                                // TODO: Test this
                                token.setAll(tokenValue, tokenValue, lineNumber);
                                return token;
                            }
                            else {
                                //TODO: Test This
                                String id = getId(tokenValue);
                                token.setAllWithoutId(id, lineNumber);
                                return token;
                            }
                        }
                    }
                    //TODO: Test This
                    String id = getId(tokenValue);
                    token.setAllWithoutId(id, lineNumber);
                    return token;
                }
                else {
                    //TODO: Test This
                    String id = getId(tokenValue);
                    token.setAllWithoutId(id, lineNumber);
                    return token;
                }
            }
            else {
                //TODO: Test This
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
        }
        // let
        else if (currentChar.equals('l')) {
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('e')) {
                while (letterList.contains(currentChar) && tokenValue.length() < 3 && letterList.contains(peekedChar)) {
                    // Create string let
                    currentChar = buff.getNextChar();
                    peekedChar = buff.peekNextChar();
                    tokenValue += currentChar;

                    if (tokenValue.equals("let")) {
                        // Check if end of word or if word continues and is actually an ID
                        peekedChar = buff.peekNextChar();
                        if (peekedChar.equals(' ') || peekedChar.equals('	') || peekedChar.equals('\n') || peekedChar.equals('\r') || operatorsList.contains(peekedChar.toString())) {
                            // TODO: Test this
                            token.setAll(tokenValue, tokenValue, lineNumber);
                            return token;
                        }
                        else {
                            //TODO: Test This
                            String id = getId(tokenValue);
                            token.setAllWithoutId(id, lineNumber);
                            return token;
                        }
                    }
                }
                //TODO: Test This
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
            else {
                //TODO: Test This
                String id = getId(tokenValue);
                token.setAllWithoutId(id, lineNumber);
                return token;
            }
        }


        else if (letterList.contains(currentChar) || currentChar.equals('_')){
            String id = getId(tokenValue);
            if (id.contains(INVALIDSIGN.toString())) {
                id = new StringBuilder(id).deleteCharAt(id.length()-1).toString();
                token.setAll("invalidid", id, lineNumber);
                return token;
            }
            token.setAllWithoutId(id, lineNumber);
            return token;
        }

        else if (digitsList.contains(currentChar)) {
            String numString = getNumber(tokenValue);

            if (numString.contains(INVALIDSIGN.toString())) {
                numString = new StringBuilder(numString).deleteCharAt(numString.length()-1).toString();
                int letterCount = 0;
                int digitCount = 0;
                for (char c : numString.toCharArray()) {
                    if (digitsList.contains(c)) {
                        digitCount++;
                    } 
                    else {
                        letterCount++;
                    }
                }
                if (letterCount > digitCount) {
                    token.setAll("invalidid", numString, lineNumber);
                }
                else {
                    token.setAll("invalidnum", numString, lineNumber);
                }
                return token;
            }
            else if (numString.contains(".")) {
                token.setAll("floatnum", numString, lineNumber);
                return token;
            }
            else {
                token.setAll("intnum", numString, lineNumber);
                return token;
            }
        }

        else if (!currentChar.equals('_') && !digitsList.contains(currentChar) && !letterList.contains(currentChar) && !reservedList.contains(currentChar.toString()) && !currentChar.equals(' ') && !currentChar.equals('	') && !currentChar.equals('\n') && !currentChar.equals('\r')) {
            token.setAll("invalidchar", currentChar.toString(), lineNumber);
            return token;
        }

        else {
            // currentChar = buff.getNextChar();
            // TODO: Do I keep this buffer thing?
        }

        return token;
    }

    static public void writeToFiles(String tokenPath, String errorPath, TokenType token) throws IOException{
        String tokenContents = token.getTokenString();
        String tokenType = token.getType(); //i.e. float, integer...
        String tokenValue = token.getValue(); //i.e. "abc", "123"...
        int line = token.getLineNumber();

        if (token.isValid() && line != -1) {
            // Change line
            if (line > prevLine) {
                // Change line
                bwToken.newLine();
            }
            prevLine = line;
            bwToken.write(tokenContents + " ");
        }
        else if (!token.isValid() && line != -1) {
            String errorOutput = "Lexical error: Invalid ";
            // type.equals("invalidchar") || type.equals("invalidnum") || type.equals("invalidid")
            if (tokenType.equals("invalidid")) {
                String format = String.format("identifier \"%s\": line %d.", tokenValue, line);
                errorOutput += format;
            }
            else if (tokenType.equals("invalidnum")) {
                String format = String.format("number \"%s\": line %d.", tokenValue, line);
                errorOutput += format;
            }
            else if (tokenType.equals("invalidchar")){
                String format = String.format("character \"%s\": line %d.", tokenValue, line);
                errorOutput += format;
            }

            bwError.write(errorOutput);
            bwError.newLine();
        }
        else {
            // Do nothing for skipping a blank character
        }
    }


    // Constructor
    public Lexer(String readFilePath) {
        letterList = Arrays.asList(letters);
        digitsList = Arrays.asList(digits);
        reservedList = Arrays.asList(reserved);
        reservedWordsList = Arrays.asList(reservedWords);
        operatorsList = Arrays.asList(operators);
        eofFlag = false;

        // Read filepath
        filePath = readFilePath;
        try {
            inSrcFile = new FileReader(filePath);
            buff = new BufferFuncs(inSrcFile); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeFileReading() throws Exception {
        inSrcFile.close();
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFilePathWithoutName() {
        // Split filepath
        dirFilePathWithoutName = "";
        String[] splitPath = filePath.split("/");
        for (int i = 0; i < splitPath.length - 1; i++) {
            dirFilePathWithoutName += splitPath[i] + "/";
        }

        return dirFilePathWithoutName;
    }

    public String getLineNumber() {
        String lineNumberString = String.valueOf(lineNumber);
        return lineNumberString;
    }

    public String getFileName() {
        // Split filepath
        String[] splitPath = filePath.split("/");
        String file = splitPath[splitPath.length - 1];

        // Get filename without extension
        String[] splitFilename = file.split("\\.");
        String originalFilename = splitFilename[0];
        return originalFilename;
    }

    // Get the next token and avoid empty tokens
    public TokenType getNextToken() throws Exception{
        TokenType token = new TokenType();
        
        // TODO: Check if we need to place THIS in an IF/ELSE as well!
        // If token is empty (space, newline, etc.), loop until next token
        while (token.getType().equals("NAN")  && !buff.isEndOfFile()) {
            token = createToken();
        }
        
        // Write EOF character
        if (buff.isEndOfFile()) {
            token.setAll("EOF", "$", lineNumber);
            eofFlag = true;
            inSrcFile.close();
        }

        return token;
    }

    // Check if we are at the EOF
    public Boolean isEndOfFile() throws Exception{
        return eofFlag;
    }

    // For debugging
    public void skipReadLine(int lineSkips) throws Exception{
        for (int i = 0; i < lineSkips; i++) {
            buff.setReadLine();
        }
    }

    public void writeTokenFiles() throws Exception {
        // Split filepath
        String[] splitPath = filePath.split("/");
        String file = splitPath[splitPath.length - 1];

        // Get filename without extension
        String[] splitFilename = file.split("\\.");
        String originalFilename = splitFilename[0];

        // Initialize file names to output to
        String tokenFilename = originalFilename + ".outlextokens";
        String errorFilename = originalFilename + ".outlexerrors";
        String tokenPath = "";
        String errorPath = "";

        for (int i = 0; i < splitPath.length - 1; i++) {
            tokenPath += splitPath[i] + "/";
            errorPath += splitPath[i] + "/";
        }
        tokenPath += tokenFilename;
        errorPath += errorFilename;

        // Delete files before making new ones;
        File myFile = new File(tokenPath);
        if (myFile.exists() && myFile.isFile()) {
            myFile.delete();
        }
        myFile = new File(errorPath);
        if (myFile.exists() && myFile.isFile()) {
            myFile.delete();
        }

        // Initialize writers to files (true == append to file!)
        tokenFile = new FileWriter(tokenPath, true);
        errorFile = new FileWriter(errorPath, true);
        bwToken = new BufferedWriter(tokenFile);
        bwError = new BufferedWriter(errorFile);

        // Read all tokens and write to files simultaneously
        // while (!eofFlag || !buff.isEndOfFile()) {
        while (!eofFlag) {
            // TokenType token = createToken();
            TokenType token = getNextToken();
            writeToFiles(tokenPath, errorPath, token);
            // token.printAll();
            // if (eofFlag) {
            //     inSrcFile.close();
            // }
        }

        // Close files
        bwToken.close();
        bwError.close();
    }

    // public static void main(String args[]) throws Exception {

    //     // Read filepath
    //     String filePath = args[0];
    //     FileReader inSrcFile = new FileReader(filePath);
    //     buff = new BufferFuncs(inSrcFile);

    //     // Initialize given lexical elements
    //     letterList = Arrays.asList(letters);
    //     digitsList = Arrays.asList(digits);
    //     reservedList = Arrays.asList(reserved);
    //     reservedWordsList = Arrays.asList(reservedWords);
    //     operatorsList = Arrays.asList(operators);

    //     // Split filepath
    //     String[] splitPath = filePath.split("/");
    //     String file = splitPath[splitPath.length - 1];

    //     // Get filename without extension
    //     String[] splitFilename = file.split("\\.");
    //     String originalFilename = splitFilename[0];

    //     // Initialize file names to output to
    //     String tokenFilename = originalFilename + ".outlextokens";
    //     String errorFilename = originalFilename + ".outlexerrors";
    //     String tokenPath = "";
    //     String errorPath = "";

    //     for (int i = 0; i < splitPath.length - 1; i++) {
    //         tokenPath += splitPath[i] + "/";
    //         errorPath += splitPath[i] + "/";
    //     }
    //     tokenPath += tokenFilename;
    //     errorPath += errorFilename;

    //     // Delete files before making new ones;
    //     File myFile = new File(tokenPath);
    //     if (myFile.exists() && myFile.isFile()) {
    //         myFile.delete();
    //     }
    //     myFile = new File(errorPath);
    //     if (myFile.exists() && myFile.isFile()) {
    //         myFile.delete();
    //     }

    //     // Initialize writers to files (true == append to file!)
    //     tokenFile = new FileWriter(tokenPath, true);
    //     errorFile = new FileWriter(errorPath, true);
    //     bwToken = new BufferedWriter(tokenFile);
    //     bwError = new BufferedWriter(errorFile);

    //     // // Use for debugging
    //     // for (int i = 0; i < 49; i++) {
    //     //     buff.setReadLine();
    //     // }

    //     // Read all tokens and write to files simultaneously
    //     while (!buff.isEndOfFile()) {
    //         TokenType token = createToken();
    //         writeToFiles(tokenPath, errorPath, token);
    //         token.printAll();
    //     }

    //     // Close files
    //     bwToken.close();
    //     bwError.close();

    //     inSrcFile.close(); // Close filereader
    // }
}
