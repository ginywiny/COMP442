// package src;
import java.io.*;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Stack;

public class Parser {

    static String RULEARROW = "->";
    static String LAMBDATRANSITION = "&epsilon";

    // Rules
    static String rulesFilePath = "/home/michael/Documents/School/COMP442/COMP442_ProjectRepo/Resources/Grammar/table.csv"; // Absolute path CHANGE THIS WHEN UPLOADING, RELATE TO PROJECT DIR
    // static String rulesFilePath = "../Resources/Grammar/table.csv"; //TODO: UNCOMMMENT WHEN RUNNING NORMALLY! 
    // static String rulesFilePath = "Resources/Grammar/table.csv"; // TODO: UNCOMMENT WHEN DEBUGGING
    static String firstFollowSetsFilePath = "/home/michael/Documents/School/COMP442/COMP442_ProjectRepo/Resources/Grammar/first_follow_sets.csv";
    static HashMap<List<String>, String> ruleMap = new HashMap<>();
    static HashMap<List<String>, List<String>> firstFollowMap = new HashMap<>();

    // Output files
    static FileWriter derivationFile;
    static FileWriter errorSyntaxFile;
    static BufferedWriter bwDerivation;
    static BufferedWriter bwSyntaxError;
    static String syntaxDerivationPath; 
    static String syntaxErrorPath;

    // Terminals and NonTerminals from Rules
    static String[] nonTerminalSymbols =  {
        "START", "ADDOP", "APARAMS", "APARAMSTAIL", "ARITHEXPR", "ARRAYSIZE", "ARRAYSIZE1",
        "ASSIGNOP", "EXPR", "EXPR1", "FACTOR", "FACTOR1", "FACTOR2", "FPARAMS", "FPARAMSTAIL",
        "FUNCBODY", "FUNCDECL", "FUNCDEF", "FUNCHEAD", "IMPLDEF", "INDICE", "MEMBERDECL", "MULTOP", 
        "OPTSTRUCTDECL2", "PROG", "RELEXPR", "RELOP", "REPTAPARAMS1", "REPTFPARAMS3", "REPTFPARAMS4", 
        "REPTFPARAMSTAIL4", "REPTFUNCBODY1", "REPTIDNEST1", "REPTIMPLDEF3", "REPTOPTSTRUCTDECL22", 
        "REPTPROG0", "REPTSTATBLOCK1", "REPTSTRUCTDECL4", "REPTVARDECL4", "RETURNTYPE", "RIGHTRECARITHEXPR",
        "RIGHTRECTERM", "SIGN", "STATBLOCK", "STATEMENT", "STATEMENT1", "STATEMENT2", "STATEMENT3", "STRUCTDECL",
        "STRUCTORIMPLORFUNC", "TERM", "TYPE", "VARDECL", "VARDECLORSTAT", "VARIABLE", "VARIABLE1", "VARIABLE2", 
        "VARIABLE3", "VISIBILITY"};
    static List<String> nonTerminalSymbolsList;
        
    // static String[] terminalSymbols = {
    //     "," , "+", "-", "|", "[", "intLit", "]", "=", "struct", "id", "{", "}", ";", "(", ")", "floatLit", "!", ":"
    //     , "void", ".", "*", "/", "&", "inherits", "eq", "geq", "gt", "leq", "lt", "neq", "if", "then", "else", "read"
    //     , "return", "while", "write", "float", "integer", "private", "public", "func", "impl", "let" 
    // };
    static String[] terminalSymbols = {
        "comma" , "plus", "minus", "or", "arrow", "opensqbr", "closesqbr", "assign", "struct", "id", "opencubr",
        "closecubr", "semi", "openpar", "closepar", "not", "colon", "floatnum", "intnum",
        "void", "dot", "mult", "div", "and", "inherits", "eq", "geq", "gt", "leq", "lt", "noteq", "if", "then", "else", "read",
        "return", "while", "write", "float", "integer", "private", "public", "func", "impl", "let" 
    };
    static List<String> terminalSymbolsList;

    // Parser stack 
    static Stack<String> parseStack = new Stack<>();
    static List<String> derivationList = new ArrayList<>();
    static Boolean parserTermination = false;

    // Lexer
    static Lexer lexer;
    static TokenType currentToken;




