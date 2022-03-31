import java.util.List;

public class ASTNodeFParam extends AST {
    
    public ASTNodeFParam(TokenType token) {
        // Assign token to parent AST Node
        super(token);
    }

    public ASTNodeFParam(TokenType token, List<AST> listNodes) {
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
