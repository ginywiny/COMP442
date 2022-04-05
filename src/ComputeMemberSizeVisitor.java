import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

public class ComputeMemberSizeVisitor implements Visitor{

    private String m_outputfilename;
    Integer varCounter = 0;
    Integer globalOffsetCounter = 0;
    public ComputeMemberSizeVisitor(String path) {
        this.m_outputfilename = path;
    }

    public String getNewTempVarName(){
    	varCounter++;
    	return "t" + varCounter.toString();  
    }

    public int getStructSize(AST p_node) {
        int size = 0;
        AST progNode = p_node;
        while (progNode.getParent() != null) {
            progNode = progNode.getParent();
        }

        // Find the size of the table for a given class (eg. return QUADRATIC)
        String varType = p_node.getToken().getValue();
        Stack<AST> stack = new Stack<>();
        AST curr = new AST();
        stack.push(progNode);
        while (!stack.isEmpty()) {
            curr = stack.pop();
            // Set the size
            if (curr.getToken().getValue().equals("StructDecl")
            && curr.m_symtab.m_name.equals(varType)) {
                size = Math.abs(curr.m_symtab.m_size);
            }

            for (AST node: curr.getChildren()) {
                stack.push(node);
            }
        }
        return size;
    }


    public void updateOffset(AST p_node) {
        // Step 1: Check if we are in FUNC or in IMPL
        AST isFuncNode = p_node;
        boolean isFunc = false;
        while (isFuncNode.getParent() != null) {
            if (isFuncNode.getToken().getValue().equals("FuncDefDecl")) {
                isFunc = true;
                break;
            }
            isFuncNode = isFuncNode.getParent();
        }
        
        // If we are in IMPL NOT FUNC!
        if (!isFunc) {
            AST structFinder = p_node;
            String functionName = "";
            String className = "";

            // Find the Class and Method
            while (structFinder.getParent() != null) {
                // Get the function name
                if (structFinder.getToken().getValue().equals("MemberFunc")) {
                    functionName = structFinder.getChildren().get(0).getToken().getValue();
                }
                if (structFinder.getToken().getValue().equals("ImplDecl")) {
                    className = structFinder.getChildren().get(0).getToken().getValue();
                }
                structFinder = structFinder.getParent();
            }

            // Find the symbol table for the class + method (eg. LINEAR::evaluate)
            boolean isMethodFound = false;
            SymbolTableEntry entryToUpdate = null;
            
            // structFinder is at PROG
            for (SymbolTableEntry classEntry : structFinder.m_symtab.m_symlist) {
                if (classEntry.m_name.equals(className)) {
                    SymbolTable subTable = classEntry.m_subtable;

                    for (SymbolTableEntry functionEntry : subTable.m_symlist) {
                        if (functionEntry.m_kind.equals("function") && functionEntry.m_name.equals(functionName)) {
                            entryToUpdate = functionEntry;
                            isMethodFound = true;
                            break;
                        }
                    }
                    break;
                }
            }
            
            // Update the table by setting the size of the current variable, and then updating offsets
            if (isMethodFound) {
                SymbolTable classFuncTable = entryToUpdate.m_subtable;
                String varType = p_node.m_symtabentry.m_type; 
                int size = 0;
                if (varType.equals("integer")) {
                    size = 4;
                }
                else if (varType.equals("float")) {
                    size = 8;
                }
                else {
                    size = getStructSize(p_node);
                }

                // Update the table offsets!
                classFuncTable.m_size = -size;
                classFuncTable.m_size = 0;
                for (SymbolTableEntry entry : classFuncTable.m_symlist) {
                    entry.m_offset = classFuncTable.m_size - entry.m_size; 
                    classFuncTable.m_size -= entry.m_size;
                }
            }
        }
        else {
            AST funcFinder = p_node;
            String funcName = "";
            // Find the function and then set funcFinder to PROG
            while (funcFinder.getParent() != null) {
                // Get the function name
                if (funcFinder.getToken().getValue().equals("FuncDefDecl")) {
                    if (funcFinder.getChildren().size() > 0) {
                        funcName = funcFinder.getChildren().get(0).getToken().getValue();
                    }
                }
                funcFinder = funcFinder.getParent();
            }


            // Find the symbol table for the class + method (eg. LINEAR::evaluate)
            boolean isFuncFound = false;
            SymbolTableEntry entryFuncToUpdate = null;
            
            // funcFinder is at PROG
            for (SymbolTableEntry funcEntry : funcFinder.m_symtab.m_symlist) {
                if (funcEntry.m_kind.equals("function") && funcEntry.m_name.equals(funcName)) {
                    entryFuncToUpdate = funcEntry;
                    isFuncFound = true;
                    break;
                }
            }

            if (isFuncFound) {
                SymbolTable funcTable = entryFuncToUpdate.m_subtable;
                String varType = p_node.m_symtabentry.m_type; 
                int size = 0;
                if (varType.equals("integer")) {
                    size = 4;
                }
                else if (varType.equals("float")) {
                    size = 8;
                }
                else {
                    size = getStructSize(p_node);
                }

                // Update the table offsets!
                funcTable.m_size = -size;
                funcTable.m_size = 0;
                for (SymbolTableEntry entry : funcTable.m_symlist) {
                    entry.m_offset = funcTable.m_size - entry.m_size; 
                    funcTable.m_size -= entry.m_size;
                }
            }
            else {
                System.out.println("Func not found...");
            }
        }
    }



