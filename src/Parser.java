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
    // static String rulesFilePath = "/home/michael/Documents/School/COMP442/COMP442_ProjectRepo/Resources/Grammar/table.csv"; // Absolute path CHANGE THIS WHEN UPLOADING, RELATE TO PROJECT DIR
    static String rulesFilePath = "/home/michael/Documents/School/COMP442/COMP442_ProjectRepo/Resources/Grammar/table2.csv"; // Absolute path CHANGE THIS WHEN UPLOADING, RELATE TO PROJECT DIR
    // static String rulesFilePath = "../Resources/Grammar/table.csv"; //TODO: UNCOMMMENT WHEN RUNNING NORMALLY! 
    // static String rulesFilePath = "Resources/Grammar/table.csv"; // TODO: UNCOMMENT WHEN DEBUGGING
    static HashMap<List<String>, String> ruleMap = new HashMap<>();;

    // Output files
    static FileWriter derivationFile;
    static FileWriter errorFile;
    static BufferedWriter bwDerivation;
    static BufferedWriter bwError;

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

    // Lexer
    static Lexer lexer; 

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

    // Print the derivation string (change to output file)
    static void printDerivations() {
        String derivationString = "";
        derivationString += "START => ";
        // LinkedList<String> stackOut = new LinkedList<>();
        Stack<String> stackOut = new Stack<>();

        String tempStack = parseStack.peek();
        while (!parseStack.empty()) {
            tempStack = parseStack.peek();
            if (tempStack.equals(("$"))) {
                break;
            }
            tempStack = parseStack.pop();
            // stackOut.add(tempStack);
            stackOut.push(tempStack);
            derivationString += tempStack + " ";
        }

        while (!stackOut.empty()) {
            // parseStack.push(stackOut.remove());
            parseStack.push(stackOut.pop());
        }

        System.out.println(derivationString);
    }

    // Add derivations to the list
    // TODO" TRACK DERIVATIONS FROM THE STACK INSTEAD!!!!
    // THIS IS NO GOOOOOOOOOOOOOOOD!!
    static void addDerivation(List<String> productions) {
        if (derivationList.size() == 0) {
            for (int i = 0; i < productions.size(); i++) {
                derivationList.add(0, productions.get(i));
            }
        }
        else {
            for (int i = 0; i < derivationList.size(); i++) {
                String nonTerminalCheck = derivationList.get(i);
                if (nonTerminalSymbolsList.contains(nonTerminalCheck)) {
                    for (int j = 0; j < productions.size(); j++) {
                        derivationList.remove(i);
                        derivationList.add(i, productions.get(j));
                    }
                    break;
                }
            }
        }
    }


    // Parse the tokens
    static Boolean parse() throws Exception {
        List<String> derivationString = new ArrayList<>(); 
        List<String> startString = new ArrayList<>(); 

        // Base case to start syntax analysis
        parseStack.push("$");
        parseStack.push("START");

        // derivationList.add("START");
        // derivationList.add("=>");

        // Get current token and top of stack
        TokenType currentToken = lexer.getNextToken();
        String topStack = parseStack.peek();
        Boolean errorFlag = false;

        // Parse the file
        while (!topStack.equals("$")) {
            topStack = parseStack.peek();

            // Remove lambda from derivations
            if (topStack.equals(LAMBDATRANSITION)) {
                parseStack.pop();
            }
            // Check if top of stack is a terminal (x element of T)
            else if (terminalSymbolsList.contains(topStack)) {

                // If terminal matches the top stack symbol (x == a)
                if (currentToken.getType().equals(topStack)) {
                    parseStack.pop();
                    currentToken = lexer.getNextToken();
                }
                else {
                    // TODO: skipErrors()
                    // TODO: error = true
                    errorFlag = true;
                }

            }
            // Skip comments
            else if (currentToken.getType().equals("inlinecmt") || currentToken.getType().equals("blockcmt")) {
                System.out.println("Skipped comment: " + currentToken.getValue());
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
                else {
                    // TODO: skipErrors()
                    // TODO: error = true
                    System.out.println("Error at: " + currentToken.getValue() + " " + currentToken.getLineNumber());
                    errorFlag = true;
                }
            }
            printDerivations();
        }

        if (!topStack.equals("$") || errorFlag.equals(true)) {
            return false;
        }
        else {
            return true;
        }

    }

    public static void main(String args[]) throws Exception{

        // Initialize terminal and non-terminal arraylist to search from when parsing
        nonTerminalSymbolsList = Arrays.asList(nonTerminalSymbols);
        terminalSymbolsList = Arrays.asList(terminalSymbols);
        
        // Test read src file
        lexer = new Lexer(args[0]);
        System.out.println(lexer.getFileName());
        System.out.println(lexer.getFilePath());
        System.out.println("-------------------");

        // Generate rule dictionary map
        readRules();

        lexer.writeTokenFiles();

        // Run parser
        // parse();

        // lexer.skipReadLine(11);
        // TokenType test2 = new TokenType();
        // for (int i = 0; i < 40; i++) {
        //     test2 = lexer.getNextToken();
        //     test2.printAll();
        // }


        
        // Close FileReader
        lexer.closeFileReading();
    }
}
