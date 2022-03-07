import java.util.ArrayList;
import java.util.List;

public class AST {

    // To fix with more info
    private TokenType token = null;
    // private String type = "";

    private List<AST> children = new ArrayList();
    private AST parent = null;
    public static int depth = 0;

    // public AST(TokenType token) {
    //     this.data = token;
    //     this.children = null;
    //     // this.depth = 0;
    // }

    // public AST(String type) {
    //     this.children = new ArrayList();
    //     this.type = type;
    // }

    public AST() {
        this.children = new ArrayList();
        this.token = new TokenType();
    }

    public AST(TokenType token) {
        this.children = new ArrayList();
        this.token = token;
    }

    public void setParentNode(AST parent) {
        this.parent = parent; 
    }

    public void addChild(AST child) {
        // child.parent = this;
        child.setParentNode(this); // Direct child to parent
        this.children.add(child); // Add child node to list of parent
    }

    public List<AST> getChildren() {
        return children;
    }

    public boolean isParentRoot() {
        return this.parent == null;
    }

    public TokenType getToken() {
        return this.token;
    }

    public void setASTToken(String type, String value, int lineNumber) {
        this.token.setAll(type, value, lineNumber);
    }

    public void setASTToken(TokenType token) {
        this.token = token;
    }
}