    public int sizeOfEntry(AST p_node) {
        int size = 0;

        //TODO: Create DFS to find the memory size for a class (ex. LINEAR)

        if(p_node.getToken().getType().equals("integer")) {
			size = 4;
        }
		else if(p_node.getToken().getType().equals("float")) {
		    size = 8;
        }
        else {
            // Get PROG
            AST progNode = p_node;
            while (progNode.getParent() != null) {
                progNode = progNode.getParent();
            }

            // Find the size of the table for a given class (eg. return QUADRATIC)
            String varType = p_node.getToken().getValue();
            Stack<AST> stack = new Stack<>();
            AST curr = new AST();
            stack.push(progNode);
            while (!stack.isEmpty()) {
                curr = stack.pop();
                // Set the size
                if (curr.getToken().getValue().equals("StructDecl")
                && curr.m_symtab.m_name.equals(varType)) {
                    size = Math.abs(curr.m_symtab.m_size);
                }

                for (AST node: curr.getChildren()) {
                    stack.push(node);
                }
            }
        }

		// if it is an array, multiply by all dimension sizes
        AST dimListNode = p_node.getParent();
        for (int i = 0; i < dimListNode.getChildren().size(); i++) {
            if (dimListNode.getChildren().get(i).getToken().getValue().equals("DimList")) {
                dimListNode = dimListNode.getChildren().get(i);
                break;
            }
        }

        // Multiply the size by the dimensions!
        AST parentNode = p_node.getParent();
		VariableTableEntry ve = (VariableTableEntry) parentNode.m_symtabentry; 
		if(ve != null && !ve.m_dims.isEmpty() && ve.m_dims.get(0) != -420) // If not EmptyArray! (-420)
			for(Integer dim : ve.m_dims)
				size *= dim;	
		return size;
    }

    public int sizeOfTypeNode(AST p_node) {
		int size = 0;
		if(p_node.getToken().getType().equals("integer"))
			size = 4;
		else if(p_node.getToken().getType().equals("float"))
			size = 8;
        else {
            // Get PROG
            AST progNode = p_node;
            while (progNode.getParent() != null) {
                progNode = progNode.getParent();
            }

            // Find the size of the table for a given class (eg. return QUADRATIC)
            String varType = p_node.getToken().getValue();
            Stack<AST> stack = new Stack<>();
            AST curr = new AST();
            stack.push(progNode);
            while (!stack.isEmpty()) {
                curr = stack.pop();
                // Set the size
                if (curr.getToken().getValue().equals("StructDecl")
                && curr.m_symtab.m_name.equals(varType)) {
                    size = Math.abs(curr.m_symtab.m_size);
                }

                for (AST node: curr.getChildren()) {
                    stack.push(node);
                }
            }
        }
		return size;
	}



