import java.util.List;

public class ASTNodeFactor extends AST {
    
    public ASTNodeFactor(TokenType token) {
        // Assign token to parent AST Node
        super(token);
    }

    public ASTNodeFactor(TokenType token, AST child) {
        // Assign token to parent AST Node
        super(token);
        this.addChild(child);
    }

    public ASTNodeFactor(TokenType token, List<AST> children) {
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
