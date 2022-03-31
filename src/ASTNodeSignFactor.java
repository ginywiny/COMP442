import java.util.List;

public class ASTNodeSignFactor extends AST {
    
    public ASTNodeSignFactor(TokenType token) {
        // Assign token to parent AST Node
        super(token);
    }

    public ASTNodeSignFactor(TokenType token, AST child) {
        // Assign token to parent AST Node
        super(token);
        this.addChild(child);
    }

    public ASTNodeSignFactor(TokenType token, List<AST> children) {
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
