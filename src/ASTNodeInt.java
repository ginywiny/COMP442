import java.util.List;

public class ASTNodeInt extends AST {
    
    public ASTNodeInt(TokenType token) {
        // Assign token to parent AST Node
        super(token);
    }

    public ASTNodeInt(TokenType token, List<AST> children) {
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
