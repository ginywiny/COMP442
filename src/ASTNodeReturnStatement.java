import java.util.List;

public class ASTNodeReturnStatement extends AST {
    
    public ASTNodeReturnStatement(TokenType token) {
        // Assign token to parent AST Node
        super(token);
    }

    public ASTNodeReturnStatement(TokenType token, AST child) {
        // Assign token to parent AST Node
        super(token);
        this.addChild(child);
    }

    public ASTNodeReturnStatement(TokenType token, List<AST> children) {
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
