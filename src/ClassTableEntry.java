public class ClassTableEntry extends SymbolTableEntry {
    public ClassTableEntry(String p_name, SymbolTable p_subtable){
		super(new String("class"), p_name, p_name, p_subtable);
	}
		
	public String toString(){
		return 	String.format("%-12s" , "| " + m_kind) +
				String.format("%-40s" , "| " + m_name) + 
				"|" + 
				m_subtable;
	}
}