import java.io.File;
import java.io.PrintWriter;
import java.util.Stack;

public class CodeGenerationVisitor implements Visitor {
    public Stack<String> m_registerPool   = new Stack<String>();
    public String        m_moonExecCode   = new String();              // moon instructions part
    public String        m_moonDataCode   = new String();              // moon data part
    public String        m_mooncodeindent = new String("          ");
    public String 	     m_outputfilename = new String();
    

    public CodeGenerationVisitor() {
    	// create a pool of registers as a stack of Strings
    	// assuming only r1, ..., r12 are available
    	for (Integer i = 12; i>=1; i--)
    		m_registerPool.push("r" + i.toString());
    }
    
    public CodeGenerationVisitor(String p_filename) {
    	this.m_outputfilename = p_filename; 
       	// create a pool of registers as a stack of Strings
    	// assuming only r1, ..., r12 are available
    	for (Integer i = 12; i>=1; i--)
    		m_registerPool.push("r" + i.toString());
    }


    @Override
    public void visit(ASTNodeProg p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren())
        child.accept(this);	
        // if the Visitor was given a file name, 
        // then write the generated code into this file
        if (!this.m_outputfilename.isEmpty()) {
            File file = new File(this.m_outputfilename);
            try (PrintWriter out = new PrintWriter(file)) {
                out.println(this.m_moonExecCode);
                out.println(this.m_moonDataCode);}		
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

        // Then, do the processing of this nodes' visitor
		// create a local variable and allocate a register to this subcomputation 
		String localregister1 = this.m_registerPool.pop();
		// generate code			
		m_moonExecCode += m_mooncodeindent + "% processing: " + p_node.m_moonVarName  + " := " + p_node.getToken().getValue() + "\n";
		// create a value corresponding to the literal value
		m_moonExecCode += m_mooncodeindent + "addi " + localregister1 + ",r0," + p_node.getToken().getValue() + "\n"; 
		// assign this value to a temporary variable (assumed to have been previously created by the symbol table generator)
		m_moonExecCode += m_mooncodeindent + "sw " + p_node.m_symtabentry.m_offset + "(r14)," + localregister1 + "\n";
		// deallocate the register for the current node
		this.m_registerPool.push(localregister1);
    }

    @Override
    public void visit(ASTNodeFloatNum p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }

        // Then, do the processing of this nodes' visitor
		// create a local variable and allocate a register to this subcomputation 
		String localregister1 = this.m_registerPool.pop();
		// generate code			
		m_moonExecCode += m_mooncodeindent + "% processing: " + p_node.m_moonVarName  + " := " + p_node.getToken().getValue() + "\n";
		// create a value corresponding to the literal value
		m_moonExecCode += m_mooncodeindent + "addi " + localregister1 + ",r0," + p_node.getToken().getValue() + "\n"; 
		// assign this value to a temporary variable (assumed to have been previously created by the symbol table generator)
		m_moonExecCode += m_mooncodeindent + "sw " + p_node.m_symtabentry.m_offset + "(r14)," + localregister1 + "\n";
		// deallocate the register for the current node
		this.m_registerPool.push(localregister1);
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


        // Then, do the processing of this nodes' visitor
		// allocate registers to this subcomputation 
		String localregister1 = this.m_registerPool.pop();
		String localregister2 = this.m_registerPool.pop();
		String localregister3 = this.m_registerPool.pop();
		String localregister4 = this.m_registerPool.pop();
		// generate code

        // Get the left part of the operation ([a] * b)
        AST leftFactor = p_node;
        boolean leftFound = false;
        while (leftFactor.getChildren().size() > 0 && leftFactor != null) {
            if (((leftFactor.getToken().getValue().equals("Factor")
            || leftFactor.getToken().getValue().equals("AddOp")
            || leftFactor.getToken().getValue().equals("MultOp")))
            && leftFactor != p_node) {
                leftFound = true;
                break;
            }
            leftFactor = leftFactor.getChildren().get(0);
        }

        // Get the right part of the operation (a * [b])
        AST rightFactor = p_node.getChildren().get(2);
        boolean rightFound = false;
        while (rightFactor.getChildren().size() > 0 && rightFactor != null) {
            if (((rightFactor.getToken().getValue().equals("Factor")
            || rightFactor.getToken().getValue().equals("AddOp")
            || rightFactor.getToken().getValue().equals("MultOp")))
            && rightFactor != p_node) {
                rightFound = true;
                break;
            }
            rightFactor = rightFactor.getChildren().get(0);
        }


        AST leftNode = new AST();
        AST rightNode = new AST();
        if (leftFound) {
            if (leftFactor.getToken().getValue().equals("Factor")) {
                leftNode = leftFactor.getChildren().get(0);
            }
            else {
                leftNode = leftFactor;
            }
        }
        if (rightFound) {
            if (rightFactor.getToken().getValue().equals("Factor")) {
                rightNode = rightFactor.getChildren().get(0);
            }
            else {
                rightNode = rightFactor;
            }
        }


