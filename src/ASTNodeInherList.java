import java.util.List;

public class ASTNodeInherList extends AST {
    
    public ASTNodeInherList(TokenType token) {
        // Assign token to parent AST Node
        super(token);
    }

    public ASTNodeInherList(TokenType token, List<AST> listNodes) {
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
