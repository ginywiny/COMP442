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
    Character[] letters = {'a','b','c','d','e','f','g','h','i',
                    'j','k','l','m','n','o','p','q','r',
                    's','t','u','v','w','x','y','z',
                    'A','B','C','D','E','F','G','H','I',
                    'J','K','L','M','N','O','P','Q','R',
                    'S','T','U','V','W','X','Y','Z'};
    List<Character> letterList;

    Integer[] digits = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    List<Integer> digitsList;

    String[] reserved = {"==", "<>", "<", ">", "<=", ">=", 
                        "+", "-", "*", "/", "=", "|", "&",
                        "!", "(", ")", "{", "}", "[", "]",
                        ";", ",", ".", ":", "::", "->", "if",
                        "then", "else", "integer", "float", "void",
                        "public", "private", "func", "var", "struct",
                        "while", "read", "write", "return", "self",
                        "inherits", "let", "impl"};
    List<String> reservedList;

    String[] commentsReserved = {"//", "/*", "*/" , "//"};
    List<String> reservedCommentList;

    static BufferedReader inputBuffer;
    static int lineNumber = 1;

    /*---------------------------------------------------------------*/
    

    // String nextToken() {
    //     return null;
    // }
    // char nextChar() {
    //     return 'p';
    // }
    // char backupChar() {
    //     return 'p';
    // }
    // Boolean isFinalState(String state)
    // String table(String state, int col)



    static TokenType createToken(BufferFuncs buff) throws Exception {

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
        // Comments
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




        // TODO: If null, finish reading
        // else {
        //     System.out.println("END OF FILE");
        // }
        

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
        BufferFuncs buffer = new BufferFuncs(new FileReader(filePath));


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


        for (int i = 0; i < 46; i++) {
            buffer.setReadLine();
            lineNumber++;
        }

        // Create character
        for (int i = 0; i < 30; i++) {
            TokenType token = createToken(buffer);
            token.printAll();
        }



        // while(true) {
        //     Character c = getNextChar(inputBuffer);
        // }

    }

}
