public class ASTNodeAssign extends AST {
    
    public ASTNodeAssign(TokenType token) {
        // Assign token to parent AST Node
        super(token);
    }

    public void accept(Visitor visitor) throws Exception {
        // for (AST child: this.getChildren()) {
        //     child.accept(visitor);
        // }
        visitor.visit(this);
    }
}
