import java.util.List;

public class ASTNodeAssignStatement extends AST {
    
    public ASTNodeAssignStatement(TokenType token) {
        // Assign token to parent AST Node
        super(token);
    }

    public ASTNodeAssignStatement(TokenType token, List<AST> children) {
        // Assign token to parent AST Node
        super(token);
        for (AST child : children) {
            this.addChild(child);
        }
    }

    public ASTNodeAssignStatement(TokenType token, AST left, AST right) {
        // Assign token to parent AST Node
        super(token);
        this.addChild(left);
        this.addChild(right);
    }

    public void accept(Visitor visitor) throws Exception {
        // for (AST child: this.getChildren()) {
        //     child.accept(visitor);
        // }
        visitor.visit(this);
    }
}
