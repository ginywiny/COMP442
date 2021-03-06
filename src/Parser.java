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
    // Assignment 3 grammar
    // static String rulesFilePath = "/home/michael/Documents/School/COMP442/COMP442_ProjectRepo/Resources/Grammar/table_semantic.csv"; // Absolute path CHANGE THIS WHEN UPLOADING, RELATE TO PROJECT DIR
    // static String rulesFilePath = "../Resources/Grammar/table_semantic.csv"; // Absolute path CHANGE THIS WHEN UPLOADING, RELATE TO PROJECT DIR
    
    static String firstFollowSetsFilePath = "/home/michael/Documents/School/COMP442/COMP442_ProjectRepo/Resources/Grammar/first_follow_sets.csv"; // ABS PATH, CHANGE WHEN UPLOADING
    // static String firstFollowSetsFilePath = "../Resources/Grammar/first_follow_sets.csv"; // TODO: Uncomment when submitting!
    
    // TESTING FOR NEWLY UPDATED GRAMMAR
    static String rulesFilePath = "/home/michael/Documents/School/COMP442/COMP442_ProjectRepo/Resources/Grammar/table_semantic_NewChanges.csv"; // Absolute path CHANGE THIS WHEN UPLOADING, RELATE TO PROJECT DIR
    

    static HashMap<List<String>, String> ruleMap = new HashMap<>();
    static HashMap<List<String>, List<String>> firstFollowMap = new HashMap<>();

    // Output files
    static FileWriter derivationFile;
    static FileWriter errorSyntaxFile;
    static FileWriter outastFile;
    static BufferedWriter bwDerivation;
    static BufferedWriter bwSyntaxError;
    static BufferedWriter bwoutast;
    static String syntaxDerivationPath; 
    static String syntaxErrorPath;
    static String outastPath; 


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
    static TokenType leafStore;

    // Symbol table creator 
    static SymbolTableCreationVisitor creationVisitor = null;
    static ComputeMemberSizeVisitor memoryVisitor = null;
    static TypeCheckingVisitor typeCheckVisitor = null;
    static CodeGenerationVisitor codeGenerationVisitor = null;
    static Stack<AST> ASTNodeTypeStack = new Stack<>();

    // TODO: Create list for epsilon transitions
    static String[] semanticTypes = {
        "_prog_", "_plusop_", "_leaf_", "_minusop_", "_orop_" , "_epsilon_",
        "_expr_", "_term_", "_assign_", "_relexpr_", "_mulop_",
        "_funclist_", "_inheritslist_", "_memberlist_", "_dot_", "_indicelist_",
        "_aparamslist_", "_intnum_", "_floatnum_", "_arithexpr_", "_notfactor_", "_signfactor_", "_fparamslist_",
        "_vardecl_", "_fparams_", "_fparamstaillist_", "_statorvardecllist_", "_paramlist_",
        "_funcdecl_", "_multop_", "_divop_", "_andop_", "_relopeq_", "_relopnoteq_", "_reloplt_", "_relopgt_",
        "_relopleq_", "_relopgeq_", "_statement_", "_voidtype_", "_addop_", "_factor_", "_plussign_", "_minussign_",
        "_statblocklist_", "_ifstatement_", "_whilestatement_", "_readstatement_", "_writestatement_",
        "_returnstatement_", "_structdecl_", "_impldefdecl_", "_funcdefdecl_", "_integertype_", "_floattype_", 
        "_idtype_", "_arraysize_", "_publicvisibility_", "_privatevisibility_", "_assignstatement_", "_memberfunc_", 
        "_memberdecl_", "_emptyarray_", "_funccall_"
    };
    static List<String> semanticTypesList;

    static String[] semanticListTypes = {
        "_indicelist_", "_fparamslist_", "_fparamstaillist_", "_statorvardecllist_",
        "_paramlist_", "_funclist_", "_aparamslist_", "_statblocklist_", "_inheritslist_", "_memberlist_",
        "_arraysize_", "_factor_", "_term_", "_arithexpr_", "_expr_", "_statement_", 
        "_structdecl_", "_impldefdecl_", "_funcdefdecl_", "_memberfunc_", "_memberdecl_",
        "_fparams_", "_readstatement_", 
        // "_assignstatement_",
        // "_funccall_"
        // "_addop_"
    };
    static List<String> semanticListTypesList; // For epsilon

    static String[] semanticLeafNodes = {
        "_plusop_", "_minusop_", "_orop_", "_leaf_", "_assign_", "_intnum_", "_floatnum_", "_mulop_", "_divop_",
        "_andop_", "_relopeq_", "_relopnoteq_", "_reloplt_", "_relopgt_", "_relopleq_", "_relopgeq_", "_voidtype_",
        "_plussign_", "_minussign_", "_integertype_", "_floattype_", "_idtype_", "_publicvisibility_", "_privatevisibility_",
        
    };
    static List<String> semanticLeafNodeList;

    static HashMap<String, String> renamingMap;


    // returnstatement = returnstat or getstat????
    // writestatment = putstat
    static String[] semanticPopCount1 = {
        "_writestatement_",
        "_returnstatement_",
        "_signfactor_",
        "_notfactor_",
        "_prog_",
        "_funccall_",
        // "_addop_",
        // "_multop_",
    };
    static List<String> semanticPopCount1List;

    // readstatement = assignstat
    // _fparams_ = fcall
    static String[] semanticPopCount2 = {
        "_dot_",
        // "_readstatement_",
        "_whilestatement_",
        // "_addop_",
    };
    static List<String> semanticPopCount2List;

    // ifstatement = ifstat
    static String[] semanticPopCount3 = {
        "_funcdecl_",
        "_vardecl_",
        "_ifstatement_",
        "_relexpr_",
        "_addop_",
        "_multop_",
        "_assignstatement_",
        // "_fparams_"
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
                String rowNonTerminal = currentNonTerminalRow.get(0);
                String colTerminal = terminals.get(j);
                String rowColLanguage = currentNonTerminalRow.get(j);
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
        // System.out.println(derivationString); // TODO: Uncomment to show derivations!
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
            }

            // Check if top of stack is a terminal (x element of T)
            else if (terminalSymbolsList.contains(topStack)) {
                // If terminal matches the top stack symbol (x == a)
                if (currentToken.getType().equals(topStack)) {
                    leafStore = currentToken; // Store this to add node when _leaf_ is called
                    parseStack.pop();
                    try {
                        currentToken = lexer.getNextToken();
                    }
                    catch (Exception e) {
                        throw e;
                    }
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

            else if (semanticTypesList.contains(topStack)) {
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
                // AST childNode = semanticStack.pop();
                // TODO: uncomment to perform bottom up semantic table generation
                AST childNode = returnExactAstNode(semanticStack.pop());
                currNode.addChild(childNode);
            }
            semanticStack.push(currNode);
            parserTermination = true; // Terminate parsing!
            parseStack.pop(); // Remove the semantic attribute from the parse stack
        }
        
        else if (semanticType.equals("_emptyarray_")) {
            String emptyArrayFlag = renamingMap.get("_emptyarray_");
            TokenType emptyFlagToken = new TokenType();
            emptyFlagToken.setAll(emptyArrayFlag, emptyArrayFlag, -1);
            currNode.setASTToken(emptyFlagToken);
            semanticStack.push(currNode);
            parseStack.pop(); // Remove the semantic attribute from the parse stack
        }
    }


    // Pop X wanted times for trees with a given X number of children
    static void popWantedTimes(String parentType, int popCount, AST subtreeNode) {
        String temp = parentType;
        parentType = renamingMap.get(parentType);
        if (parentType == null) {
            parentType = temp;
        }
        subtreeNode.setASTToken(parentType, parentType, -1);

        // Use this for making the symbol tables
        List<AST> nodeList = new ArrayList<>();

        for (int i = 0; i < popCount; i++) {
            // AST childNode = semanticStack.pop();
            // TODO: uncomment to perform bottom up semantic table generation
            AST childNode = returnExactAstNode(semanticStack.pop()); 
            subtreeNode.addChild(childNode);
            nodeList.add(childNode);
        }

        semanticStack.push(subtreeNode);
    }

    // Pop for a list without knowing the size
    static void popUntilEpsilon(String listType, AST subtreeNode) {
        String temp = listType;
        listType = renamingMap.get(listType);
        if (listType == null) {
            listType = temp;
        }
        subtreeNode.setASTToken(listType, listType, -1);

        // Use this for making the symbol tables
        List<AST> nodeList = new ArrayList<>();

        while (!semanticStack.peek().getToken().getType().equals("epsilon")) {
            // AST leafNode = semanticStack.pop();
            // TODO: uncomment to perform bottom up semantic table generation
            AST leafNode = returnExactAstNode(semanticStack.pop());
            subtreeNode.addChild(leafNode);
            nodeList.add(leafNode);
        }

        // Remove epsilon flag
        semanticStack.pop();

        // Add to stack
        semanticStack.push(subtreeNode);
    }

    static List<AST> reverseChildren(List<AST> list) {
        Stack<AST> stack = new Stack<>();
        List<AST> reversedList = new ArrayList<>();
        for (AST node : list) {
            stack.push(node);
        }
        while (!stack.isEmpty()) {
            reversedList.add(stack.pop());
        }

        return reversedList;
    } 


    static AST returnExactAstNode(AST preNode) {
        // Get the node type
        TokenType nodeToken = preNode.getToken();
        String nodeType = renamingMap.get(nodeToken.getType());
        if (nodeType == null) {
            nodeType = nodeToken.getType();
        }

        nodeType = nodeType.toLowerCase();
        List<AST> children = preNode.getChildren();
        // TODO: Reverse children order!
        children  = reverseChildren(children);

        switch(nodeType) {
            case "prog":
                // String child1 = renamingMap.get(children.get(0).getToken().getType());
                return new ASTNodeProg(nodeToken, children);
            case "plus":
                // return new ASTNodePlusOp(nodeToken, children.get(0), children.get(1));
                return new ASTNodePlusOp(nodeToken);

            case "minus":
                // return new ASTNodeMinusOp(nodeToken, children.get(0), children.get(1));
                return new ASTNodeMinusOp(nodeToken);
         
            case "or":
                // return new ASTNodeOrOp(nodeToken, children.get(0), children.get(1));
                return new ASTNodeOrOp(nodeToken);
         
            case "mult":
                // return new ASTNodeMulOp(nodeToken, children.get(0), children.get(1));
                return new ASTNodeMulOp(nodeToken);
         
            case "expr":
                return new ASTNodeExpr(nodeToken, children);
         
            case "term":
                return new ASTNodeTerm(nodeToken, children);
         
            case "dimlist":
                return new ASTNodeDimList(nodeToken, children);
            
            case "emptyarray":
                return new ASTNodeEmptyArray(nodeToken);

            case "assign":
                return new ASTNodeAssign(nodeToken);
         
            case "relexpr":
                return new ASTNodeRelExpr(nodeToken, children);
         
            case "funcdeflist":
                return new ASTNodeFuncList(nodeToken, children);
         
            case "inherlist":
                return new ASTNodeInherList(nodeToken, children);
         
            case "memblist":
                return new ASTNodeMembList(nodeToken, children);
         
            case "dot":
                // return new ASTNodeDot(nodeToken, children.get(0), children.get(1));
                return new ASTNodeDot(nodeToken, children);
         
            case "indexlist":
                return new ASTNodeIndexList(nodeToken, children);
         
            case "aparams":
                return new ASTNodeAParams(nodeToken, children);
         
            case "intnum":
                return new ASTNodeIntNum(nodeToken);
         
            case "floatnum":
                return new ASTNodeFloatNum(nodeToken);
         
            case "arithexpr":
                return new ASTNodeArithExpr(nodeToken, children);
         
            case "not":
                // return new ASTNodeNotFactor(nodeToken, children.get(0));
                return new ASTNodeNotFactor(nodeToken, children);
         
            case "sign":
                // return new ASTNodeSignFactor(nodeToken, children.get(0));
                return new ASTNodeSignFactor(nodeToken, children);
         
            case "vardecl":
                return new ASTNodeVarDecl(nodeToken, children);
         
            case "fparam":
                return new ASTNodeFParam(nodeToken, children);
         
            case "statorvardecl":
                return new ASTNodeStatOrVarDecl(nodeToken, children);
         
            case "fparamlist":
                return new ASTNodeFParamList(nodeToken, children);
         
            case "funcdef":
                return new ASTNodeFuncDef(nodeToken, children);
            
            case "funccall":
                return new ASTNodeFuncCall(nodeToken, children);
         
            case "div":
                // return new ASTNodeDivOp(nodeToken, children.get(0), children.get(1));
                return new ASTNodeDivOp(nodeToken);
         
            case "and":
                // return new ASTNodeAndOp(nodeToken, children.get(0), children.get(1));
                return new ASTNodeAndOp(nodeToken);
                
            case "eq":
                return new ASTNodeRelopEq(nodeToken);
         
            case "noteq":
                return new ASTNodeRelopNotEq(nodeToken);
         
            case "lt":
                return new ASTNodeRelopLt(nodeToken);
         
            case "gt":
                return new ASTNodeRelopGt(nodeToken);
         
            case "leq":
                return new ASTNodeRelopLeq(nodeToken);
         
            case "geq":
                return new ASTNodeRelopGeq(nodeToken);
         
            case "stat":
                return new ASTNodeStatement(nodeToken, children);
         
            case "void":
                return new ASTNodeVoidType(nodeToken);
                
            // THIS IS THE OPERATIOON: y + x, NOT THE OPERAND (+)
            case "addop":
                // return new ASTNodeAddOp(nodeToken, children.get(0), children.get(1));
                return new ASTNodeAddOp(nodeToken, children);
         
            // THIS IS THE OPERATIOON: y * x, NOT THE OPERAND (*)
            case "multop":
                // return new ASTNodeMultOp(nodeToken, children.get(0), children.get(1));
                return new ASTNodeMultOp(nodeToken, children);

            case "factor":
                // return new ASTNodeFactor(nodeToken, children.get(0));
                return new ASTNodeFactor(nodeToken, children);
         
            case "plussign":
                return new ASTNodePlusSign(nodeToken);
         
            case "minussign":
                return new ASTNodeMinusSign(nodeToken);
         
            case "statblock":
                return new ASTNodeStatBlockList(nodeToken, children);
         
            case "ifstat":
                return new ASTNodeIfStatement(nodeToken, children);

            case "assignstat":
                // return new ASTNodeAssignStatement(nodeToken, children.get(0), children.get(1));
                return new ASTNodeAssignStatement(nodeToken, children);
         
            case "whilestat":
                // return new ASTNodeWhileStatement(nodeToken, children.get(0), children.get(1));
                return new ASTNodeWhileStatement(nodeToken, children);
         
            case "readstat":
                // return new ASTNodeReadStatement(nodeToken, children.get(0), children.get(1));
                return new ASTNodeReadStatement(nodeToken, children);
         
            case "writestat":
                // return new ASTNodeWriteStatement(nodeToken, children.get(0));
                return new ASTNodeWriteStatement(nodeToken, children);
         
            case "returnstat":
                // return new ASTNodeReturnStatement(nodeToken, children.get(0));
                return new ASTNodeReturnStatement(nodeToken, children);
         
            case "structdecl":
                return new ASTNodeStructDecl(nodeToken, children);
         
            case "impldecl":
                return new ASTNodeImplDefDecl(nodeToken, children);
         
            case "funcdefdecl":
                return new ASTNodeFuncDefDecl(nodeToken, children);
         
            case "id":
                return new ASTNodeId(nodeToken);
         
            case "integer":
                return new ASTNodeIntegerType(nodeToken);
         
            case "float":
                return new ASTNodeFloatType(nodeToken);
         
            case "public":
                return new ASTNodePublicVisibility(nodeToken);
         
            case "private":
                return new ASTNodePrivateVisibility(nodeToken);
            case "memberdecl":
                return new ASTNodeMemberDecl(nodeToken, children);
            case "memberfunc":
                return new ASTNodeMemberFunc(nodeToken, children);
            default:
                nodeToken.printAll();
                return null;
        }
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

            // TODO: Uncomment to write derviations from Assignment2
            writeParserOutputFiles(errorMessage, false); // Write to error file
        }

        // Error handling
        if (currentToken.getType().equals("EOF") || followSet.contains(currentToken.getType())) {
            parseStack.pop();
        }
        else {
            while (((!firstSet.contains(currentToken.getType()) || (firstSet.contains(LAMBDATRANSITION) && !followSet.contains(currentToken.getType()))) && !lexer.isEndOfFile())) {
                currentToken = lexer.getNextToken();
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
        // TODO: Move to method or Uncomment when writing derivations and errors
        bwDerivation.close();
        bwSyntaxError.close();
        
        
        bwoutast.close();
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

    // Used to print the contents from the AST
    static public void printDfsOutput() throws Exception {
        List<AST> currChildren = new ArrayList<>();
        Stack<AST> stack = new Stack<>();
        Stack<Integer> depthStack = new Stack<>();
        AST root = ast;
        AST curr = new AST();
        int currDepth = 0;

        // Skip first ROOT node and move directly to Prog
        root = root.getChildren().get(0);

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
            
            String renamedType = renamingMap.get(nodeType);
            if (renamedType == null) {
                renamedType = nodeType;
            }
            outputLine += renamedType;

            // Writing to output file
            System.out.println(outputLine);
            // TODO: Uncomment to write out
            bwoutast.write(outputLine);
            bwoutast.newLine();
        }
    }






    // Add key value mapping for renaming of semantic attributes
    static public void makeAttributeMap() {
        renamingMap.put("_prog_", "Prog");
        renamingMap.put("Prog", "Prog");
        renamingMap.put("_plusop_", "Plus");
        renamingMap.put("_leaf_", "Id");
        renamingMap.put("_minusop_", "Minus");
        renamingMap.put("_orop_", "Or");
        renamingMap.put("_expr_", "Expr");
        renamingMap.put("_term_", "Term");
        renamingMap.put("_arraysize_", "DimList");
        renamingMap.put("_assign_", "Assign");
        renamingMap.put("_relexpr_", "RelExpr");
        renamingMap.put("_mulop_", "Mult");
        renamingMap.put("_funclist_", "FuncDefList");
        renamingMap.put("_inheritslist_", "InherList");
        renamingMap.put("_memberlist_", "MembList");
        renamingMap.put("_dot_", "Dot");
        renamingMap.put("_indicelist_", "IndexList");
        renamingMap.put("_aparamslist_", "aParams");
        renamingMap.put("_intnum_", "IntNum");
        renamingMap.put("_floatnum_", "FloatNum");
        renamingMap.put("_arithexpr_", "ArithExpr");
        renamingMap.put("_notfactor_", "Not");
        renamingMap.put("_signfactor_", "Sign");
        renamingMap.put("_fparamslist_", "FParamList");
        renamingMap.put("_vardecl_", "VarDecl");
        renamingMap.put("_fparams_", "FParam");
        renamingMap.put("_fparamstaillist_", "DimList");
        renamingMap.put("_statorvardecllist_", "StatOrVarDecl");
        renamingMap.put("_paramlist_", "FParamList");
        renamingMap.put("_funcdecl_", "FuncDef");
        renamingMap.put("_multop_", "MultOp");
        renamingMap.put("_divop_", "Div");
        renamingMap.put("_andop_", "And");
        renamingMap.put("_relopeq_", "Eq");
        renamingMap.put("_relopnoteq_", "Noteq");
        renamingMap.put("_reloplt_", "Lt");
        renamingMap.put("_relopgt_", "Gt");
        renamingMap.put("_relopleq_", "Leq");
        renamingMap.put("_relopgeq_", "Geq");
        renamingMap.put("_statement_", "Stat");
        renamingMap.put("_voidtype_", "Void");
        renamingMap.put("_addop_", "AddOp");
        renamingMap.put("_factor_", "Factor");
        renamingMap.put("_plussign_", "PlusSign");
        renamingMap.put("_minussign_", "MinusSign");
        renamingMap.put("_statblocklist_", "StatBlock");
        renamingMap.put("_ifstatement_", "IfStat");
        renamingMap.put("_whilestatement_", "WhileStat");
        renamingMap.put("_readstatement_", "ReadStat");
        renamingMap.put("_writestatement_", "WriteStat");
        renamingMap.put("_returnstatement_", "ReturnStat");
        renamingMap.put("_assignstatement_", "AssignStat");
        renamingMap.put("_structdecl_", "StructDecl");
        renamingMap.put("_impldefdecl_", "ImplDecl");
        renamingMap.put("_funcdefdecl_", "FuncDefDecl");
        renamingMap.put("_integertype_", "Integer");
        renamingMap.put("_floattype_", "Float");
        renamingMap.put("_idtype_", "Id");
        renamingMap.put("_publicvisibility_", "Public");
        renamingMap.put("_privatevisibility_", "Private");
        renamingMap.put("_memberfunc_", "MemberFunc");
        renamingMap.put("_memberdecl_", "MemberDecl");
        renamingMap.put("_emptyarray_", "EmptyArray");
        renamingMap.put("_funccall_", "FuncCall");
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
        renamingMap = new HashMap<>();
        makeAttributeMap();

        //----------------------Rule file reading----------------------
        // Create token files
        Lexer lexerWriter = new Lexer(args[0]);
        lexerWriter.writeTokenFiles();
        // Test read src file
        lexer = new Lexer(args[0]);
        // Generate rule dictionary map
        readRules();
        // Generate first follow sets dictionary map
        readFirstFollowSets();


        //---------------------File path initialization------------
        // Delete old files and create new ones
        syntaxDerivationPath = lexer.getFilePathWithoutName() + "/" + lexer.getFileName() + ".outderivation";
        syntaxErrorPath = lexer.getFilePathWithoutName() + "/" + lexer.getFileName() + ".outsyntaxerrors";
        outastPath = lexer.getFilePathWithoutName() + "/" + lexer.getFileName() + ".outast";
        String outSymbolTablePath = lexer.getFilePathWithoutName() + "/" + lexer.getFileName() + ".outsymboltables";
        String outTypeCheckPath = lexer.getFilePathWithoutName() + "/" + lexer.getFileName() + ".outsemanticerrors";
        String outMemorySymbolTablePath = lexer.getFilePathWithoutName() + "/" + lexer.getFileName() + ".outmemorysymboltables";
        String outCodeGeneratorPath = lexer.getFilePathWithoutName() + "/" + lexer.getFileName() + ".moon";
        //-----------------------------------------------------------

        //---------------------File Writer initialization------------
        // // TODO: Move this file writing to a different method to display AST
        // Delete files before making new ones;
        // Create outast file
        File myFile = new File(outastPath);
        if (myFile.exists() && myFile.isFile()) {
            myFile.delete();
        }
        outastFile = new FileWriter(outastPath, true);
        bwoutast = new BufferedWriter(outastFile);
        
        // // TODO: Move this file writing to a different method to display derivations and errors
        // // Create syntax derivation file
        myFile = new File(syntaxDerivationPath);
        if (myFile.exists() && myFile.isFile()) {
            myFile.delete();
        }
        // Create syntax derivation error file
        myFile = new File(syntaxErrorPath);
        if (myFile.exists() && myFile.isFile()) {
            myFile.delete();
        }
        // Initialize writers to files (true == append to file!)
        derivationFile = new FileWriter(syntaxDerivationPath, true);
        errorSyntaxFile = new FileWriter(syntaxErrorPath, true);
        bwDerivation = new BufferedWriter(derivationFile);
        bwSyntaxError = new BufferedWriter(errorSyntaxFile);
        // //-----------------------------------------------------------
        

        // Run parser
        parse();
        System.out.println("------------End of parse--------------");

        // Print tree
        printDfsOutput();

        // Close reading and writing files
        closeReadingWritingFiles();

        // TODO: Generate Table Assn4
        creationVisitor = new SymbolTableCreationVisitor(outSymbolTablePath);
        memoryVisitor = new ComputeMemberSizeVisitor(outMemorySymbolTablePath);
        typeCheckVisitor = new TypeCheckingVisitor(outTypeCheckPath);
        codeGenerationVisitor = new CodeGenerationVisitor(outCodeGeneratorPath);
        AST root = returnExactAstNode(ast.getChildren().get(0)); // skip ROOT token to get PROG

        root.accept(creationVisitor); // Symbol Table creation
        root.accept(memoryVisitor); // Assign memory to symbol table contents
        root.accept(typeCheckVisitor); // Semantic type checking
        root.accept(codeGenerationVisitor); // Generate moon code 

    }
}
