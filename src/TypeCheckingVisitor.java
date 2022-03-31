import java.io.File;
import java.io.PrintWriter;

public class TypeCheckingVisitor implements Visitor {
    private String m_outputfilename;
    Integer varCounter = 0;
    public TypeCheckingVisitor(String path) {
        this.m_outputfilename = path;
    }

    public String getNewTempVarName(){
    	varCounter++;
    	return "t" + varCounter.toString();  
    }

    @Override
    public void visit(ASTNodeProg node) {
        System.out.println();
        if (!this.m_outputfilename.isEmpty()) {
			File file = new File(this.m_outputfilename);
			try (PrintWriter out = new PrintWriter(file)){ 
			    // out.println(p_node.m_symtab);
			    out.println("Test semantic file");
			}
			catch(Exception e){
				e.printStackTrace();}
		}
    }

    @Override
    public void visit(AST node) {
        System.out.println(node.getToken().getType());
    }

    @Override
    public void visit(ASTNodeId node) {
        System.out.println(node.getToken().getType());
    }

    @Override
    public void visit(ASTNodeType node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodePlusOp node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeMinusOp node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeOrOp node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeMulOp node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeExpr node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeTerm node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeDimList node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeAssign node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeRelExpr node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeFuncList node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeInherList node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeMembList node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeDot node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeIndexList node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeAParams node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeIntNum node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeFloatNum node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeInt node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeFloat node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeArithExpr node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeNotFactor node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeSignFactor node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeFParamsList node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeVarDecl node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeFParam node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeStatOrVarDecl node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeFParamList node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeFuncDef node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeMultOp node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeDivOp node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeAndOp node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeRelopEq node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeRelopNotEq node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeRelopLt node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeRelopGt node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeRelopLeq node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeRelopGeq node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeStatement node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeAssignStatement node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeVoidType node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeAddOp node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeFactor node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodePlusSign node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeMinusSign node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeStatBlockList node) {
        System.out.println();
    }
    
    @Override
    public void visit(ASTNodeIfStatement node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeWhileStatement node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeReadStatement node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeWriteStatement node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeReturnStatement node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeStructDecl node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeImplDefDecl node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeFuncDefDecl node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeIntegerType node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodePublicVisibility node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodePrivateVisibility node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeFloatType node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeMemberFunc node) {
        System.out.println();
    }

    @Override
    public void visit(ASTNodeMemberDecl node) {
        System.out.println();
    }
}
