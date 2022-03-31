import java.util.List;

public class ASTNodeVarDecl extends AST {
    
    public ASTNodeVarDecl(TokenType token) {
        // Assign token to parent AST Node
        super(token);
    }

    public ASTNodeVarDecl(TokenType token, List<AST> listNodes) {
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
