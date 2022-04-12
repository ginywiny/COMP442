import java.util.Vector;

public class FunctionTableEntry extends SymbolTableEntry {

    public Vector<String> m_params   = new Vector<String>();
	public Vector<String> m_dimTypeList = new Vector<String>();
	
	public FunctionTableEntry(String p_type, String p_name, SymbolTable p_table, Vector<String> dimTypeList){
		super(new String("function"), p_type, p_name, p_table);
		m_dimTypeList = dimTypeList;
	}

	public String toString(){
		return 	String.format("%-12s" , "| " + m_kind) +
				String.format("%-12s" , "| " + m_name) + 
				String.format("%-28s"  , "| " + m_dimTypeList + ": " + m_type) + 
				"|" + 
				m_subtable;
	}	

}
