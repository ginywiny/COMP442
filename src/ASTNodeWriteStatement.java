import java.util.List;

public class ASTNodeWriteStatement extends AST {
    
    public ASTNodeWriteStatement(TokenType token) {
        // Assign token to parent AST Node
        super(token);
    }

    public ASTNodeWriteStatement(TokenType token, AST child) {
        // Assign token to parent AST Node
        super(token);
        this.addChild(child);
    }

    public ASTNodeWriteStatement(TokenType token, List<AST> children) {
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
