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
    

    String nextToken() {
        return null;
    }
    char nextChar() {
        return 'p';
    }
    char backupChar() {
        return 'p';
    }
    // Boolean isFinalState(String state)
    // String table(String state, int col)

    // static TokenType createToken(BufferedReader buff) throws Exception {
    static TokenType createToken(BufferFuncs buff) throws Exception {

        // Character currentChar = getNextChar(buff);
        Character currentChar = buff.getNextChar();
        TokenType token = new TokenType();

        // Cast character to string add to token object
        String tokenValue = Character.toString(currentChar);

        // New line, increment line count
        if (currentChar.equals('\n')) {
            lineNumber++;
            return token; // TODO: Fix this
        }
        else if (currentChar.equals('=')) {
            // Character peekedChar = peekNextChar(buff);
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('=')) {
                tokenValue += peekedChar;
                token.setAll("eq", tokenValue, lineNumber);
                // getNextChar(buff); // Advance the buffer by a char
                buff.getNextChar(); // Advance the buffer by a char
            }
            else {
                token.setAll("assign", tokenValue, lineNumber);
            }
        }
        else if (currentChar.equals('<')) {
            // Character peekedChar = peekNextChar(buff);
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('>')) {
                tokenValue += peekedChar;
                token.setAll("noteq", tokenValue, lineNumber);
                // getNextChar(buff); // Advance the buffer by a char
                buff.getNextChar(); // Advance the buffer by a char
            }
            else if (peekedChar.equals('=')) {
                tokenValue += peekedChar;
                token.setAll("leq", tokenValue, lineNumber);
                getNextChar(buff); // Advance the buffer by a char
            }
            else {
                token.setAll("lt", tokenValue, lineNumber);
            }
        }
        else if (currentChar.equals('>')) {
            // Character peekedChar = peekNextChar(buff);
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('=')) {
                tokenValue += peekedChar;
                token.setAll("geq", tokenValue, lineNumber);
                // getNextChar(buff); // Advance the buffer by a char
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
            token.setAll("minus", tokenValue, lineNumber);
        }
        else if (currentChar.equals('*')) {
            token.setAll("mult", tokenValue, lineNumber);
        }
        else if (currentChar.equals('/')) {
            // Character peekedChar = peekNextChar(buff);
            Character peekedChar = buff.peekNextChar();

            if (peekedChar.equals('*')) {
                // TODO: Implement comment multiple line (loop till */)
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
        // inputBuffer = new BufferedReader(new FileReader(filePath));


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

        // Create character
        for (int i = 0; i < 10; i++) {
            
            // TokenType token = createToken(inputBuffer);
            TokenType token = createToken(buffer);
            token.printAll();
        }

        // Read lines synchronously
        // String nullCheck = new String();
        // nullCheck = inputBuffer.readLine();

        // while(true) {
        //     Character c = getNextChar(inputBuffer);
        // }

    }

}
