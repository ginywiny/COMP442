import java.util.List;

public class ASTNodeProg extends AST {
    
    public ASTNodeProg(TokenType token) {
        // Assign token to parent AST Node
        super(token);
    }

    public ASTNodeProg(TokenType token, List<AST> children) {
        // Assign token to parent AST Node
        super(token);
        for (AST child : children) {
            this.addChild(child);
        }
    }

    public void accept(Visitor visitor) throws Exception {
        // List<AST> temp = this.getChildren(); 
        // for (AST child: this.getChildren()) {
        //     child.accept(visitor);
        // }
        visitor.visit(this);
    }
}
