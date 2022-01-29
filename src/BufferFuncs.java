import java.io.BufferedReader;
import java.io.FileReader;

public class BufferFuncs {
    static BufferedReader buff;

    BufferFuncs(FileReader file) {
        this.buff = new BufferedReader(file);
    }


    // Get the next char from the buffer stream
    public Character getNextChar() throws Exception{
        // Check if not empty
        buff.mark(1);
        String check = buff.readLine();
        if (check == null) {
            return null;
        }
        buff.reset();

        // Newline (\n) == value 13
        // Startline == value 10
        // int temp = buff.read();
        // System.out.println("Val: " + temp);
        // Character next = (char)temp;

        Character next = (char)buff.read(); 
        return next;
    }

    // Peek the following char and return back to original
    public Character peekNextChar() throws Exception {
        // Check if not empty
        buff.mark(1);
        String check = buff.readLine();
        if (check == null) {
            return null;
        }
        buff.reset();

        // Peek the next char
        buff.mark(1);
        Character test = (char)buff.read(); // Read next char

        // Reset back to start
        buff.reset();
        return test;
    }

    public BufferedReader getBuffer() {
        return buff;
    }

    public void setReadLine() throws Exception {
        String test = buff.readLine();

        System.out.println(test);
    }


    // public Character peekDepth(int depth) throws Exception {
    //     // Check if not EOF
    //     buff.mark(1);
    //     String check = buff.readLine();
    //     if (check == null) {
    //         return null;
    //     }
    //     buff.reset();
    // }

}
