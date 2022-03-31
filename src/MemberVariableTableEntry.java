import java.util.Vector;

public class MemberVariableTableEntry extends SymbolTableEntry {

	String m_visibility;

    public MemberVariableTableEntry(String p_kind, String p_type, String p_name, Vector<Integer> p_dims, String visibility){
		super(p_kind, p_type, p_name, null);
		m_dims = p_dims;
		m_visibility = visibility;
	}
		
	public String toString(){
		// If empty array declared in function argument
		if (m_dims.size() > 0 && m_dims.get(0) == -420) {
			return 	String.format("%-12s" , "| " + m_kind) +
				String.format("%-12s" , "| " + m_name) + 
				String.format("%-12s"  , "| " + m_type) + 
              	String.format("%-12s"  , "| " + "[]") + 
				// String.format("%-8s"  , "| " + m_size) + 
				// String.format("%-8s"  , "| " + m_offset) +
				String.format("%-8s"  , "| " + m_visibility)
		        + "|";
		}
		// If array declared in function argument with numbers
		else if (m_dims.size() > 0) {
			return 	String.format("%-12s" , "| " + m_kind) +
				String.format("%-12s" , "| " + m_name) + 
				String.format("%-12s"  , "| " + m_type) + 
              	String.format("%-12s"  , "| " + m_dims) + 
				// String.format("%-8s"  , "| " + m_size) + 
				// String.format("%-8s"  , "| " + m_offset) +
				String.format("%-8s"  , "| " + m_visibility)
		        + "|";
		}
		// If not array
		else {
			return 	String.format("%-12s" , "| " + m_kind) +
				String.format("%-12s" , "| " + m_name) + 
				String.format("%-12s"  , "| " + m_type) + 
              	String.format("%-12s"  , "| ") + 
				// String.format("%-8s"  , "| " + m_size) + 
				// String.format("%-8s"  , "| " + m_offset) +
				String.format("%-8s"  , "| " + m_visibility)
		        + "|";
		}
		
	}
}
