package mysql.inter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddUniqueKey extends AlterTable
{
    public String constraintID;
    public List<String> localKeys = new ArrayList<String>();
    
    public AddUniqueKey(String tableName, String constraintID, List<String> localKeys)
    {
    	this.tableName = tableName;
    	this.constraintID = constraintID;
    	this.localKeys = localKeys;
    }
    
    public String getKeyName()
    {
    	String name  = this.getClass().getSimpleName();
    	String keys = generateKeys(localKeys);
    	String constraint = constraintID == null? "": "-" + constraintID;
    	return name + "-" + tableName + constraint + "-" + keys;
    }
    
    @Override
    public void add(BufferedWriter out)
    {
    	String constraint = constraintID == null? "": " CONSTRAINT " + constraintID;
    	
    	String add = "ALTER TABLE " + tableName + " ADD" + constraint
    			+ " UNIQUE ("	+ generateKeys(localKeys) + ");";

    	try {
			out.write(add);
	    	out.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void drop(BufferedWriter out)
    {

    	
    	String stmt = "ALTER TABLE " + tableName 
    			+ " DROP KEY "	+ constraintID + ";";

    	try {
        	if (constraintID == null)
        	{
        		System.out.println("Can't drop foreign key without constraint ID");
        		out.write("Can't drop foreign key, use the SHOW CREATE TABLE to find the constraint ID" );
        	}
    		
			out.write(stmt);
	    	out.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}
