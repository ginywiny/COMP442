import java.util.List;

public class ASTNodeNotFactor extends AST {
    
    public ASTNodeNotFactor(TokenType token) {
        // Assign token to parent AST Node
        super(token);
    }

    public ASTNodeNotFactor(TokenType token, AST child) {
        // Assign token to parent AST Node
        super(token);
        this.addChild(child);
    }

    public ASTNodeNotFactor(TokenType token, List<AST> children) {
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
