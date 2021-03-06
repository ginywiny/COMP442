public class TokenType {
        private String type;
        private String value;
        private int lineNumber;
        private Boolean valid;


    TokenType(){
        this.type = "NAN"; //Ex.  id
        this.value = "NAN"; //Ex. variableTemp
        this.lineNumber = -1;
        this.valid = false;
    }

    public String getType() {
        return type;
    }
    public String getValue() {
        return value;
    }
    public int getLineNumber() {
        return lineNumber;
    }

    public void setAll(String type, String value, int lineNumber) {
        this.type = type;
        this.value = value;
        this.lineNumber = lineNumber; 
        if (type.equals("invalidchar") || type.equals("invalidnum") || type.equals("invalidid")) {
            this.valid = false;
        }
        else {
            this.valid = true;
        }
    }

    public void setAllWithoutId(String valueBefore, int lineNumber) {
        this.lineNumber = lineNumber; 

        if (valueBefore.charAt(valueBefore.length() - 1) == '$') {
            this.value = new StringBuilder(valueBefore).deleteCharAt(valueBefore.length()-1).toString();
            this.type = "invalidid";
            this.valid = false;
        } else {
            this.type = "id";
            this.value = valueBefore;
            this.valid = true;
        }
    }
    
    // See if token is valid or an error
    public Boolean isValid() {
        return valid;
    }

    public String getTokenString() {
        String output = String.format("[%s, %s, %d]", type, value, lineNumber);
        return output;
    }

    public void printAll() {
        if (lineNumber != -1) {
            String output = String.format("[%s, %s, %d]", type, value, lineNumber);
            System.out.println(output);
        }
    }
}