		m_moonExecCode += m_mooncodeindent + "% processing: " + p_node.m_moonVarName + 
        " := " + leftNode.m_moonVarName + " + " + rightNode.m_moonVarName + "\n";
		// load the values of the operands into registers
		m_moonExecCode += m_mooncodeindent + "lw "   + localregister2 + 
        "," + p_node.m_symtab.lookupName(leftNode.m_moonVarName).m_offset + "(r14)\n";
        

		m_moonExecCode += m_mooncodeindent + "lw "   + localregister3 + "," + 
        p_node.m_symtab.lookupName(rightNode.m_moonVarName).m_offset + "(r14)\n";
		// add operands
		m_moonExecCode += m_mooncodeindent + "add "  + localregister4 + "," + localregister2 + "," + localregister3 + "\n"; 
		// assign the result into a temporary variable (assumed to have been previously created by the symbol table generator)
		m_moonExecCode += m_mooncodeindent + "sw "   + p_node.m_symtab.lookupName(p_node.m_moonVarName).m_offset + "(r14)," + localregister4 + "\n";		
		// deallocate the registers 
		this.m_registerPool.push(localregister1);
		this.m_registerPool.push(localregister2);		
		this.m_registerPool.push(localregister3);
		this.m_registerPool.push(localregister4);

    }

    @Override
    public void visit(ASTNodeAddOp p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }


        // Get the right part of the operation (a * [b])
        AST leftFactor = p_node;
        boolean leftFound = false;
        while (leftFactor.getChildren().size() > 0 && leftFactor != null) {
            if (((leftFactor.getToken().getValue().equals("Factor")
            || leftFactor.getToken().getValue().equals("MultOp")
            || leftFactor.getToken().getValue().equals("AddOp")))
            && leftFactor != p_node) {
                leftFound = true;
                break;
            }
            leftFactor = leftFactor.getChildren().get(0);
        }

        // Get the left part of the operation ([a] * b)
        AST rightFactor = p_node.getChildren().get(2);
        boolean rightFound = false;
        while (rightFactor.getChildren().size() > 0 && rightFactor != null) {
            if (((rightFactor.getToken().getValue().equals("Factor")
            || rightFactor.getToken().getValue().equals("MultOp")
            || rightFactor.getToken().getValue().equals("AddOp")))
            && rightFactor != p_node) {
                rightFound = true;
                break;
            }
            rightFactor = rightFactor.getChildren().get(0);
        }


        AST leftNode = new AST();
        AST rightNode = new AST();
        if (leftFound) {
            if (leftFactor.getToken().getValue().equals("Factor")) {
                leftNode = leftFactor.getChildren().get(0);
            }
            else {
                leftNode = leftFactor;
            }
        }
        if (rightFound) {
            if (rightFactor.getToken().getValue().equals("Factor")) {
                rightNode = rightFactor.getChildren().get(0);
            }
            else {
                rightNode = rightFactor;
            }
        }
        
        // Then, do the processing of this nodes' visitor
		// allocate registers to this subcomputation 
		String localregister1 = this.m_registerPool.pop();
		String localregister2 = this.m_registerPool.pop();
		String localregister3 = this.m_registerPool.pop();
		String localregister4 = this.m_registerPool.pop();
		// generate code
		m_moonExecCode += m_mooncodeindent + "% processing: " + p_node.m_moonVarName + " := " + leftNode.m_moonVarName + 
        " + " + rightNode.m_moonVarName + "\n";
		// load the values of the operands into registers
		m_moonExecCode += m_mooncodeindent + "lw "   + localregister2 + "," + p_node.m_symtab.lookupName(leftNode.m_moonVarName).m_offset + "(r14)\n";
		m_moonExecCode += m_mooncodeindent + "lw "   + localregister3 + "," + p_node.m_symtab.lookupName(rightNode.m_moonVarName).m_offset + "(r14)\n";
		// add operands
		m_moonExecCode += m_mooncodeindent + "add "  + localregister4 + "," + localregister2 + "," + localregister3 + "\n"; 
		// assign the result into a temporary variable (assumed to have been previously created by the symbol table generator)
		m_moonExecCode += m_mooncodeindent + "sw "   + p_node.m_symtab.lookupName(p_node.m_moonVarName).m_offset + "(r14)," + localregister4 + "\n";		
		// deallocate the registers 
		this.m_registerPool.push(localregister1);
		this.m_registerPool.push(localregister2);		
		this.m_registerPool.push(localregister3);
		this.m_registerPool.push(localregister4);
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

        // Then, do the processing of this nodes' visitor
		// allocate local registers
		String localregister1 = this.m_registerPool.pop();
		String localregister2 = this.m_registerPool.pop();
		String localregister3 = this.m_registerPool.pop();
        
        // Get the ID of the register to write to
        AST outputRegister = p_node.getParent().getChildren().get(0).getChildren().get(0);
        // Get the register contents from which data is acquired
        AST inputRegister = p_node;
        while (inputRegister.getChildren().size() > 0) {
            if (inputRegister.m_moonVarName.length() > 0 && inputRegister != p_node) {
                break;
            }
            inputRegister = inputRegister.getChildren().get(0);
        }

		//generate code
		m_moonExecCode += m_mooncodeindent + "% processing: "  + outputRegister.m_moonVarName + " := " + inputRegister.m_moonVarName + "\n";
		// load the assigned value into a register
		m_moonExecCode += m_mooncodeindent + "lw "   + localregister2 + "," + p_node.m_symtab.lookupName(inputRegister.m_moonVarName).m_offset + "(r14)\n";
		// assign the value to the assigned variable
		m_moonExecCode += m_mooncodeindent + "sw "   + p_node.m_symtab.lookupName(inputRegister.m_moonVarName).m_offset + "(r14)," + localregister2 + "\n";
		// deallocate local registers
		this.m_registerPool.push(localregister1);		
		this.m_registerPool.push(localregister2);		
		this.m_registerPool.push(localregister3);		
    }

    @Override
    public void visit(ASTNodeVoidType p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
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
		String localregister1 = this.m_registerPool.pop();

        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }

        AST returnFinder = p_node;
        while (returnFinder.getChildren().size() > 0) {
            if (returnFinder.m_moonVarName.length() > 0 && returnFinder != p_node) {
                break;
            }
            returnFinder = returnFinder.getChildren().get(0);
        }

        // copy the result of the return value into the first memory cell in the current stack frame
		// this way, the return value is conveniently at the top of the calling function's stack frame
		m_moonExecCode += m_mooncodeindent + "% processing: return("  + returnFinder.m_moonVarName + ")\n";
		m_moonExecCode += m_mooncodeindent + "lw " + localregister1 + "," + p_node.m_symtab.lookupName(returnFinder.m_moonVarName).m_offset + "(r14)\n";
		m_moonExecCode += m_mooncodeindent + "sw "   + "0(r14)," + localregister1 + "\n";
		this.m_registerPool.push(localregister1);	
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
        
        // THIS IS FINEEEE
        String funcName = p_node.getChildren().get(0).getToken().getValue();
        if (funcName.equals("main")) {
            // generate moon program's entry point
            m_moonExecCode += m_mooncodeindent + "entry\n";
            // make the stack frame pointer (address stored in r14) point 
            // to the top address allocated to the moon processor 
            m_moonExecCode += m_mooncodeindent + "addi r14,r0,topaddr\n";
            // propagate acceptance of this visitor to all the children
        }

        // TODO: CHECK TO SEE IF THIS IS RIGHT OR NOTTTTTT TO KEEP IN ELSE
        // ----------------------------------------------------
        else {
            // propagate accepting the same visitor to all the children
            // this effectively achieves Depth-First AST Traversal
            m_moonExecCode += m_mooncodeindent + "% processing function definition: "  + funcName + "\n";
            //create the tag to jump onto 
            // and copy the jumping-back address value in the called function's stack frame 
            m_moonExecCode += String.format("%-10s",funcName)  + "sw -4(r14),r15\n" ;
        }
        // ----------------------------------------------------


        
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }

        // TODO: CHECK TO SEE IF THIS IS RIGHT OR NOTTTTTT TO KEEP IN IF STATEMENT
        // ----------------------------------------------------
        if (!funcName.equals("main")) {
            // copy back the jumping-back address into r15
            m_moonExecCode += m_mooncodeindent + "lw r15,-4(r14)\n";
            // jump back to the calling function
            m_moonExecCode += m_mooncodeindent + "jr r15\n";	
        }
        // ----------------------------------------------------

        

        // THIS IS FINEEEE
        if (funcName.equals("main")) {
            // generate moon program's end point
            m_moonDataCode += m_mooncodeindent + "% buffer space used for console output\n";
            // buffer used by the lib.m subroutines
            m_moonDataCode += String.format("%-10s" , "buf") + "res 20\n";
            // halting point of the entire program
            m_moonExecCode += m_mooncodeindent + "hlt\n";
        }
    }

    @Override
    public void visit(ASTNodeIntegerType p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
        }
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
    }

    @Override
    public void visit(ASTNodeMemberFunc p_node) throws Exception {
        // propagate accepting the same visitor to all the children
		// this effectively achieves Depth-First AST Traversal
		for (AST child : p_node.getChildren()) {
            child.accept(this);
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
