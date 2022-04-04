import java.util.Vector;

public class SymbolTableEntry {
    public String          m_kind       = null;
	public String          m_type       = null;
	public String          m_name       = null;
	public int             m_size       = 0;
	public int             m_offset     = 0;
	public SymbolTable          m_subtable   = null;
	public Vector<Integer> m_dims       = new Vector<Integer>();
	
	public SymbolTableEntry() {};
	
	public SymbolTableEntry(String p_kind, String p_type, String p_name, SymbolTable p_subtable){
		m_kind = p_kind;
		m_type = p_type;
		m_name = p_name;
		m_subtable = p_subtable;
	}
}
