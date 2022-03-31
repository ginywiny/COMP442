import java.util.Vector;

public class MemberFuncTableEntry extends FunctionTableEntry {

	String m_visibility;

    public MemberFuncTableEntry(String p_type, String p_name, SymbolTable table, String visibility, Vector<String> dimTypeList){
		super(p_type, p_name, table, dimTypeList);
		this.m_visibility = visibility;
	}
		
	public String toString(){
		return 	String.format("%-12s" , "| " + m_kind) +
				String.format("%-12s" , "| " + m_name) + 
				String.format("%-12s"  , "| " + m_dimTypeList + ": " + m_type) + 
				String.format("%-17s"  , "| " + m_visibility)
		        + "|" +
				m_subtable;
	}
}
