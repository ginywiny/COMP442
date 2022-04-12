import java.util.Vector;

public class InheritedTableEntry extends SymbolTableEntry {

	Vector<String> inheritIdList = new Vector<>();
	
	public InheritedTableEntry(SymbolTable p_table, Vector<String> idList){
		super("Inherit", "NA", "NA", p_table);
		this.inheritIdList = idList;
		super.setInheritanceList(inheritIdList);
	}

	public String toString(){
		if (inheritIdList.size() == 0) {
			return String.format("%-12s" , "| " + "inherit") + 
			String.format("%-40s"  , "| none" ) + 
			"|";
		}

		else {
			return 	String.format("%-12s" , "| " + "inherit") +
			String.format("%-40s"  , "| " + inheritIdList) + 
			"|";
		}
		
	}	

}
