import java.util.List;

public class ASTNodeIfStatement extends AST {
    
    public ASTNodeIfStatement(TokenType token) {
        // Assign token to parent AST Node
        super(token);
    }

    public ASTNodeIfStatement(TokenType token, List<AST> listNodes) {
        // Assign token to parent AST Node
        super(token);
        for (AST child: listNodes) {
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