    // Put all the rules into a dictionary to parse
    static void readRules() throws Exception {
        // Parse rule file and create arraylist of all contents
        List<String[]> contents = new ArrayList<>();
        FileReader temp = new FileReader(rulesFilePath);
        try(BufferedReader br = new BufferedReader(temp)) {
            String line = "";
            while ((line = br.readLine()) != null) {
                contents.add(line.split(","));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        temp.close();


        // Read all terminals (columns) from the csv file 
        List<String> terminals = new ArrayList<>();
        for (int i = 0; i < contents.get(0).length; i++) {
            terminals.add(contents.get(0)[i]);
        }

        // Get the arraylist of state transitions for each NONTERMINAL
        // Start from 1 to skip the terminal row
        List<List<String>> nonTerminals = new ArrayList<>();
        for (int i = 1; i < contents.size(); i++) {
            String[] terminalRow = contents.get(i);
            List<String> currentRowList = new ArrayList<>(); 

            for (int j = 0; j < terminalRow.length; j++) {
                currentRowList.add(terminalRow[j]);
            }
            nonTerminals.add(currentRowList);
        }

        
        // Loop through the non-terminals
        for (int i = 0; i < nonTerminals.size(); i++) {
            // Get the row for the non-terminal
            List<String> currentNonTerminalRow = nonTerminals.get(i);

            // Start at 1 to skip empty 0 column of terminals
            // for (int j = 1; j < currentNonTerminalRow.size(); j++){
            for (int j = 1; j < terminals.size(); j++){
                // Check if the column is empty
                String currentNonTerminalCol = currentNonTerminalRow.get(j);
                String emptyCheck = currentNonTerminalCol.replaceAll("\\s", "");
                if (emptyCheck.length() == 0) {
                    continue;
                }

                // Print and save rule to hashmap
                int row = i + 1;
                String rowNonTerminal = currentNonTerminalRow.get(0);
                String colTerminal = terminals.get(j);
                String rowColLanguage = currentNonTerminalRow.get(j);

                // System.out.println("Row: " + row + " Col: " + j + " " + rowNonTerminal + " " + colTerminal + " " + rowColLanguage);
                ruleMap.put(Collections.unmodifiableList(Arrays.asList(rowNonTerminal, colTerminal)), rowColLanguage); // (row, col)
            }
        }
    }


    // Put all first and follow sets into a hashmap
    static void readFirstFollowSets() throws Exception {
        // Parse first follow sets file and create arraylist of all contents
        List<String[]> contents = new ArrayList<>();
        FileReader temp = new FileReader(firstFollowSetsFilePath);
        try(BufferedReader br = new BufferedReader(temp)) {
            String line = "";
            while ((line = br.readLine()) != null) {
                contents.add(line.split(","));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        temp.close();

        // Read all terminals (columns) from the csv file 
        List<String> terminals = new ArrayList<>();
        for (int i = 0; i < contents.get(0).length; i++) {
            terminals.add(contents.get(0)[i]);
        }

        // Get the arraylist of state transitions for each NONTERMINAL
        // Start from 1 to skip the terminal row
        List<List<String>> nonTerminals = new ArrayList<>();
        for (int i = 1; i < contents.size(); i++) {
            String[] terminalRow = contents.get(i);
            List<String> currentRowList = new ArrayList<>(); 

            for (int j = 0; j < terminalRow.length; j++) {
                currentRowList.add(terminalRow[j]);
            }
            nonTerminals.add(currentRowList);
        }

        
        // Loop through the non-terminals
        for (int i = 0; i < nonTerminals.size(); i++) {
            // Get the row for the non-terminal
            List<String> currentNonTerminalRow = nonTerminals.get(i);

            // Start at 1 to skip empty 0 column of terminals
            for (int j = 1; j < currentNonTerminalRow.size(); j++){
                // Check if the column is empty
                String currentNonTerminalCol = currentNonTerminalRow.get(j);
                String emptyCheck = currentNonTerminalCol.replaceAll("\\s", "");
                if (emptyCheck.length() == 0) {
                    continue;
                }

                // Print and save rule to hashmap
                int row = i + 1;
                String rowNonTerminal = currentNonTerminalRow.get(0); // Nonterminal: START, ARRAYSIZE, etc.
                String colTerminal = terminals.get(j); // Column set: First, follow

                String rowColSet = currentNonTerminalRow.get(j); // Get the set list as a string
                String[] rowColSetArr = rowColSet.split(" ");
                List<String> setList = Arrays.asList(rowColSetArr);

                // System.out.println("Row: " + row + " Col: " + j + " " + rowNonTerminal + " " + colTerminal + " " + setList);
                firstFollowMap.put(Collections.unmodifiableList(Arrays.asList(rowNonTerminal, colTerminal)), setList); // (row, col)
            }
        }
    }


    // Row:Col
    static List<String> getReversedRule(String row, String col) {
        List<String> reversedRulesArray = new ArrayList<>();

        // Get the rule from the grammar table
        String normalRule = ruleMap.get(Arrays.asList(row, col));
        if (normalRule == null) {
            return null;
        }

        // Take only the RHS of the arrow
        String normalProduction = normalRule.split(RULEARROW)[1]; 

        // Swap last to first order
        String[] normalProductionList = normalProduction.split("\\s");
        for (int i = 0; i < normalProductionList.length; i++) {
            String emptyCheck = normalProductionList[i].replaceAll("\\s", "");
            if (emptyCheck.length() == 0) {
                continue;
            }
            reversedRulesArray.add(0, emptyCheck);
        }

        return reversedRulesArray;
    }

    // TODO: Change for file output (derivation file)
    // Print the derivation string (change to output file)
    static void getDerivationOutput() throws Exception {
        String derivationString = "";
        derivationString += "START => ";
        Stack<String> stackOut = new Stack<>();

        if (!parseStack.isEmpty()) {
            String tempStack = parseStack.peek();
            while (!parseStack.empty()) {
                tempStack = parseStack.peek();
                if (tempStack.equals(("$"))) {
                    break;
                }
                tempStack = parseStack.pop();
                stackOut.push(tempStack);
                derivationString += tempStack + " ";
            }
    
            while (!stackOut.empty()) {
                parseStack.push(stackOut.pop());
            }
        }

        // Write derivation to parser file
        writeParserOutputFiles(derivationString, true); // TODO: Comment to remove writing to files
        System.out.println(derivationString);
    }


    // Parse the tokens
    static Boolean parse() throws Exception {
        // Base case to start syntax analysis
        parseStack.push("$");
        parseStack.push("START");

        // Get current token and top of stack
        currentToken = new TokenType();
        currentToken = lexer.getNextToken();
        String topStack = parseStack.peek();
        Boolean errorFlag = false;

        // Parse the file
        while (!topStack.equals("$")) {
            topStack = parseStack.peek();

            if (currentToken.getType().equals("EOF")) {
                // Get $ from the EOF token from the LEXER
                List<String> reversedNonTerminalRule = getReversedRule(topStack, currentToken.getValue()); // Get REPTPROG0:$
                if (reversedNonTerminalRule != null) {
                    parseStack.pop();
                    for (int i = 0; i < reversedNonTerminalRule.size(); i++) {
                        parseStack.push(reversedNonTerminalRule.get(i));
                    }
                    // No more tokens! Set to null
                    currentToken = new TokenType();
                    currentToken.setAll("EMERGENCY END", "NAN", -1);
                    parserTermination = true; // Stop parsing!
                }
                else {
                    System.out.println("Error at: " + currentToken.getValue() + " " + currentToken.getLineNumber());
                    skipErrors();
                    errorFlag = true;
                }
            }

            // Remove lambda from derivations
            else if (topStack.equals(LAMBDATRANSITION)) {
                parseStack.pop();
                topStack = parseStack.peek();
            }
            // Check if top of stack is a terminal (x element of T)
            else if (terminalSymbolsList.contains(topStack)) {
                // If terminal matches the top stack symbol (x == a)
                if (currentToken.getType().equals(topStack)) {
                    parseStack.pop();
                    currentToken = lexer.getNextToken();
                }
                else {
                    skipErrors();
                    errorFlag = true;
                }
            }
            // Skip comments
            else if (currentToken.getType().equals("inlinecmt") || currentToken.getType().equals("blockcmt")) {
                // System.out.println("Skipped comment: " + currentToken.getValue());
                currentToken = lexer.getNextToken();
            }
            // If top of stack is a non-terminal
            else {
                List<String> reversedNonTerminalRule = getReversedRule(topStack, currentToken.getType());
                // If the non-terminal rule exists
                if (reversedNonTerminalRule != null) {
                    parseStack.pop();
                    for (int i = 0; i < reversedNonTerminalRule.size(); i++) {
                        parseStack.push(reversedNonTerminalRule.get(i));
                    }
                }
                else if (currentToken.getType().equals("EMERGENCY END")) {
                    parseStack.pop();
                }
                else {
                    skipErrors();
                    errorFlag = true;
                }
            }
            getDerivationOutput();
        }

        if (!topStack.equals("$") || errorFlag.equals(true)) {
            return false;
        }
        else {
            return true;
        }

    }

    static public void skipErrors() throws Exception {
        // TODO: Fix comment message
        String line = lexer.getLineNumber();
        String stackTop = parseStack.peek();
        List<String> firstSet = firstFollowMap.get(Arrays.asList(stackTop, "first")); // FIRST(top())
        List<String> followSet = firstFollowMap.get(Arrays.asList(stackTop, "follow")); // FOLLOW(top())

        // If a terminal!
        if (firstSet == null && followSet == null) {
            // Make terminal the first set
            firstSet = new ArrayList<>();
            firstSet.add(stackTop);
            followSet = new ArrayList<>();
            followSet.add("NA");
        }
        // If non-terminal has no firstSet
        if (firstSet == null) {
            firstSet = new ArrayList<>();
            firstSet.add("NA");
        }
        // If non-terminal has no followSet
        if (followSet == null) {
            followSet = new ArrayList<>();
            followSet.add("NA");
        }

        // Writing error message
        if (!firstSet.contains("NA")) {
            String expected = "[ ";
            for (int i = 0; i < firstSet.size(); i++) {
                expected += firstSet.get(i) + " ";
            }
            expected += "]";
            String errorMessage = "Syntax error: " + "[ " + currentToken.getValue() + " ]" + " at line: " + line + " expected: " + expected;
            System.out.println(errorMessage);
            writeParserOutputFiles(errorMessage, false); // Write to error file
        }

        // Error handling
        if (currentToken.getType().equals("EOF") || followSet.contains(currentToken.getType())) {
            parseStack.pop();
        }
        else {
            while (((!firstSet.contains(currentToken.getType()) || (firstSet.contains(LAMBDATRANSITION) && !followSet.contains(currentToken.getType()))) && !lexer.isEndOfFile())) {
                currentToken = lexer.getNextToken();
                // if (lexer.isEndOfFile()) {
                // }
            }
        }

    }

    // Write derivation and error file output
    static void writeParserOutputFiles(String message, Boolean isDerivation) throws Exception {
        if (isDerivation) {
            bwDerivation.write(message);
            bwDerivation.newLine();
        }
        else {
            bwSyntaxError.write(message);
            bwSyntaxError.newLine();
        }
    }

    // Close files used to reading and writing
    static void closeReadingWritingFiles() throws Exception{
        // Close writing files
        bwDerivation.close();
        bwSyntaxError.close();
        // Close FileReader
        lexer.closeFileReading();
    }

    public Parser() throws Exception {
        // Test read src file
        // lexer = new Lexer(args[0]);
        // Initialize terminal and non-terminal arraylist to search from when parsing
        nonTerminalSymbolsList = Arrays.asList(nonTerminalSymbols);
        terminalSymbolsList = Arrays.asList(terminalSymbols);
        readRules();
        readFirstFollowSets();
        syntaxDerivationPath = lexer.getFilePathWithoutName() + "/" + lexer.getFileName() + ".outderivation";
        syntaxErrorPath = lexer.getFilePathWithoutName() + "/" + lexer.getFileName() + ".outsyntaxerrors";
    }

    

    public static void main(String args[]) throws Exception{

        // --------------------Initialization-------------------------
        // Initialize terminal and non-terminal arraylist to search from when parsing
        nonTerminalSymbolsList = Arrays.asList(nonTerminalSymbols);
        terminalSymbolsList = Arrays.asList(terminalSymbols);
        // Test read src file
        lexer = new Lexer(args[0]);
        // Generate rule dictionary map
        readRules();
        // Generate first follow sets dictionary map
        readFirstFollowSets();

        //---------------------File writer initialization------------
        // Delete old files and create new ones
        syntaxDerivationPath = lexer.getFilePathWithoutName() + "/" + lexer.getFileName() + ".outderivation";
        syntaxErrorPath = lexer.getFilePathWithoutName() + "/" + lexer.getFileName() + ".outsyntaxerrors";
        // Delete files before making new ones;
        File myFile = new File(syntaxDerivationPath);
        if (myFile.exists() && myFile.isFile()) {
            myFile.delete();
        }
        myFile = new File(syntaxErrorPath);
        if (myFile.exists() && myFile.isFile()) {
            myFile.delete();
        }
        // Initialize writers to files (true == append to file!)
        derivationFile = new FileWriter(syntaxDerivationPath, true);
        errorSyntaxFile = new FileWriter(syntaxErrorPath, true);
        bwDerivation = new BufferedWriter(derivationFile);
        bwSyntaxError = new BufferedWriter(errorSyntaxFile);
        


        // // Use to generate the files
        // try {
        //     lexer.writeTokenFiles();
        // }
        // catch (Exception e) {
        //     System.out.println("We stopped here");
        // }

        // lexer.skipReadLine(70);
        // TokenType test2 = new TokenType();
        // for (int i = 0; i < 40; i++) {
        //     test2 = lexer.getNextToken();
        //     test2.printAll();
        // }



        // Run parser
        parse();


        // Close reading and writing files
        closeReadingWritingFiles();
    }
}
