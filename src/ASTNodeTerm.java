import java.util.List;

public class ASTNodeTerm extends AST {
    
    public ASTNodeTerm(TokenType token) {
        // Assign token to parent AST Node
        super(token);
    }

    public ASTNodeTerm(TokenType token, List<AST> listNodes) {
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
