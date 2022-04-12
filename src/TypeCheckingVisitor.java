import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TypeCheckingVisitor implements Visitor {
    public String m_outputfilename = new String();
	public String m_errors         = new String();


    public TypeCheckingVisitor(String path) {
        this.m_outputfilename = path;
    }

    @Override
    public void visit(ASTNodeProg p_node) throws Exception {
    // propagate accepting the same visitor to all the children
    // this effectively achieves Depth-First AST Traversal
    for (AST child : p_node.getChildren()) {
        child.accept(this);
    }
    if (!this.m_outputfilename.isEmpty()) {
        File file = new File(this.m_outputfilename);
        try (PrintWriter out = new PrintWriter(file)){ 
            out.println(this.m_errors);
        }
        catch(Exception e){
            e.printStackTrace();}
        }
    }

    @Override
    public void visit(AST p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeId p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeType p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodePlusOp p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeMinusOp p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeOrOp p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeMulOp p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeExpr p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeTerm p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeDimList p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeEmptyArray p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeAssign p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeRelExpr p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFuncList p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFuncCall p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }

        // Get funccall name first
        String funcName = p_node.getChildren().get(0).getChildren().get(0).getToken().getValue(); // Get id
        SymbolTable globalTable = p_node.m_symtab;
        boolean funcFound = false;
        boolean isDeclared = false;

        if (!funcName.equals("main")){
            // Find global
            while (globalTable.m_uppertable != null) {
                if (globalTable.m_name.equals("global")) {
                    break;
                }
                globalTable = globalTable.m_uppertable;
            }

            AST funcParams = p_node.getChildren().get(0).getChildren().get(1);
            int lineNumber = p_node.getChildren().get(0).getChildren().get(0).getToken().getLineNumber();
            Stack<AST> stack = new Stack<>();
            List<AST> params = new ArrayList<>();
            stack.push(funcParams);

            while (!stack.isEmpty() && funcParams.getChildren().size() > 0) {
                funcParams = stack.pop();
                if (funcParams.getToken().getValue().equals("Factor")) {
                    params = funcParams.getChildren();
                }
                for (AST child : funcParams.getChildren()) {
                    stack.push(child);
                }
            }

            boolean foundButWrong = false;
            AST funcFinder = p_node;
            while (funcFinder != null) {
                if (funcFinder.getParent().getToken().getValue().equals("Prog")) {
                    break;
                }
                funcFinder = funcFinder.getParent();
            }
            String structName = funcFinder.getChildren().get(0).getToken().getValue();

            // First check for functions
            for (SymbolTableEntry entry : globalTable.m_symlist) {
                if (entry.m_kind.equals("function") 
                && entry.m_name.equals(funcName)) {
                    isDeclared = true;
                    if (params.size() == entry.m_params.size()) {
                            funcFound = true;
                            for (int i = 0; i < params.size(); i++) {
                            String type = params.get(i).getToken().getType();
                            if (!type.equals(entry.m_params.get(i))) {
                                // p_node.setType("TypeError");
                                funcFound = false;
                                // this.m_errors += "[Error] Function call with wrong type of parameters: " + funcName + "\n";
                                foundButWrong = true;
                                break;
                            }
                        }
                        if (funcFound) {
                            p_node.m_type = entry.m_type;
                            p_node.setData(funcName);
                            funcFound = true;
                            return;
                            // break;
                        }
                    }
                    else {
                        p_node.setType("TypeError");
                        this.m_errors += "[Error] Function call with wrong number of parameters: " + funcName + " line: " + lineNumber + "\n";
                    }
                }
                else if (entry.m_kind.equals("class")
                        && entry.m_name.equals(structName)) {
                    SymbolTable classTable = entry.m_subtable;
                    
                    for (SymbolTableEntry entry2: classTable.m_symlist) {
                        if (entry2.m_kind.equals("function")
                        && entry2.m_name.equals(funcName)) {
                            isDeclared = true;

                            if (entry2.m_params.size() == 0 && params.size() == 0 
                            || (entry2.m_params.size() > 0 && params.size() == entry2.m_params.size())) {
                                funcFound = true;
                                for (int i = 0; i < params.size(); i++) {
                                    String type = params.get(i).getToken().getType();
                                    if (type.equals("floatnum")) {
                                        type = "float";
                                    }
                                    else if (type.equals("intnum")) {
                                        type = "integer";
                                    }
                                    if (!type.equals(entry2.m_params.get(i))) {
                                        // p_node.setType("TypeError");
                                        funcFound = false;
                                        foundButWrong = true;
                                        // this.m_errors += "[Error] Function call with wrong type of parameters: " + funcName + " line: " + lineNumber + "\n";
                                        // return;
                                        break;
                                    }
                                }
                                if (funcFound) {
                                    p_node.m_type = entry2.m_type;
                                    p_node.setData(funcName);
                                    funcFound = true;
                                    return;
                                    // break;
                                }
                            }
                            else {
                                p_node.setType("TypeError");
                                this.m_errors += "[Error] Function call with wrong number of parameters: " + funcName + " line: " + lineNumber + "\n";
                                return;
                            }
                        }
                    }
                }
            }

            if (foundButWrong) {
                p_node.setType("TypeError");
                this.m_errors += "[Error] Function call with wrong type of parameters: " + funcName + " line: " + lineNumber + "\n";
                return;
            }

            if (!funcFound && !isDeclared) {
                p_node.setType("TypeError");
                this.m_errors += "[Error] Undeclared member function declaration: " + funcName + " line: " + lineNumber + "\n";
                return;
            }
        }
    }

    @Override
    public void visit(ASTNodeInherList p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeMembList p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeDot p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeIndexList p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeAParams p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeIntNum p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFloatNum p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeInt p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFloat p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeArithExpr p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeNotFactor p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeSignFactor p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFParamsList p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeVarDecl p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }

        AST idNode = p_node.getChildren().get(0);
        AST typeNode = p_node.getChildren().get(1);

        String id = idNode.getToken().getValue();
        String type = typeNode.getToken().getValue();

        // Get global table to find all classes
        SymbolTable getGlobalTable = p_node.m_symtab;
        while (getGlobalTable.m_uppertable != null) {
            if (getGlobalTable.m_name.equals("global")) {
                break;
            }
            getGlobalTable = getGlobalTable.m_uppertable;
        }

        AST funcImplOrStruct = p_node;
        while (funcImplOrStruct.getParent() != null) {
            if (funcImplOrStruct.getParent().getToken().getValue().equals("Prog")) {
                break;
            }
            funcImplOrStruct = funcImplOrStruct.getParent();
        }

        String parentType = funcImplOrStruct.getToken().getValue();

        // See if class exists
        if (!type.equals("integer") && !type.equals("float")) {
            boolean classExists = false;

            for (SymbolTableEntry entry : getGlobalTable.m_symlist) {
                if (entry.m_kind.equals("class") && entry.m_name.equals(type)) {
                    classExists = true;
                }
            }

            if (classExists) {
                p_node.m_type = type;
            }
            else {
                p_node.setType("TypeError");
                this.m_errors += "[Error] Undeclared variable class type: " + id + "-> " + type + " line: " + idNode.getToken().getLineNumber() + "\n";
            }
        }
    }

    @Override
    public void visit(ASTNodeFParam p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeStatOrVarDecl p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFParamList p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFuncDef p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeMultOp p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }

        AST leftNode = p_node.getChildren().get(0);
        Stack<AST> stack = new Stack<>();
        stack.push(leftNode);
        while(!stack.isEmpty()){
            leftNode = stack.pop();
            if (leftNode.getToken().getType().equals("intnum")
            || leftNode.getToken().getType().equals("floatnum")) {
                break;
            }
            for (AST child: leftNode.getChildren()) {
                stack.push(child);
            }
        }

        AST rightNode = p_node.getChildren().get(2);
        stack.clear();
        stack.push(rightNode);
        while(!stack.isEmpty()) {
            rightNode = stack.pop();
            if (rightNode.getToken().getType().equals("intnum")
            || rightNode.getToken().getType().equals("floatnum")) {
                break;
            }
            for (AST child : rightNode.getChildren()) {
                stack.push(child);
            }
        }

        if (leftNode.getToken().getType().equals(rightNode.getToken().getType())) {
            p_node.setType(leftNode.getToken().getType());
        }
        else {
            if (leftNode.getToken().getLineNumber() > -1) {
                p_node.setType("TypeError");
                this.m_errors += "[Error] Type error in MultOp expression line: " + leftNode.getToken().getLineNumber() + "\n";
            }
            else {
                p_node.setType("TypeError");
                this.m_errors += "[Error] Type error in MultOp expression line: " + rightNode.getToken().getLineNumber() + "\n";
            }
            return;
        }
    }

    @Override
    public void visit(ASTNodeDivOp p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeAndOp p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeRelopEq p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeRelopNotEq p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeRelopLt p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeRelopGt p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeRelopLeq p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeRelopGeq p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeStatement p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeAssignStatement p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }

        AST leftType = p_node.getChildren().get(0).getChildren().get(0);
        AST rightType = p_node.getChildren().get(2);
        Stack<AST> stack = new Stack<>();
        stack.push(rightType);
        while(!stack.isEmpty()) {
            rightType = stack.pop();
            if (rightType.getToken().getType().equals("id")) {
                break;
            }
            for (AST child : rightType.getChildren()) {
                stack.push(child);
            }
        }

        AST implNode = p_node;
        boolean isImpl = false;
        while (implNode.getParent() != null) {
            if (implNode.getToken().getType().equals("ImplDecl")) {
                isImpl = true;
                break;
            }
            implNode = implNode.getParent();
        }

        SymbolTable upperTable = p_node.m_symtab;
        while (upperTable.m_uppertable != null) {
            if (upperTable.m_name.equals("global")) {
                break;
            }
            upperTable = upperTable.m_uppertable;
        }

        String leftVar = leftType.getToken().getValue();
        String rightVar = rightType.getToken().getValue();
        String leftVarType = "";
        String rightVarType = "";

        if (isImpl) {
            AST funcNode = p_node;
            String funcName = "";
            while (funcNode.getParent() != null) {
                if (funcNode.getToken().getValue().equals("MemberFunc")) {
                    funcName = funcNode.getChildren().get(0).getToken().getValue();
                    break;
                }
                funcNode = funcNode.getParent();
            }

            String structName = implNode.getChildren().get(0).getToken().getValue();
            for (SymbolTableEntry entry : upperTable.m_symlist) {
                if (entry.m_kind.equals("class")
                    && entry.m_name.equals(structName)) {
                        SymbolTable subTable = entry.m_subtable;
                        for (SymbolTableEntry entry2: subTable.m_symlist) {
                            if (entry2.m_kind.equals("data") 
                                && entry2.m_name.equals(leftVar)) {
                                    leftVarType = entry2.m_type;
                            }
                            if (entry2.m_kind.equals("data") 
                                && entry2.m_name.equals(rightVar)) {
                                    rightVarType = entry2.m_type;
                            }

                            if (entry2.m_kind.equals("function")
                                && entry2.m_name.equals(funcName)) {
                                    SymbolTable funcTable = entry2.m_subtable;
                                    for (SymbolTableEntry entry3 : funcTable.m_symlist) {
                                        if ((entry3.m_kind.equals("param") || entry3.m_kind.equals("local"))
                                            && entry3.m_name.equals(leftVar)) {
                                                leftVarType = entry3.m_type;
                                        }
                                        if ((entry3.m_kind.equals("param") || entry3.m_kind.equals("local"))
                                            && entry3.m_name.equals(rightVar)) {
                                                rightVarType = entry3.m_type;
                                        }
                                    }
                                }
                        }
                    }
            }

            if (rightVarType.length() == 0) {
                float number = 0;
                boolean isNumber = false;
                try {
                    number = Float.parseFloat(rightVar);
                } catch (NumberFormatException e) {
                    System.out.println(e + " for " + structName + "::" + funcName);
                } finally {
                    isNumber = true;
                }

                if (isNumber) {
                    if (number % 1 == 0) {
                        boolean isInt = true;
                        for (char c : rightVar.toCharArray()) {
                            if (c == '.') {
                                isInt = false;
                                break;
                            }
                        }
                        if (isInt) {
                            rightVarType = "integer";
                        }
                        else {
                            rightVarType = "float";
                        }
                    }
                    else {
                        rightVarType = "float";
                    }
                }
            }

            if (leftVarType.length() == 0) {
                float number = 0;
                boolean isNumber = false;
                try {
                    number = Float.parseFloat(rightVar);
                } catch (NumberFormatException e) {
                    System.out.println(e + " for " + structName + "::" + funcName);
                } finally {
                    isNumber = true;
                }

                if (isNumber) {
                    if (number % 1 == 0) {
                        leftVarType = "integer";
                    }
                    else {
                        leftVarType = "float";
                    }
                }
            }
            
            

            if (leftVarType.equals(rightVarType)) {
                p_node.setType(leftVarType);
            }
            else {
                p_node.setType("TypeError");
                this.m_errors += "[Error] Type error in assignment statement line: " + p_node.getChildren().get(1).getToken().getLineNumber() + "\n";
                return;
            }

        }



        // Get global table to find all classes
        SymbolTable getGlobalTable = p_node.m_symtab;
        while (getGlobalTable.m_uppertable != null) {
            if (getGlobalTable.m_name.equals("global")) {
                break;
            }
            getGlobalTable = getGlobalTable.m_uppertable;
        }
        AST funcImplOrStruct = p_node;
        while (funcImplOrStruct.getParent() != null) {
            if (funcImplOrStruct.getParent().getToken().getValue().equals("Prog")) {
                break;
            }
            funcImplOrStruct = funcImplOrStruct.getParent();
        }

        String parentType = funcImplOrStruct.getToken().getValue();

        if (parentType.equals("ImplDecl")) {
            boolean varExists = false;
            boolean foundInInher = false;
            boolean foundLocally = false;
            String className = funcImplOrStruct.getChildren().get(0).getToken().getValue();
            List<String> inherList = new ArrayList();
            String id = leftVar;
            String type = leftVarType;

            AST funcNode = p_node;
            String localFuncName = "";
            
            while (funcNode.getParent() != null) {
                if (funcNode.getToken().getValue().equals("MemberFunc")) {
                    localFuncName = funcNode.getChildren().get(0).getToken().getValue();
                    break; 
                }
                funcNode = funcNode.getParent();
            }
            
            for (SymbolTableEntry entry : getGlobalTable.m_symlist) {
                if (entry.m_kind.equals("class") && entry.m_name.equals(className)) {
                    SymbolTable subTable = entry.m_subtable;
                    for (SymbolTableEntry entry2: subTable.m_symlist) {
                        if (entry2.m_kind.equals("data") && entry2.m_name.equals(id)) {
                            p_node.setData(id);
                            varExists = true;
                            return;
                        }
                        else if (entry2.m_kind.equals("Inherit")) {
                            for (String inher : entry2.m_inheritIdList) {
                                inherList.add(inher);
                            }
                        }
                        else if (entry2.m_kind.equals("function") && entry2.m_name.equals(localFuncName)) {
                            SymbolTable funcSubTable = entry2.m_subtable;
                            for (SymbolTableEntry entry3: funcSubTable.m_symlist) {
                                if (entry3.m_name.equals(id)) {
                                    varExists = true;
                                    foundLocally = true;
                                    p_node.setData(id);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            if (!varExists) {
                for (String inheritedClass : inherList) {
                    for (SymbolTableEntry entry : getGlobalTable.m_symlist) {
                        if (entry.m_kind.equals("class") && entry.m_name.equals(inheritedClass)) {
                            SymbolTable subTable = entry.m_subtable;
                            for (SymbolTableEntry entry2: subTable.m_symlist) {
                                if (entry2.m_kind.equals("data") && entry2.m_name.equals(id)) {
                                    p_node.setData(id);
                                    varExists = true;
                                    foundInInher = true;
                                    return;
                                }
                                else if (entry2.m_kind.equals("function") && entry2.m_name.equals(localFuncName)) {
                                    SymbolTable funcSubTable = entry2.m_subtable;
                                    for (SymbolTableEntry entry3: funcSubTable.m_symlist) {
                                        if (entry3.m_name.equals(id)) {
                                            varExists = true;
                                            foundLocally = true;
                                            p_node.setData(id);
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }


            if (!varExists && !foundInInher && !foundLocally) {
                p_node.setType("TypeError");
                this.m_errors += "[Error] Undeclared data member: " + id + "-> " + type + " line: " + leftType.getToken().getLineNumber() + "\n";
            }
        }
        else if (parentType.equals("FuncDefDecl")) {
            boolean varExists = false;
            String id = leftVar;
            String type = leftVarType;
            AST funcNode = p_node;
            String localFuncName = funcImplOrStruct.getChildren().get(0).getToken().getValue();
            
            for (SymbolTableEntry entry : getGlobalTable.m_symlist) {
                if (entry.m_kind.equals("function") && entry.m_name.equals(localFuncName)) {
                    SymbolTable funcSubTable = entry.m_subtable;
                    for (SymbolTableEntry entry2: funcSubTable.m_symlist) {
                        if (entry2.m_name.equals(id)) {
                            varExists = true;
                            p_node.setData(id);
                            return;
                        }
                    }
                }
            }
            if (!varExists) {
                p_node.setType("TypeError");
                this.m_errors += "[Error] Undeclared data member: " + id + "-> " + type + " line: " + leftType.getToken().getLineNumber() + "\n";
            }
        }
        
    }

    @Override
    public void visit(ASTNodeVoidType p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
        p_node.m_type = "void";
    }

    @Override
    public void visit(ASTNodeAddOp p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
        AST leftNode = p_node.getChildren().get(0);
        Stack<AST> stack = new Stack<>();
        stack.push(leftNode);
        while(!stack.isEmpty()){
            leftNode = stack.pop();
            if (leftNode.getToken().getValue().equals("Factor")) {
                break;
            }
            for (AST child: leftNode.getChildren()) {
                stack.push(child);
            }
        }

        if (leftNode.getChildren().size() > 0){
            leftNode = leftNode.getChildren().get(0);
        }

        if (!leftNode.getToken().getType().equals("id")) {
            stack.clear();
            stack.push(leftNode);
            while (!stack.isEmpty()) {
                leftNode = stack.pop();
                if (leftNode.getToken().getType().equals("id")
                    || leftNode.getToken().getType().equals("floatnum")
                    || leftNode.getToken().getType().equals("intnum")) {
                    break;
                }

                for (AST child : leftNode.getChildren()) {
                    stack.push(child);
                }
            }
        }

        String leftType = "";
        if (leftNode.getToken().getType().equals("floatnum")) {
            leftType = "float";
        }
        else if (leftNode.getToken().getType().equals("intnum")) {
            leftType = "integer";
        }

        AST rightNode = p_node.getChildren().get(2);
        stack.clear();
        stack.push(rightNode);
        while(!stack.isEmpty()) {
            rightNode = stack.pop();
            if (rightNode.getToken().getType().equals("intnum")
            || rightNode.getToken().getType().equals("floatnum")
            || rightNode.getToken().getType().equals("id")) {
                break;
            }
            for (AST child : rightNode.getChildren()) {
                stack.push(child);
            }
        }

        String rightType = "";
        if (rightNode.getToken().getType().equals("floatnum")) {
            rightType = "float";
        }
        else if (rightNode.getToken().getType().equals("intnum")) {
            rightType = "integer";
        }

        AST funcOrImpl = p_node;
        while (funcOrImpl.getParent() != null) {
            if (funcOrImpl.getParent().getToken().getValue().equals("Prog")) {
                break;
            }
            funcOrImpl = funcOrImpl.getParent();
        }

        // Get global table to find all classes
        SymbolTable getGlobalTable = p_node.m_symtab;
        while (getGlobalTable.m_uppertable != null) {
            if (getGlobalTable.m_name.equals("global")) {
                break;
            }
            getGlobalTable = getGlobalTable.m_uppertable;
        }

        String nameFuncOrImpl = "";
        String idLeft = leftNode.m_moonVarName;
        String idRight = rightNode.m_moonVarName;
        // If we are in a func
        if (funcOrImpl.getToken().getValue().equals("FuncDefDecl")) {
            nameFuncOrImpl = funcOrImpl.getChildren().get(0).getToken().getValue();
            boolean varExists = false;
            AST funcNode = p_node;
            String localFuncName = nameFuncOrImpl;
            for (SymbolTableEntry entry : getGlobalTable.m_symlist) {
                if (entry.m_kind.equals("function") && entry.m_name.equals(localFuncName)) {
                    SymbolTable funcSubTable = entry.m_subtable;
                    for (SymbolTableEntry entry2: funcSubTable.m_symlist) {
                        if (leftType.length() > 0 && rightType.length() > 0) {
                            break;
                        }
                        if (entry2.m_name.equals(idLeft)) {
                            // varExists = true;
                            leftType = entry2.m_type;
                        }
                        if (entry2.m_name.equals(idRight)) {
                            rightType = entry2.m_type;
                            // varExists = true;
                        }
                    }
                }
            }

            // if () {
            //     p_node.setType("TypeError");
            //     this.m_errors += "[Error] Undeclared data member: " + idLeft + "-> " + type + " line: " + leftNode.getToken().getLineNumber() + "\n";
            // }


            if (leftType.equals(rightType)) {
                p_node.setType(leftNode.getToken().getType());
                return;
            }
            else {
                if (leftNode.getToken().getLineNumber() > -1) {
                    p_node.setType("TypeError");
                    this.m_errors += "[Error] Type error in AddOp expression line: " + leftNode.getToken().getLineNumber() + "\n";
                }
                else {
                    p_node.setType("TypeError");
                    this.m_errors += "[Error] Type error in AddOp expression line: " + rightNode.getToken().getLineNumber() + "\n";
                }
                return;
            }
            
        }
        // If we are in an impl
        else if (funcOrImpl.getToken().getValue().equals("ImplDecl")) {
            nameFuncOrImpl = funcOrImpl.getChildren().get(0).getToken().getValue();
            List<String> inherList = new ArrayList();
            AST memberFuncNode = p_node;
            boolean varExists = false;
            while (memberFuncNode.getParent() != null) {
                if (memberFuncNode.getToken().getValue().equals("MemberFunc")) {
                    break;
                }
                memberFuncNode = memberFuncNode.getParent();
            }

            String localFuncName = memberFuncNode.getChildren().get(0).m_moonVarName;
            for (SymbolTableEntry entry : getGlobalTable.m_symlist) {
                if (entry.m_kind.equals("class") && entry.m_name.equals(nameFuncOrImpl)) {
                    SymbolTable subTable = entry.m_subtable;
                    for (SymbolTableEntry entry2: subTable.m_symlist) {
                        if (leftType.length() > 0 && rightType.length() > 0) {
                            varExists = true;
                            break;
                        }
                        if (entry2.m_name.equals(idLeft)) {
                            leftType = entry2.m_type;
                        }
                        if (entry2.m_name.equals(idRight)) {
                            rightType = entry2.m_type;
                        }
                        else if (entry2.m_kind.equals("Inherit")) {
                            for (String inher : entry2.m_inheritIdList) {
                                inherList.add(inher);
                            }
                        }



                        else if (entry2.m_kind.equals("function") && entry2.m_name.equals(localFuncName)) {
                            SymbolTable funcSubTable = entry2.m_subtable;
                            for (SymbolTableEntry entry3: funcSubTable.m_symlist) {
                                if (leftType.length() > 0 && rightType.length() > 0) {
                                    varExists = true;
                                    break;
                                }
                                if (entry3.m_name.equals(idLeft)) {
                                    leftType = entry3.m_type;
                                }
                                if (entry3.m_name.equals(idRight)) {
                                    rightType = entry3.m_type;
                                }
                            }
                        }
                    }
                }
            }


            if (!varExists) {
                for (String inheritedClass : inherList) {
                    for (SymbolTableEntry entry : getGlobalTable.m_symlist) {
                        if (entry.m_kind.equals("class") && entry.m_name.equals(inheritedClass)) {
                            SymbolTable subTable = entry.m_subtable;
                            for (SymbolTableEntry entry2: subTable.m_symlist) {
                                if (entry2.m_name.equals(idLeft)) {
                                }
                                else if (entry2.m_kind.equals("function") && entry2.m_name.equals(localFuncName)) {
                                    SymbolTable funcSubTable = entry2.m_subtable;
                                    for (SymbolTableEntry entry3: funcSubTable.m_symlist) {
                                        if (entry3.m_name.equals(idLeft)) {
                                            leftType = entry3.m_type;
                                        }
                                        if (entry3.m_name.equals(idRight)) {
                                            rightType = entry3.m_type;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }


            if (leftType.equals(rightType)) {
                p_node.setType(leftNode.getToken().getType());
                return;
            }
            else {
                if (leftNode.getToken().getLineNumber() > -1) {
                    p_node.setType("TypeError");
                    this.m_errors += "[Error] Type error in AddOp expression line: " + leftNode.getToken().getLineNumber() + "\n";
                }
                else {
                    p_node.setType("TypeError");
                    this.m_errors += "[Error] Type error in AddOp expression line: " + rightNode.getToken().getLineNumber() + "\n";
                }
                return;
            }
        }  

        // IF ALL PREVIOUS FAILS!
        if (leftNode.getToken().getType().equals(rightNode.getToken().getType())) {
            p_node.setType(leftNode.getToken().getType());
        }
        else {
            if (leftNode.getToken().getLineNumber() > -1) {
                p_node.setType("TypeError");
                this.m_errors += "[Error] Type error in AddOp expression line: " + leftNode.getToken().getLineNumber() + "\n";
            }
            else {
                p_node.setType("TypeError");
                this.m_errors += "[Error] Type error in AddOp expression line: " + rightNode.getToken().getLineNumber() + "\n";
            }
            return;
        }
    }

    @Override
    public void visit(ASTNodeFactor p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodePlusSign p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeMinusSign p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeStatBlockList p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }
    
    @Override
    public void visit(ASTNodeIfStatement p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeWhileStatement p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeReadStatement p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeWriteStatement p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeReturnStatement p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeStructDecl p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeImplDefDecl p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }


        
    }

    @Override
    public void visit(ASTNodeFuncDefDecl p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
        p_node.setType(p_node.getChildren().get(2).getToken().getValue());
        p_node.setData(p_node.getChildren().get(2).getToken().getValue());
    }

    @Override
    public void visit(ASTNodeIntegerType p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
        p_node.m_type = "integer";
    }

    @Override
    public void visit(ASTNodePublicVisibility p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodePrivateVisibility p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFloatType p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
        p_node.m_type = "float";
    }

    @Override
    public void visit(ASTNodeMemberFunc p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }

        AST implNode = p_node;
        while (implNode != null) {
            if (implNode.getToken().getValue().equals("ImplDecl")) {
                break;
            }
            implNode = implNode.getParent();
        }

        String structName = implNode.getChildren().get(0).getToken().getValue();


        String funcName = p_node.getChildren().get(0).getToken().getValue();
        SymbolTable globalTable = p_node.m_symtab;
        // Find global
        while (globalTable.m_uppertable != null) {
            if (globalTable.m_name.equals("global")) {
                break;
            }
            globalTable = globalTable.m_uppertable;
        }

        boolean isDeclared = false;
        boolean foundButWrong = false;

        boolean funcFound = false;
        // First check for functions
        if (!funcName.equals("main")){
            // Find global
            while (globalTable.m_uppertable != null) {
                if (globalTable.m_name.equals("global")) {
                    break;
                }
                globalTable = globalTable.m_uppertable;
            }

            AST funcParams = p_node.getChildren().get(1);
            List<AST> params = funcParams.getChildren();

            // First check for functions
            for (SymbolTableEntry entry : globalTable.m_symlist) {
                if (entry.m_kind.equals("class")
                && entry.m_name.equals(structName)) {
                    SymbolTable classTable = entry.m_subtable;
                    
                    for (SymbolTableEntry entry2: classTable.m_symlist) {
                        if (entry2.m_kind.equals("function")
                        && entry2.m_name.equals(funcName)) {
                            isDeclared = true;


                            if (entry2.m_params.size() == 0 && params.size() == 0 
                            || (entry2.m_params.size() > 0 && params.size() == entry2.m_params.size())) {
                                funcFound = true;
                                for (int i = 0; i < params.size(); i++) {
                                    String type = params.get(i).getChildren().get(1).getToken().getType();
                                    if (!type.equals(entry2.m_params.get(i))) {
                                        // p_node.setType("TypeError");
                                        funcFound = false;
                                        foundButWrong = true;
                                        // this.m_errors += "[Error] Function call with wrong type of parameters: " + funcName + " line: " + p_node.getChildren().get(0).getToken().getLineNumber() + "\n";
                                        // return;
                                        break;
                                    }
                                }
                                if (funcFound) {
                                    p_node.m_type = entry2.m_type;
                                    p_node.setData(funcName);
                                    funcFound = true;
                                    return;
                                    // break;
                                }
                            }
                            else {
                                p_node.setType("TypeError");
                                this.m_errors += "[Error] Function call with wrong number of parameters: " + funcName + " line: " + p_node.getChildren().get(0).getToken().getLineNumber() + "\n";
                                return;
                            }
                        }
                    }
                }
            }
            if (foundButWrong) {
                p_node.setType("TypeError");
                this.m_errors += "[Error] Function call with wrong type of parameters: " + funcName + " line: " + p_node.getChildren().get(0).getToken().getLineNumber() + "\n";
                return;
            }

            if (!funcFound && !isDeclared) {
                p_node.setType("TypeError");
                this.m_errors += "[Error] Undeclared member function declaration: " + funcName + " line: " + p_node.getChildren().get(0).getToken().getLineNumber() + "\n";
                return;
            }
        }
    }

    @Override
    public void visit(ASTNodeMemberDecl p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }
}
