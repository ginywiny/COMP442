import java.io.BufferedReader;
import java.io.LineNumberReader;
import java.io.FileReader;

public class BufferFuncs {
    // static BufferedReader buff;
    static LineNumberReader buff;

    BufferFuncs(FileReader file) {
        // this.buff = new BufferedReader(file);
        this.buff = new LineNumberReader(file);
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

    // Return bufferedreader
    public BufferedReader getBuffer() {
        return buff;
    }

    // Read a line from the buffer
    public void setReadLine() throws Exception {
        String test = buff.readLine();

        System.out.println(test);
    }

    // Read line number from inputfile
    public int getLineNumber() throws Exception {
        return buff.getLineNumber();
    }

    public Boolean isEndOfFile() throws Exception{
        // Check if not empty
        buff.mark(1);
        String check = buff.readLine();
        if (check == null) {
            return true;
        }
        buff.reset();
        return false;
    }

}
