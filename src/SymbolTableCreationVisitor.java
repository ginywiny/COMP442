import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

public class SymbolTableCreationVisitor implements Visitor{

    private String m_outputfilename;
    Integer varCounter = 0;
    public SymbolTableCreationVisitor(String path) {
        this.m_outputfilename = path;
    }

    public String getNewTempVarName(){
    	varCounter++;
    	return "t" + varCounter.toString();  
    }

    @Override
    public void visit(ASTNodeProg p_node) throws Exception {
        System.out.println("PROG");
        p_node.m_symtab = new SymbolTable(0,"global", null);
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren() ) {
			//make all children use this scopes' symbol table
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
		}
		if (!this.m_outputfilename.isEmpty()) {
			File file = new File(this.m_outputfilename);
			try (PrintWriter out = new PrintWriter(file)){ 
			    out.println(p_node.m_symtab);
			}
			catch(Exception e){
				e.printStackTrace();}
		}
    }

    @Override
    public void visit(AST p_node) throws Exception {
    // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeId p_node)  throws Exception {
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        System.out.println("Node id");
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
        p_node.m_moonVarName = p_node.getToken().getValue();
    }

    @Override
    public void visit(ASTNodeType p_node)  throws Exception {
        System.out.println("Not used");
    }

    @Override
    public void visit(ASTNodePlusOp p_node)  throws Exception {
        System.out.println("Plus op");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

    }

    @Override
    public void visit(ASTNodeMinusOp p_node)  throws Exception {
        System.out.println("Minus op");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

    }

    @Override
    public void visit(ASTNodeOrOp p_node)  throws Exception {
        System.out.println("Or Op");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

    }

    @Override
    public void visit(ASTNodeMulOp p_node)  throws Exception {
        System.out.println("Mul op");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

    }

    @Override
    public void visit(ASTNodeExpr p_node)  throws Exception {
        System.out.println("Expr");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeTerm p_node)  throws Exception {
        System.out.println("Term");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeDimList p_node)  throws Exception {
        System.out.println("Dim list");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeEmptyArray p_node)  throws Exception {
        System.out.println("Empty Array");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeAssign p_node) throws Exception{
        System.out.println("Assign");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeRelExpr p_node)  throws Exception {
        System.out.println("Relexpr");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

    }

    @Override
    public void visit(ASTNodeFuncList p_node)  throws Exception {
        System.out.println("Func list");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeInherList p_node)  throws Exception {
        System.out.println("Inherit List");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeMembList p_node)  throws Exception {
        System.out.println("Member list");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeDot p_node)  throws Exception {
        System.out.println("Dot");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeIndexList p_node)  throws Exception {
        System.out.println("Index list");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeAParams p_node)  throws Exception {
        System.out.println("AParams");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeIntNum p_node) throws Exception{
        System.out.println("Int num");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
		}

        AST nodeImpl = p_node;
        boolean isImpl = false;
        String methodName = "";
        while (nodeImpl.getParent() != null) {
            if (nodeImpl.getToken().getValue().equals("MemberFunc")) {
                methodName = nodeImpl.getChildren().get(0).getToken().getValue();
            }
            if (nodeImpl.getToken().getValue().equals("ImplDecl")) {
                isImpl = true;
                break;
            }
            nodeImpl = nodeImpl.getParent();
        }

        // If not in IMPL
        if (!isImpl) {
            String tempvarname = this.getNewTempVarName();
            p_node.m_moonVarName = tempvarname;
            String vartype = "integer";
            p_node.m_symtabentry = new VariableTableEntry("litval", vartype, tempvarname, new Vector<Integer>());
            p_node.m_symtab.addEntry(p_node.m_symtabentry);
        }
        // If in IMPL
        else {
            String structName = nodeImpl.getChildren().get(0).getToken().getValue();
            AST progNode = nodeImpl.getParent();
            Stack<AST> stack = new Stack<>();
            AST curr = new AST();
            stack.push(progNode);
            
            while (!stack.isEmpty()) {
                curr = stack.pop();
                if (curr.getToken().getValue().equals("StructDecl") 
                && curr.getChildren().get(0).getToken().getValue().equals(structName)) {
                    String tempvarname = this.getNewTempVarName();
                    p_node.m_moonVarName = tempvarname;
                    String vartype = "integer";
                    p_node.m_symtabentry = new VariableTableEntry("litval", vartype, tempvarname, new Vector<Integer>());

                    if (methodName.length() > 0) {
                        SymbolTableEntry classTable = curr.m_symtabentry;
                        for (SymbolTableEntry entry : classTable.m_subtable.m_symlist) {
                            if (entry.m_kind.equals("function") && entry.m_name.equals(methodName)) {
                                entry.m_subtable.addEntry(p_node.m_symtabentry);
                            }
                        }
                    }
                    else {
                        curr.m_symtab.addEntry(p_node.m_symtabentry);
                    }
                }

                for (AST node : curr.getChildren()) {
                    stack.push(node);
                }
            }
        }
    }

    @Override
    public void visit(ASTNodeFloatNum p_node) throws Exception {
        System.out.println("Float num");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
		}

        AST nodeImpl = p_node;
        boolean isImpl = false;
        String methodName = "";
        while (nodeImpl.getParent() != null) {
            if (nodeImpl.getToken().getValue().equals("MemberFunc")) {
                methodName = nodeImpl.getChildren().get(0).getToken().getValue();
            }
            if (nodeImpl.getToken().getValue().equals("ImplDecl")) {
                isImpl = true;
                break;
            }
            nodeImpl = nodeImpl.getParent();
        }

        // If not in IMPL
        if (!isImpl) {
            String tempvarname = this.getNewTempVarName();
            p_node.m_moonVarName = tempvarname;
            String vartype = "float";
            p_node.m_symtabentry = new VariableTableEntry("litval", vartype, tempvarname, new Vector<Integer>());
            p_node.m_symtab.addEntry(p_node.m_symtabentry);
        }
        // If in IMPL
        else {
            String structName = nodeImpl.getChildren().get(0).getToken().getValue();
            AST progNode = nodeImpl.getParent();
            Stack<AST> stack = new Stack<>();
            AST curr = new AST();
            stack.push(progNode);
            
            while (!stack.isEmpty()) {
                curr = stack.pop();
                if (curr.getToken().getValue().equals("StructDecl") 
                && curr.getChildren().get(0).getToken().getValue().equals(structName)) {
                    String tempvarname = this.getNewTempVarName();
                    p_node.m_moonVarName = tempvarname;
                    String vartype = "float";
                    p_node.m_symtabentry = new VariableTableEntry("litval", vartype, tempvarname, new Vector<Integer>());

                    if (methodName.length() > 0) {
                        SymbolTableEntry classTable = curr.m_symtabentry;
                        for (SymbolTableEntry entry : classTable.m_subtable.m_symlist) {
                            if (entry.m_kind.equals("function") && entry.m_name.equals(methodName)) {
                                entry.m_subtable.addEntry(p_node.m_symtabentry);
                            }
                        }
                    }
                    else {
                        curr.m_symtab.addEntry(p_node.m_symtabentry);
                    }
                }

                for (AST node : curr.getChildren()) {
                    stack.push(node);
                }
            }

        }
    }

    @Override
    public void visit(ASTNodeInt p_node)  throws Exception {
        System.out.println("Int type");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFloat p_node)  throws Exception {
        System.out.println("Float type");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeArithExpr p_node)  throws Exception {
        System.out.println("Arith expr");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeNotFactor p_node)  throws Exception {
        System.out.println("Not factor");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeSignFactor p_node)  throws Exception {
        System.out.println("Sign factor");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFParamsList p_node)  throws Exception {
        System.out.println("F Params");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFuncCall p_node) throws Exception {
        System.out.println("Func call");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeVarDecl p_node) throws Exception {
        System.out.println("Var");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren() ) {
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
		}

        // Cheeck if we are in impl
        AST isImplParent = new AST();
        isImplParent = p_node;
        Boolean writeFlag = true;
        while (isImplParent != null) {
            if (isImplParent.getToken().getValue().equals("ImplDecl")) {
                writeFlag = false;
                break;
            }
            isImplParent = isImplParent.getParent();
        }

        if (!p_node.getParent().getToken().getType().equals("MemberDecl") && writeFlag) {
            // Then, do the processing of this nodes' visitor
            // aggregate information from the subtree
            // get the type from the first child node and aggregate here 
            String vartype = p_node.getChildren().get(1).getToken().getValue();
            // get the id from the second child node and aggregate here
            String varid = p_node.getChildren().get(0).getToken().getValue();
            // loop over the list of dimension nodes and aggregate here 
            Vector<Integer> dimlist = new Vector<Integer>();
            for (AST dim : p_node.getChildren().get(2).getChildren()){
                // parameter dimension
                Integer dimval = Integer.parseInt(dim.getToken().getValue()); 
                dimlist.add(dimval); 
            }

            // create the symbol table entry for this variable
            // it will be picked-up by another node above later
            p_node.m_symtabentry = new VariableTableEntry("local", vartype, varid, dimlist);
            p_node.m_symtab.varTableList.add(p_node.m_symtabentry);
            p_node.m_symtab.addEntry(p_node.m_symtabentry);
        }
        // if in impldecl
        else if (!writeFlag) {
            // Then, do the processing of this nodes' visitor
            // aggregate information from the subtree
            // get the type from the first child node and aggregate here 
            String vartype = p_node.getChildren().get(1).getToken().getValue();
            // get the id from the second child node and aggregate here
            String varid = p_node.getChildren().get(0).getToken().getValue();
            // loop over the list of dimension nodes and aggregate here 
            Vector<Integer> dimlist = new Vector<Integer>();
            for (AST dim : p_node.getChildren().get(2).getChildren()){
                // parameter dimension
                Integer dimval = Integer.parseInt(dim.getToken().getValue()); 
                dimlist.add(dimval); 
            }

            // Set root to impl start
            AST rootImplName = new AST();
            rootImplName = p_node;
            while (rootImplName != null) {
                if (rootImplName.getToken().getValue().equals("ImplDecl")) {
                    break;
                }
                rootImplName = rootImplName.getParent();
            }

            // Get table name
            String implName = rootImplName.getChildren().get(0).getToken().getValue();
            p_node.m_symtab.lookupName(implName);

            // Perform DFS to find the table to add to
            Stack<AST> stack = new Stack<>();
            List<AST> currChildren = new ArrayList<>();
            AST curr = new AST();
            AST progNode = rootImplName.getParent();
            stack.push(progNode);
            while (!stack.isEmpty()) {
                curr = stack.pop();
                if (curr.getToken().getValue().equals("StructDecl") && curr.getChildren().get(0).getToken().getValue().equals(implName)) {
                    VariableTableEntry variableEntry = new VariableTableEntry("local", vartype, varid, dimlist);
                    
                    // Find function containing the variable
                    AST memberFuncName = new AST();
                    memberFuncName = p_node;
                    while (memberFuncName != null) {
                        if (memberFuncName.getToken().getValue().equals("MemberFunc")) {
                            break;
                        }
                        memberFuncName = memberFuncName.getParent();
                    }
                    String funcName = memberFuncName.getChildren().get(0).getToken().getValue();

                    if (curr.getChildren().size() > 0) {
                        AST functionList = curr.getChildren().get(2);
                        for (AST funcNode : functionList.getChildren()) {
                            if (funcNode.getChildren().get(1).getToken().getValue().equals("FuncDef")
                            && funcNode.getChildren().get(1).getChildren().get(0).getToken().getValue().equals(funcName)) {
                                funcNode.getChildren().get(1).m_symtab.addEntry(variableEntry);
                                break;
                            }
                        }
                    }
                }
                currChildren = curr.getChildren();
                for (int i = 0; i < currChildren.size(); i++) {
                    stack.push(currChildren.get(i));
                }
            }
        }
    }

    @Override
    public void visit(ASTNodeFParam p_node)  throws Exception {
        System.out.println("F Param");

        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

        // Cheeck if we are in impl
        AST isImplParent = new AST();
        isImplParent = p_node;
        Boolean writeFlag = true;
        while (isImplParent != null) {
            if (isImplParent.getToken().getValue().equals("ImplDecl")) {
                writeFlag = false;
                break;
            }
            isImplParent = isImplParent.getParent();
        }

        if (writeFlag) {
            String varid = p_node.getChildren().get(0).getToken().getValue(); // Id
            String vartype = p_node.getChildren().get(1).getToken().getValue(); // Type
            Vector<Integer> dimList = new Vector<Integer>();
            AST dimListNode = p_node.getChildren().get(2);

            if (dimListNode.getChildren().size() > 0 && !dimListNode.getChildren().get(0).getToken().getValue().equals("EmptyArray")) {
                for (AST node : dimListNode.getChildren()) { // array
                    Integer dimValue = Integer.parseInt(node.getToken().getValue());
                    dimList.add(dimValue);
                }
            }


            if (dimListNode.getChildren().size() > 0 && dimListNode.getChildren().get(0).getToken().getValue().equals("EmptyArray")) {
                dimList.add(-420);
            }
            
            p_node.m_symtabentry = new VariableTableEntry("param", vartype, varid, dimList);
            p_node.m_symtab.addEntry(p_node.m_symtabentry);
            p_node.m_symtab.varTableList.add(p_node.m_symtabentry);
        }
    }

    @Override
    public void visit(ASTNodeStatOrVarDecl p_node)  throws Exception {
        System.out.println("Stat or Var Decl");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFParamList p_node)  throws Exception {
        System.out.println("FParamList");

        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFuncDef p_node)  throws Exception {
        System.out.println("Func Def");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    

    @Override
    public void visit(ASTNodeDivOp p_node)  throws Exception {
        System.out.println("Div Op");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

    }

    @Override
    public void visit(ASTNodeAndOp p_node)  throws Exception {
        System.out.println("And op");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

    }

    @Override
    public void visit(ASTNodeRelopEq p_node)  throws Exception {
        System.out.println("Relop eq");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

    }

    @Override
    public void visit(ASTNodeRelopNotEq p_node)  throws Exception {
        System.out.println("Relop noteq");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

    }

    @Override
    public void visit(ASTNodeRelopLt p_node)  throws Exception {
        System.out.println("Relop lt");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

    }

    @Override
    public void visit(ASTNodeRelopGt p_node)  throws Exception {
        System.out.println("Relop gt");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

    }

    @Override
    public void visit(ASTNodeRelopLeq p_node)  throws Exception {
        System.out.println("Relop leq");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

    }

    @Override
    public void visit(ASTNodeRelopGeq p_node)  throws Exception {
        System.out.println("Reqlop geq");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

    }

    @Override
    public void visit(ASTNodeStatement p_node)  throws Exception {
        System.out.println("Statement");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeVoidType p_node)  throws Exception {
        System.out.println("Void type");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeMultOp p_node)  throws Exception {
        System.out.println("Mult Op");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

        // TODO: Find the type to put!!!!!
        // Do this to find the node type!!
        String varType = "";
        AST statementTypeFinder = p_node;
        while (!statementTypeFinder.getParent().getToken().getValue().equals("Stat") && statementTypeFinder.getParent() != null) {
            statementTypeFinder = statementTypeFinder.getParent();
        }
        statementTypeFinder = statementTypeFinder.getParent(); // Get Stat

        Stack<AST> stack = new Stack<>();
        stack.push(statementTypeFinder);
        AST currIdNode = new AST();
        while (statementTypeFinder != null) {
            currIdNode = stack.pop();
            if (currIdNode.getToken().getType().equals("id")) {
                break;
            }

            for (AST child : currIdNode.getChildren()) {
                stack.push(child);
            }
        }

        SymbolTableEntry entry = new SymbolTableEntry();
        if (p_node.m_symtab.m_name.equals("global")) {

            AST typeFinder = new AST();
            typeFinder = p_node;
            String functionName = "";
            String structName = "";
            while (typeFinder.getParent() != null) {
                if (typeFinder.getToken().getValue().equals("MemberFunc")) {
                    functionName = typeFinder.getChildren().get(0).getToken().getValue();
                }
                else if (typeFinder.getToken().getValue().equals("ImplDecl")) {
                    structName = typeFinder.getChildren().get(0).getToken().getValue();
                }
                typeFinder = typeFinder.getParent();
            }

            SymbolTable methodTable = null;
            // Find the struct first
            for (SymbolTableEntry classEntry : p_node.m_symtab.m_symlist) {
               if (classEntry.m_name.equals(structName)) {
                   // From struct, find function
                   SymbolTable structEntries = classEntry.m_subtable;
                   for (SymbolTableEntry funcEntry : structEntries.m_symlist) {
                        if (funcEntry.m_name.equals(functionName)) {
                            entry = structEntries.lookupName(currIdNode.getToken().getValue());
                            varType = entry.m_type;
                            methodTable = funcEntry.m_subtable;
                            break;
                        }
                   }
                   break;
               }
            }

            String tempvarname = this.getNewTempVarName();
            p_node.m_moonVarName = tempvarname;
            p_node.m_symtabentry = new VariableTableEntry("tempvar", varType, p_node.m_moonVarName, p_node.m_symtab.lookupName(p_node.getChildren().get(0).m_moonVarName).m_dims);
            methodTable.addEntry(p_node.m_symtabentry);
        }

        else {
            entry = p_node.m_symtab.lookupName(currIdNode.getToken().getValue());
            varType = entry.m_type;
            String tempvarname = this.getNewTempVarName();
            p_node.m_moonVarName = tempvarname;
            // TODO: WHAT DO I DO WITH THIS?????  p_node.m_symtab.lookupName(p_node.getChildren().get(0).m_moonVarName).m_dims
            // p_node.m_symtabentry = new VariableTableEntry("tempvar", varType, p_node.m_moonVarName, p_node.m_symtab.lookupName(p_node.getChildren().get(0).m_moonVarName).m_dims);
            p_node.m_symtabentry = new VariableTableEntry("tempvar", varType, p_node.m_moonVarName, p_node.m_symtab.lookupName(p_node.getChildren().get(0).m_moonVarName).m_dims);
            p_node.m_symtab.addEntry(p_node.m_symtabentry);
        }
    }

    @Override
    public void visit(ASTNodeAddOp p_node)  throws Exception {
        System.out.println("Add op");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }

        // TODO: Find the type to put!!!!!
        // Do this to find the node type!!
        String varType = "";

        AST statementTypeFinder = p_node;
        while (!statementTypeFinder.getParent().getToken().getValue().equals("Stat") && statementTypeFinder.getParent() != null) {
            statementTypeFinder = statementTypeFinder.getParent();
        }
        statementTypeFinder = statementTypeFinder.getParent(); // Get Stat

        Stack<AST> stack = new Stack<>();
        stack.push(statementTypeFinder);
        AST currIdNode = new AST();
        while (statementTypeFinder != null) {
            currIdNode = stack.pop();
            if (currIdNode.getToken().getType().equals("id")) {
                break;
            }

            for (AST child : currIdNode.getChildren()) {
                stack.push(child);
            }
        }


        SymbolTableEntry entry = new SymbolTableEntry();
        if (p_node.m_symtab.m_name.equals("global")) {

            AST typeFinder = new AST();
            typeFinder = p_node;
            String functionName = "";
            String structName = "";
            while (typeFinder.getParent() != null) {
                if (typeFinder.getToken().getValue().equals("MemberFunc")) {
                    functionName = typeFinder.getChildren().get(0).getToken().getValue();
                }
                else if (typeFinder.getToken().getValue().equals("ImplDecl")) {
                    structName = typeFinder.getChildren().get(0).getToken().getValue();
                }
                typeFinder = typeFinder.getParent();
            }

            SymbolTable methodTable = null;
            // Find the struct first
            for (SymbolTableEntry classEntry : p_node.m_symtab.m_symlist) {
               if (classEntry.m_name.equals(structName)) {
                   // From struct, find function
                   SymbolTable structEntries = classEntry.m_subtable;
                   for (SymbolTableEntry funcEntry : structEntries.m_symlist) {
                        if (funcEntry.m_name.equals(functionName)) {
                            entry = structEntries.lookupName(currIdNode.getToken().getValue());
                            varType = entry.m_type;
                            methodTable = funcEntry.m_subtable;
                            break;
                        }
                   }
                   break;
               }
            }

            String tempvarname = this.getNewTempVarName();
            p_node.m_moonVarName = tempvarname;
            p_node.m_symtabentry = new VariableTableEntry("tempvar", varType, p_node.m_moonVarName, p_node.m_symtab.lookupName(p_node.getChildren().get(0).m_moonVarName).m_dims);
            methodTable.addEntry(p_node.m_symtabentry);
        }

        else {
            entry = p_node.m_symtab.lookupName(currIdNode.getToken().getValue());
            varType = entry.m_type;
            String tempvarname = this.getNewTempVarName();
            p_node.m_moonVarName = tempvarname;
            // TODO: WHAT DO I DO WITH THIS?????  p_node.m_symtab.lookupName(p_node.getChildren().get(0).m_moonVarName).m_dims
            // p_node.m_symtabentry = new VariableTableEntry("tempvar", varType, p_node.m_moonVarName, p_node.m_symtab.lookupName(p_node.getChildren().get(0).m_moonVarName).m_dims);
            p_node.m_symtabentry = new VariableTableEntry("tempvar", varType, p_node.m_moonVarName, p_node.m_symtab.lookupName(p_node.getChildren().get(0).m_moonVarName).m_dims);
            p_node.m_symtab.addEntry(p_node.m_symtabentry);
        }

    }

    @Override
    public void visit(ASTNodeFactor p_node)  throws Exception {
        System.out.println("Factor");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodePlusSign p_node)  throws Exception {
        System.out.println("Plus Sign");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeMinusSign p_node)  throws Exception {
        System.out.println("Minus Sign");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeStatBlockList p_node)  throws Exception {
        System.out.println("Stat Block List");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeAssignStatement p_node) throws Exception {
        System.out.println("Assign stat");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }
    
    @Override
    public void visit(ASTNodeIfStatement p_node)  throws Exception {
        System.out.println("If stat");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeWhileStatement p_node)  throws Exception {
        System.out.println("While stat");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeReadStatement p_node)  throws Exception {
        System.out.println("Read stat");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeWriteStatement p_node)  throws Exception {
        System.out.println("Write stat");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeReturnStatement p_node)  throws Exception {
        System.out.println("Return stat");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeStructDecl p_node)  throws Exception {
        System.out.println("Struct Decl");
        // Create initial table header
        String className = p_node.getChildren().get(0).getToken().getValue();

        // Get a list of the inherited structs
		Vector<String> inherList = new Vector<String>();
        AST inherListNodes = p_node.getChildren().get(1);
        for (AST nodes : inherListNodes.getChildren()) {
            inherList.add(nodes.getToken().getValue());
        }

        Vector<String> inheritIdList = new Vector<>();
        for (AST node : p_node.getChildren().get(1).getChildren()) {
            String inheritId = node.getToken().getValue();
            inheritIdList.add(inheritId);
        }

        // Create parent table for class
        SymbolTable localTable = new SymbolTable(1, className, p_node.m_symtab);
        // Create table for inherit
        InheritedTableEntry inheritEntry = new InheritedTableEntry(null, inheritIdList);
        // Create table for class and link to parent table
        p_node.m_symtabentry = new ClassTableEntry(className, localTable);

        // Add the tables
        p_node.m_symtab.addEntry(p_node.m_symtabentry);
        p_node.m_symtab = localTable;

        p_node.m_symtab.addEntry(inheritEntry);

        for (AST child : p_node.getChildren()) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeImplDefDecl p_node)  throws Exception {
        System.out.println("ImplDefDecl");
        // // SKIP VERIFYING THE CHILDREN FROM THE IMPL! WE DO NOT NEED THEM!
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFuncDefDecl p_node)  throws Exception {
        System.out.println("FuncDecl");

        // Cheeck if we are in impl
        AST isImplParent = new AST();
        isImplParent = p_node;
        Boolean writeFlag = true;
        while (isImplParent != null) {
            if (isImplParent.getToken().getValue().equals("ImplDecl")) {
                writeFlag = false;
                break;
            }
            isImplParent = isImplParent.getParent();
        }

        if (!p_node.getParent().getToken().getType().equals("MemberDecl") && writeFlag) {
            // Function return type
            String ftype = p_node.getChildren().get(2).getToken().getValue();
            
            // Function name
            String fname = p_node.getChildren().get(0).getToken().getValue(); // ID
            SymbolTable localtable = new SymbolTable(1,fname, p_node.m_symtab);

            // Step 1 for Variables: Getting the function arguments
            AST FparamListItems = p_node.getChildren().get(1);
            // Get the types for all the function params
            Vector<String> dimTypeList = new Vector<>();
            for (int i = 0; i < FparamListItems.getChildren().size(); i++) {
                AST currNode = FparamListItems.getChildren().get(i);
                String paramType = currNode.getChildren().get(1).getToken().getValue();
                currNode.getChildren().get(0).m_type = paramType;
                if (currNode.getChildren().get(2).getChildren().size() > 0 && currNode.getChildren().get(2).getChildren().get(0).getToken().getValue().equals("EmptyArray")) {
                    paramType += "[]";
                }
                
                dimTypeList.add(paramType);
            }

            // p_node.m_symtabentry = new FunctionTableEntry(ftype, fname, paramlist, localtable, dimTypeList);
            p_node.m_symtabentry = new FunctionTableEntry(ftype, fname, localtable, dimTypeList);
            p_node.m_symtab.addEntry(p_node.m_symtabentry);
            p_node.m_symtab = localtable;


            Vector<String> inheritIdList = new Vector<>();
            for (AST node : p_node.getChildren()) {
                String inheritId = node.getToken().getValue();
                inheritIdList.add(inheritId);
            }
        }
        
		// propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren() ) {
			child.m_symtab = p_node.m_symtab;
			child.accept(this);
		}
    }

    @Override
    public void visit(ASTNodeIntegerType p_node) throws Exception{
        System.out.println("Integer type");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodePublicVisibility p_node)  throws Exception {
        System.out.println("Public");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodePrivateVisibility p_node)  throws Exception {
        System.out.println("Private");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFloatType p_node)  throws Exception {
        System.out.println("Float type");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeMemberDecl p_node)  throws Exception {
        System.out.println("Member Decl");

        String visibility = p_node.getChildren().get(0).getToken().getValue();
        AST nodeType = p_node.getChildren().get(1);

        // Cheeck if we are in impl
        AST isImplParent = new AST();
        isImplParent = p_node;
        Boolean writeFlag = true;
        while (isImplParent != null) {
            if (isImplParent.getToken().getValue().equals("ImplDecl")) {
                writeFlag = false;
                break;
            }
            isImplParent = isImplParent.getParent();
        }

        // Check for var
        if (nodeType.getToken().getType().equals("VarDecl") && writeFlag) {
            String varid = nodeType.getChildren().get(0).getToken().getValue(); // Get id
            String vartype = nodeType.getChildren().get(1).getToken().getValue(); // Get type
            Vector<Integer> dimlist = new Vector<Integer>(); // Get dimensions
            AST dimListNode =  nodeType.getChildren().get(2);

            if (dimListNode.getChildren().size() > 0 && !dimListNode.getChildren().get(0).getToken().getValue().equals("EmptyArray")) {
                for (AST dim : dimListNode.getChildren()){
                    // parameter dimension
                    Integer dimval = Integer.parseInt(dim.getToken().getValue()); 
                    dimlist.add(dimval); 
                }
            }
            if (dimListNode.getChildren().size() > 0 && dimListNode.getChildren().get(0).getToken().getValue().equals("EmptyArray")) {
                dimlist.add(-420);
            }
            
		    p_node.m_symtabentry = new MemberVariableTableEntry("data", vartype, varid, dimlist, visibility);
            p_node.m_symtab.varTableList.add(p_node.m_symtabentry);
            p_node.m_symtab.addEntry(p_node.m_symtabentry);
        }

        // TODO: Check for array fparam integer[]
        else if (nodeType.getToken().getType().equals("FuncDef") && writeFlag) {

            // Function return type
            String ftype = nodeType.getChildren().get(2).getToken().getValue();
            
            // Function name
            String fname = nodeType.getChildren().get(0).getToken().getValue(); // ID
            String className = p_node.getParent().getParent().getChildren().get(0).getToken().getValue();
            String fnameLocalTable = className + "::" + fname;

            SymbolTable localtable = new SymbolTable(2,fnameLocalTable, p_node.m_symtab);


            // Step 1 for Variables: Getting the function arguments
            AST FparamListItems = nodeType.getChildren().get(1);

            // Get the types for all the function params
            Vector<String> dimTypeList = new Vector<>();
            for (int i = 0; i < FparamListItems.getChildren().size(); i++) {
                AST currNode = FparamListItems.getChildren().get(i);
                String paramType = currNode.getChildren().get(1).getToken().getValue();
                currNode.getChildren().get(0).m_type = paramType;
                dimTypeList.add(paramType);
            }


            p_node.m_symtabentry = new MemberFuncTableEntry(ftype, fname, localtable, visibility, dimTypeList);
            p_node.m_symtabentry.m_size = dimTypeList.size();
            p_node.m_symtabentry.m_params = dimTypeList;
            p_node.m_symtab.addEntry(p_node.m_symtabentry);
            p_node.m_symtab = localtable;

        }


        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeMemberFunc p_node)  throws Exception {
        System.out.println("Member Func");
        // propagate accepting the same visitor to all the children
        // this effectively achieves Depth-First AST Traversal
        for (AST child : p_node.getChildren() ) {
            child.m_symtab = p_node.m_symtab;
            child.accept(this);
        }
    }
}
