package mysql.inter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddForeignKey extends AlterTable
{
    public String foreignTable;
    public String constraintID;
    
    public List<String> localKeys = new ArrayList<String>();    
    public List<String> foreighKeys = new ArrayList<String>();
    

    
    public AddForeignKey(String localTableName, String foreignTable, String constrantID, List<String> localKeys, List<String> foreighKeys )
    {
    	tableName  = localTableName;
    	this.foreignTable = foreignTable;
    	this.constraintID = constrantID;
    	this.localKeys = localKeys;    	
    	this.foreighKeys = foreighKeys;


    }
    
    public String getKeyName()
    {
    	String name  = this.getClass().getSimpleName();
    	String keys = generateKeys(localKeys);
    	String constraint = constraintID == null? "": "-" + constraintID;
    	String foreignKeys = generateKeys(foreighKeys);
    	return name + "-" + tableName + constraint + "-" + keys + foreignKeys;
    }
    
    @Override
    public void add(BufferedWriter out)
    {
    	String constraint = constraintID == null? "": " CONSTRAINT " + constraintID;
    	
    	String add = "ALTER TABLE " + tableName + " ADD" + constraint
    			+ " FOREIGN KEY ("	+ generateKeys(localKeys) + ")" 
				+ " REFERENCES " + foreignTable
				+ " (" + generateKeys(foreighKeys) + ");";

    	try {
			out.write(add);
	    	out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Override
    public void drop(BufferedWriter out)
    {

    	
    	String stmt = "ALTER TABLE " + tableName 
    			+ " DROP FOREIGN KEY "	+ constraintID + ";";

    	try {
        	if (constraintID == null)
        	{
        		System.out.println("Can't drop foreign key without constraint ID");
        		out.write("Can't drop foreign key, use the SHOW CREATE TABLE to find the constraint ID" );
        		out.newLine();
        	}
    		
			out.write(stmt);
	    	out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    

}
