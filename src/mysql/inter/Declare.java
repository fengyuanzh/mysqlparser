package mysql.inter;

import java.io.BufferedWriter;
import java.io.IOException;

public class Declare extends Stmt
{
    public String name;
    public DataType type;
    public boolean nullable;
    public String defaultValue;
    public int charLength;
    
    public Declare ( String name, DataType type, boolean nullable, String defaultValue, int charLength)
    {
    	this.name = name;
    	this.type = type;
    	this.charLength = charLength;
    	this.nullable = nullable;
    	this.defaultValue = defaultValue;
    }

    public String toString()
    {
    	return name + " " + type.toString(charLength)  + (nullable?"":" NOT NULL");
    }
    
    public String getKey() 
    {
		return name;
	}
    
    @Override
    public void add(BufferedWriter out, Block b)
    {
    	
    	String add = "ALTER TABLE " + b.toString() + " ADD COLUMN ";
    	add += name + " " + type.toString(charLength) + (nullable?"":" NOT NULL") 
    	+ ((defaultValue == null)?"":(" DEFAULT " + defaultValue));
    	
    	Object[] keys = (((CreateTable)b).stmtMap).keySet().toArray();
    	//System.out.println(array);

    	String position = " FIRST;";
    	for (int i = 0; i< keys.length; i++)
    	{
    		if (keys[i].equals(name) && i > 0){
    			position = " AFTER " + keys[i-1] + ";";
    			break;
    		}
    	}
    	add += position;
    	try {
			out.write(add);
	    	out.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Override
    public void drop(BufferedWriter out, Block b)
    {
    	
    	String drop = "ALTER TABLE " + b.toString() + " DROP COLUMN ";
    	drop += name + ";";
    	try {
			out.write(drop);
	    	out.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Override
	public void diff(BufferedWriter out, Block b, Stmt sourceStmt)
	{
		if ( !this.toString().equals(sourceStmt.toString()))
		{
			String modify = "ALTER TABLE " + b.toString() + " MODIFY COLUMN ";
			modify += name + " " + type.toString(charLength) + (nullable?"":" NOT NULL") 
	    	+ ((defaultValue == null)?"":(" DEFAULT " + defaultValue));
			
			try {
				out.write(modify);
		    	out.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
