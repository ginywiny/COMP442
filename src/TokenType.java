public class TokenType {
        private String type;
        private String value;
        private int lineNumber;


    TokenType(){
        this.type = "NAN";
        this.value = "NAN";
        this.lineNumber = -1;
    }

    public String getType() {
        return type;
    }
    public String getValue() {
        return value;
    }
    public int lineNumber() {
        return lineNumber;
    }

    public void setAll(String type, String value, int lineNumber) {
        this.type = type;
        this.value = value;
        this.lineNumber = lineNumber; 
    }

    public void printAll() {
        if (lineNumber != -1) {
            String output = String.format("[%s, %s, %d]", type, value, lineNumber);
            System.out.println(output);
        }
        else {
            System.out.println("Invalid character");
        }
    }
}
