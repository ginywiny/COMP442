import java.util.List;

public class ASTNodeAddOp extends AST {
    
    public ASTNodeAddOp(TokenType token) {
        // Assign token to parent AST Node
        super(token);
    }

    public ASTNodeAddOp(TokenType token, AST left, AST right) {
        // Assign token to parent AST Node
        super(token);
        this.addChild(left);
        this.addChild(right);
    }

    public ASTNodeAddOp(TokenType token, List<AST> children) {
        // Assign token to parent AST Node
        super(token);
        for (AST child : children) {
            this.addChild(child);
        }
    }

    public void accept(Visitor visitor) throws Exception {
        // for (AST child: this.getChildren()) {
        //     child.accept(visitor);
        // }
        visitor.visit(this);
    }
}