    @Override
    public void visit(ASTNodeProg p_node) throws Exception {
        for (AST child : p_node.getChildren())
			child.accept(this);
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
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeId p_node)  throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeType p_node)  throws Exception {
        System.out.println("Not used");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodePlusOp p_node)  throws Exception {
        System.out.println("Plus op");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeMinusOp p_node)  throws Exception {
        System.out.println("Minus op");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeOrOp p_node)  throws Exception {
        System.out.println("Or Op");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeMulOp p_node)  throws Exception {
        System.out.println("Mul op");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeExpr p_node)  throws Exception {
        System.out.println("Expr");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeTerm p_node)  throws Exception {
        System.out.println("Term");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeDimList p_node)  throws Exception {
        System.out.println("Dim list");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeEmptyArray p_node)  throws Exception {
        System.out.println("Empty Array");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeAssign p_node) throws Exception{
        System.out.println("Assign");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeRelExpr p_node)  throws Exception {
        System.out.println("Relexpr");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFuncList p_node)  throws Exception {
        System.out.println("Func list");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeInherList p_node)  throws Exception {
        System.out.println("Inherit List");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeMembList p_node)  throws Exception {
        System.out.println("Member list");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeDot p_node)  throws Exception {
        System.out.println("Dot");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeIndexList p_node)  throws Exception {
        System.out.println("Index list");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeAParams p_node)  throws Exception {
        System.out.println("AParams");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeIntNum p_node) throws Exception{
        System.out.println("Int num");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
        String varType = p_node.m_symtabentry.m_type; 
        int size = 0;
        if (varType.equals("integer")) {
            size = 4;
        }
        else if (varType.equals("float")) {
            size = 8;
        }
        else {
            size = getStructSize(p_node);
        }

        p_node.m_symtabentry.m_size = size;
        this.updateOffset(p_node);
    }

    @Override
    public void visit(ASTNodeFloatNum p_node) throws Exception {
        System.out.println("Float num");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
        String varType = p_node.m_symtabentry.m_type; 
        int size = 0;
        if (varType.equals("integer")) {
            size = 4;
        }
        else if (varType.equals("float")) {
            size = 8;
        }
        else {
            size = getStructSize(p_node);
        }

        p_node.m_symtabentry.m_size = size;
        this.updateOffset(p_node);
    }

    @Override
    public void visit(ASTNodeInt p_node)  throws Exception {
        System.out.println("Int type");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFloat p_node)  throws Exception {
        System.out.println("Float type");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeArithExpr p_node)  throws Exception {
        System.out.println("Arith expr");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeNotFactor p_node)  throws Exception {
        System.out.println("Not factor");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeSignFactor p_node)  throws Exception {
        System.out.println("Sign factor");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFParamsList p_node)  throws Exception {
        System.out.println("F Params");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeVarDecl p_node) throws Exception {
        System.out.println("Var");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren() ) {
            child.accept(this);
        }

        // Cheeck if we are in impl
        AST temp = p_node;
        boolean isMemberDecl = false;
        boolean isMemberFunc = false;
        while (temp != null) {
            if (temp.getToken().getValue().equals("MemberDecl")) {
                isMemberDecl = true;
                break;
            }
            else if (temp.getToken().getValue().equals("MemberFunc")) {
                isMemberFunc = true;
                break;
            }
            temp = temp.getParent();
        }


        // determine the size for basic variables
        // If data
        if (isMemberDecl) {
            temp.m_symtabentry.m_size = this.sizeOfEntry(p_node.getChildren().get(1));
        } 
        // if local
        else if (!isMemberFunc) {
            p_node.m_symtabentry.m_size = this.sizeOfEntry(p_node.getChildren().get(1));
        }

        // Set the size for the variable within the struct by finding it in IMPL
        if (isMemberFunc) {
            // Set the structFinger to PROG
            AST structFinder = p_node;
            String functionName = "";
            String className = "";
            while (structFinder.getParent() != null) {
                // Get the function name
                if (structFinder.getToken().getValue().equals("MemberFunc")) {
                    functionName = structFinder.getChildren().get(0).getToken().getValue();
                }
                if (structFinder.getToken().getValue().equals("ImplDecl")) {
                    className = structFinder.getChildren().get(0).getToken().getValue();
                }
                structFinder = structFinder.getParent();
            }

            Stack<AST> stack = new Stack<>();
            AST curr = new AST();
            boolean isMethodFound = false;
            stack.push(structFinder);
            while (!stack.isEmpty()) {
                curr = stack.pop();

                if (curr.getChildren().size() > 0 
                && curr.getChildren().get(0).getToken().getValue().equals(functionName)
                && curr.getParent().getParent().getParent().getToken().getValue().equals("StructDecl")
                && curr.getParent().getParent().getParent().getChildren().get(0).getToken().getValue().equals(className)) {
                    isMethodFound = true;
                    break;
                }

                for (AST node: curr.getChildren()) {
                    stack.push(node);
                }
            }

            if (isMethodFound) {
                // For the new value added (local from IMPL), find it in the symboltable and assign its type + size
                for (SymbolTableEntry entry : curr.m_symtab.m_symlist) {
                    if (entry.m_name.equals(p_node.getChildren().get(0).getToken().getValue())) {
                        entry.m_size = this.sizeOfEntry(p_node.getChildren().get(1));
                        break;
                    }
                }

                curr.m_symtab.m_size = -(this.sizeOfTypeNode(curr.getChildren().get(2)));
                //then is the return addess is stored on the stack frame
                curr.m_symtab.m_size = 0;
                for (SymbolTableEntry entry : curr.m_symtab.m_symlist){
                    entry.m_offset = curr.m_symtab.m_size - entry.m_size; 
                    curr.m_symtab.m_size -= entry.m_size;
                }
            }
        }
    }

    @Override
    public void visit(ASTNodeFParam p_node)  throws Exception {
        System.out.println("F Param");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
			child.accept(this);
        }

        AST memberFuncNode = p_node;
        Boolean isMemberFunc = false;
        while (memberFuncNode != null) {
            if (memberFuncNode.getToken().getValue().equals("MemberFunc")) {
                isMemberFunc = true;
                break;
            }
            memberFuncNode = memberFuncNode.getParent();
        }
        // Do not add size to the parameter if we are assigning the size within IMPL (MemberFunc)
        if (!isMemberFunc) {
            // determine the size for basic variables
            p_node.m_symtabentry.m_size = this.sizeOfEntry(p_node.getChildren().get(1));
        }
    }

    @Override
    public void visit(ASTNodeStatOrVarDecl p_node)  throws Exception {
        System.out.println("Stat or Var Decl");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFParamList p_node)  throws Exception {
        System.out.println("FParamList");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFuncDef p_node)  throws Exception {
        System.out.println("Func Def");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }


        p_node.m_symtab.m_size = -(this.sizeOfTypeNode(p_node.getChildren().get(2)));
		//then is the return addess is stored on the stack frame
		p_node.m_symtab.m_size = 0;
		for (SymbolTableEntry entry : p_node.m_symtab.m_symlist){
			entry.m_offset = p_node.m_symtab.m_size - entry.m_size; 
			p_node.m_symtab.m_size -= entry.m_size;
		}
    }

    

    @Override
    public void visit(ASTNodeDivOp p_node)  throws Exception {
        System.out.println("Div Op");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeAndOp p_node)  throws Exception {
        System.out.println("And op");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeRelopEq p_node)  throws Exception {
        System.out.println("Relop eq");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeRelopNotEq p_node)  throws Exception {
        System.out.println("Relop noteq");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeRelopLt p_node)  throws Exception {
        System.out.println("Relop lt");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeRelopGt p_node)  throws Exception {
        System.out.println("Relop gt");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeRelopLeq p_node)  throws Exception {
        System.out.println("Relop leq");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeRelopGeq p_node)  throws Exception {
        System.out.println("Reqlop geq");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeStatement p_node)  throws Exception {
        System.out.println("Statement");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeVoidType p_node)  throws Exception {
        System.out.println("Void type");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeMultOp p_node)  throws Exception {
        System.out.println("Mult Op");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
        String varType = p_node.m_symtabentry.m_type; 
        int size = 0;
        if (varType.equals("integer")) {
            size = 4;
        }
        else if (varType.equals("float")) {
            size = 8;
        }
        else {
            size = getStructSize(p_node);
        }

        p_node.m_symtabentry.m_size = size;
        this.updateOffset(p_node);
    }

    @Override
    public void visit(ASTNodeAddOp p_node)  throws Exception {
        System.out.println("Add op");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
        String varType = p_node.m_symtabentry.m_type; 
        int size = 0;
        if (varType.equals("integer")) {
            size = 4;
        }
        else if (varType.equals("float")) {
            size = 8;
        }
        else {
            size = getStructSize(p_node);
        }

        p_node.m_symtabentry.m_size = size;
        this.updateOffset(p_node);
    }

    @Override
    public void visit(ASTNodeFactor p_node)  throws Exception {
        System.out.println("Factor");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodePlusSign p_node)  throws Exception {
        System.out.println("Plus Sign");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeMinusSign p_node)  throws Exception {
        System.out.println("Minus Sign");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeStatBlockList p_node)  throws Exception {
        System.out.println("Stat Block List");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeAssignStatement p_node) throws Exception {
        System.out.println("Assign stat");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }
    
    @Override
    public void visit(ASTNodeIfStatement p_node)  throws Exception {
        System.out.println("If stat");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeWhileStatement p_node)  throws Exception {
        System.out.println("While stat");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeReadStatement p_node)  throws Exception {
        System.out.println("Read stat");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeWriteStatement p_node)  throws Exception {
        System.out.println("Write stat");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeReturnStatement p_node)  throws Exception {
        System.out.println("Return stat");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeStructDecl p_node)  throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
        // compute total size and offsets along the way		
        // this should be node on all nodes that represent
        // a scope and contain their own table
        for (SymbolTableEntry entry : p_node.m_symtab.m_symlist){
            entry.m_offset = p_node.m_symtab.m_size - entry.m_size;
            p_node.m_symtab.m_size -= entry.m_size;
        }
    }

    @Override
    public void visit(ASTNodeImplDefDecl p_node)  throws Exception {
        System.out.println("ImplDefDecl");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFuncDefDecl p_node)  throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
        p_node.m_symtab.m_size = -(this.sizeOfTypeNode(p_node.getChildren().get(2)));
		//then is the return addess is stored on the stack frame
		p_node.m_symtab.m_size = 0;
		for (SymbolTableEntry entry : p_node.m_symtab.m_symlist){
			entry.m_offset = p_node.m_symtab.m_size - entry.m_size; 
			p_node.m_symtab.m_size -= entry.m_size;
		}
    }

    @Override
    public void visit(ASTNodeIntegerType p_node) throws Exception{
        System.out.println("Integer type");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodePublicVisibility p_node)  throws Exception {
        System.out.println("Public");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodePrivateVisibility p_node)  throws Exception {
        System.out.println("Private");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeFloatType p_node)  throws Exception {
        System.out.println("Float type");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(ASTNodeMemberDecl p_node)  throws Exception {
        System.out.println("Member Decl");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren() ) {
			child.accept(this);
        }

        AST funcNode = p_node.getChildren().get(1); 
        AST memberVarOrFunc = p_node.getChildren().get(1); 

        boolean isVar = false;
        if (memberVarOrFunc.getToken().getType().equals("VarDecl")) {
            isVar = true;
        }


        // If var is data
        if (isVar) {
            p_node.m_symtabentry.m_size = this.sizeOfEntry(memberVarOrFunc.getChildren().get(1));
        }
        // TODO: Do we need this for the function? If so, update function entry to include size and offset for memberdecl
        // else {
        //     p_node.m_symtabentry.m_size = this.sizeOfEntry(memberVarOrFunc.getChildren().get(2));
        // }
		// TODO: Check for function size 
    }

    @Override
    public void visit(ASTNodeMemberFunc p_node)  throws Exception {
        System.out.println("Member Func");
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
    }
}
