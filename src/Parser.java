// package src;
import java.io.*;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;
import java.util.Stack;


public class Parser {

    static String RULEARROW = "->";
    static String LAMBDATRANSITION = "&epsilon";

    // Rules
    // static String rulesFilePath = "/home/michael/Documents/School/COMP442/COMP442_ProjectRepo/Resources/Grammar/table.csv"; // Absolute path CHANGE THIS WHEN UPLOADING, RELATE TO PROJECT DIR
    static String rulesFilePath = "/home/michael/Documents/School/COMP442/COMP442_ProjectRepo/Resources/Grammar/table_semantic.csv"; // Absolute path CHANGE THIS WHEN UPLOADING, RELATE TO PROJECT DIR
    // static String rulesFilePath = "../Resources/Grammar/table.csv"; //TODO: UNCOMMMENT WHEN RUNNING NORMALLY! 
    static String firstFollowSetsFilePath = "/home/michael/Documents/School/COMP442/COMP442_ProjectRepo/Resources/Grammar/first_follow_sets.csv"; // ABS PATH, CHANGE WHEN UPLOADING
    // static String firstFollowSetsFilePath = "../Resources/Grammar/first_follow_sets.csv";
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

    // AST
    static Stack<AST> semanticStack = new Stack<>();
    static AST ast; 
    static int indentCount = 0;
    static TokenType leafStore;

    // TODO: Create list for epsilon transitions
    static String[] semanticTypes = {
        "_prog_", "_structimplorfunclist_", "_plusop_", "_leaf_", "_minusop_", "_orop_" , "_epsilon_",
        "_dimlist_", "_expr_", "_term_", "_assign_", "_relexpr_", "_mulop_",
        "_funclist_", "_inheritslist_", "_memberlist_", "_dot_", "_indicelist_",
        "_aparamslist_", "_intnum_", "_floatnum_", "_arithexpr_", "_notfactor_", "_signfactor_", "_fparamslist_",
        "_vardecl_", "_fparams_", "_fparamstaillist_", "_statorvardecllist_", "_paramlist_",
        "_funcdecl_", "_multop_", "_divop_", "_andop_", "_relopeq_", "_relopnoteq_", "_reloplt_", "_relopgt_",
        "_relopleq_", "_relopgeq_", "_statement_", "_voidtype_", "_addop_", "_factor_", "_plussign_", "_minussign_",
        "_statblocklist_", "_ifstatement_", "_whilestatement_", "_readstatement_", "_writestatement_",
        "_returnstatement_", "_structdecl_", "_impldefdecl_", "_funcdefdecl_", "_integertype_", "_floattype_", 
        "_idtype_", "_arraysize_", "_publicvisibility_", "_privatevisibility_"
    };
    static List<String> semanticTypesList;

    static String[] semanticListTypes = {
        "_structimplorfunclist_", "_indicelist_", "_fparamslist_", "_fparamstaillist_", "_statorvardecllist_",
        "_paramlist_", "_funclist_", "_aparamslist_", "_statblocklist_", "_inheritslist_", "_memberlist_",
        "_arraysize_", "_factor_", "_term_", "_arithexpr_", "_expr_", "_statement_", "_fparams_",
        "_structdecl_", "_impldefdecl_", "_funcdefdecl_", "_assignstatement_"
    };
    static List<String> semanticListTypesList;

    static String[] semanticLeafNodes = {
        "_plusop_", "_minusop_", "_orop_", "_leaf_", "_assign_", "_intnum_", "_floatnum_", "_mulop_", "_divop_",
        "_andop_", "_relopeq_", "_relopnoteq_", "_reloplt_", "_relopgt_", "_relopleq_", "_relopgeq_", "_voidtype_",
        "_plussign_", "_minussign_", "_integertype_", "_floattype_", "_idtype_", "_publicvisibility_", "_privatevisibility_",
        
    };
    static List<String> semanticLeafNodeList;


    // returnstatement = returnstat or getstat????
    // writestatment = putstat
    static String[] semanticPopCount1 = {
        "_writestatement_",
        "_returnstatement_",
        "_signfactor_",
        "_notfactor_",
        "_prog_",
        "_addop_",
        "_multop_"

    };
    static List<String> semanticPopCount1List;

    // readstatement = assignstat
    // _fparams_ = fcall
    static String[] semanticPopCount2 = {
        "_dot_",
        "_readstatement_",
        "_whilestatement_",
        "_dot_"
    };
    static List<String> semanticPopCount2List;

    // ifstatement = ifstat
    static String[] semanticPopCount3 = {
        "_funcdecl_",
        "_vardecl_",
        "_ifstatement_",
        "_relexpr_"
    };
    static List<String> semanticPopCount3List;




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


    // TODO: update this later with the added semantic attributes
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



    static void printAstTree() {
        String output = "";
        for (int i = 0; i < indentCount; i++) {
            output += "-";
        }
        output += parseStack.peek();

        System.out.println(output);
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

        // Start AST root with start symbol
        // ast = new AST(currentToken);
        TokenType rootToken = new TokenType();
        rootToken.setAll("ROOT", "ROOOT", 0);
        // ast = new AST("ROOT");
        ast = new AST(rootToken);

        // Parse the file
        while (!topStack.equals("$")) {
            topStack = parseStack.peek();

            if (currentToken.getType().equals("EOF") && parserTermination) {
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
                indentCount--;
            }

            // Check if top of stack is a terminal (x element of T)
            else if (terminalSymbolsList.contains(topStack)) {
                // If terminal matches the top stack symbol (x == a)
                if (currentToken.getType().equals(topStack)) {
                    leafStore = currentToken; // Store this to add node when _leaf_ is called
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
            else if (nonTerminalSymbolsList.contains(topStack)) {
                // // Create subtree
                // AST subAst = new AST(topStack);

                List<String> reversedNonTerminalRule = getReversedRule(topStack, currentToken.getType());
                if (reversedNonTerminalRule == null && currentToken.getType().equals("EOF")) {
                    reversedNonTerminalRule = getReversedRule(topStack, currentToken.getValue());
                }

                // If the non-terminal rule exists
                if (reversedNonTerminalRule != null) {
                    parseStack.pop();
                    for (int i = 0; i < reversedNonTerminalRule.size(); i++) {
                        parseStack.push(reversedNonTerminalRule.get(i));

                        // // Create ast nodes for children
                        // AST childAst = new AST(reversedNonTerminalRule.get(i));
                        // // Link children to subtree root
                        // subAst.addChild(childAst);
                    }
                    // Link to main node
                    // ast.addChild(subAst);
                    indentCount++;
                }
                else if (currentToken.getType().equals("EMERGENCY END")) {
                    parseStack.pop();
                }
                else {
                    skipErrors();
                    errorFlag = true;
                }
            }

            else if (semanticTypesList.contains(topStack)) {
                // TODO: Use semantic check method here
                // createNodeOrTree(topStack);
                createNodeOrTree(topStack);
                topStack = parseStack.peek();
            }

            // Comment out to remove file writing and syso outputting            
            getDerivationOutput();
        }

        if (!topStack.equals("$") || errorFlag.equals(true)) {
            // _prog_ is a single node to link to root: root -> prog -> structimplorfunc...
            ast.addChild(semanticStack.pop());
            return false;
        }
        else {
            // Link subtree nodes to main tree
            ast.addChild(semanticStack.pop());
            return true;
        }

    }


    static void createNodeOrTree(String semanticType) {
        AST currNode = new AST();
        if (semanticType.equals("_epsilon_")) {
            currNode.setASTToken("epsilon", "epsilon", -1);
            semanticStack.push(currNode);
            parseStack.pop(); // Remove the semantic attribute from the parse stack
        }
        else if (semanticListTypesList.contains(semanticType)) {
            popUntilEpsilon(semanticType, currNode);
            // semanticStack.push(currNode); // ALREADY ADDED IN popUntilEpsilon
            parseStack.pop(); // Remove the semantic attribute from the parse stack
        }
        else if (semanticPopCount1List.contains(semanticType) && !semanticType.equals("_prog_")) {
            popWantedTimes(semanticType, 1, currNode);
            parseStack.pop(); // Remove the semantic attribute from the parse stack
        }
        else if (semanticPopCount2List.contains(semanticType)) {
            popWantedTimes(semanticType, 2, currNode);
            parseStack.pop(); // Remove the semantic attribute from the parse stack
        }
        else if (semanticPopCount3List.contains(semanticType)) {
            popWantedTimes(semanticType, 3, currNode);
            parseStack.pop(); // Remove the semantic attribute from the parse stack
        }
        else if (semanticLeafNodeList.contains(semanticType)) {
            currNode.setASTToken(leafStore);
            semanticStack.push(currNode);
            parseStack.pop(); // Remove the semantic attribute from the parse stack
        }
        // Get remaining nodes from AST and assign them to root
        else if (semanticType.equals("_prog_")) {
            while (!semanticStack.isEmpty()) {
                currNode.setASTToken("Prog", "Prog", -1);
                AST childNode = semanticStack.pop();
                currNode.addChild(childNode);
            }
            semanticStack.push(currNode);
            parserTermination = true; // Terminate parsing!
            parseStack.pop(); // Remove the semantic attribute from the parse stack
        }
    }


    // Pop X wanted times for trees with a given X number of children
    static void popWantedTimes(String parentType, int popCount, AST subtreeNode) {
        subtreeNode.setASTToken(parentType, parentType, -1);

        for (int i = 0; i < popCount; i++) {
            AST childNode = semanticStack.pop();
            subtreeNode.addChild(childNode);
        }

        semanticStack.push(subtreeNode);
    }

    // Pop for a list without knowing the size
    static void popUntilEpsilon(String listType, AST subtreeNode) {
        subtreeNode.setASTToken(listType, listType, -1);

        while (!semanticStack.peek().getToken().getType().equals("epsilon")) {
            AST leafNode = semanticStack.pop();
            subtreeNode.addChild(leafNode);
        }

        // Remove epsilon flag
        semanticStack.pop();

        // Add to stack
        semanticStack.push(subtreeNode);
    }


    static public void skipErrors() throws Exception {
        System.out.println("Skip errors entered!");
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

    static public void printDfsOutput() {
        List<AST> currChildren = new ArrayList<>();
        Stack<AST> stack = new Stack<>();
        Stack<Integer> depthStack = new Stack<>();
        AST root = ast;
        AST curr = new AST();
        int currDepth = 0;
        stack.push(root);
        depthStack.push(currDepth);

        while (!stack.isEmpty()) {
            curr = stack.pop(); // Get node
            String nodeType = curr.getToken().getType();
            currDepth = depthStack.pop(); // Get depth
            currChildren = curr.getChildren(); // Get children
            String outputLine = "";

            for (int i = 0; i < currChildren.size(); i++) {
                int tempDepth = currDepth + 1;
                stack.push(currChildren.get(i));
                depthStack.push(tempDepth);
            }
            // Add spacing
            for (int i = 0; i < currDepth; i++) {
                outputLine += "| ";
            }
            
            outputLine += nodeType;
            System.out.println(outputLine);
        }

    }


    public static void main(String args[]) throws Exception{

        // --------------------Initialization-------------------------
        // Initialize terminal and non-terminal arraylist to search from when parsing
        nonTerminalSymbolsList = Arrays.asList(nonTerminalSymbols);
        terminalSymbolsList = Arrays.asList(terminalSymbols);
        semanticTypesList = Arrays.asList(semanticTypes);
        semanticListTypesList = Arrays.asList(semanticListTypes);
        semanticLeafNodeList = Arrays.asList(semanticLeafNodes);
        semanticPopCount1List = Arrays.asList(semanticPopCount1);
        semanticPopCount2List = Arrays.asList(semanticPopCount2);
        semanticPopCount3List = Arrays.asList(semanticPopCount3);

        //----------------------Rule file reading----------------------
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
        //-----------------------------------------------------------
        


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
        System.out.println("------------End of parse--------------");

        // Print tree
        printDfsOutput();
        
        // Close reading and writing files
        closeReadingWritingFiles();
    }
}
