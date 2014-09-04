/* Copyright MetaBit Systems Pty 2004 */

package mysql.inter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Key extends Stmt
{
	public String constraint;
    public String name;
    
    public KeyType type;
    public List<String> keys = new ArrayList<String>();
    
    public Key (String constraint, String name, KeyType type, List<String> keys  )
    {
    	this.constraint = constraint;
    	this.name = name;
    	this.keys = keys;
    	this.type = type;
    }
    
    public String toString()
    {
    	String value ="";
    	
    	if (constraint != null){
    		value = value + "CONSTRAINT " + constraint + " ";
    	}
    	
    	value =  value + type.getName();
    	
    	if (name != null)
    	{
    		value = value + " " + name + " ";
    	}
    	
    	value = value + generateKeys(keys);
    	return value;
    }
    
	protected String generateKeys(List<String> list) {
		String keys = "";
		if (list.size() > 0)
		{
			keys += "(";
		}
		
    	for (int i = 0; i<list.size(); i++ )
    	{
    		if (i == 0)
    		{
    			keys += list.get(i);
    		}
    		else
    		{
    			keys += ("," + list.get(i));
    		}
    	}
		if (list.size() > 0)
		{
			keys += ")";
		}
    	return keys;
	}
	
	public String getKey()
	{
		return toString();
	}
	
	@Override
	public void diff(BufferedWriter out, Block b, Stmt sourceStmt)
	{
		// No need to do diff
	} 
	
	@Override
	public void add(BufferedWriter out, Block b) {
		String add = "ALTER TABLE " + b.toString() + " ADD " + toString() + ";";
		
		try {
			out.write(add);
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void drop(BufferedWriter out, Block b){
		String drop = "ALTER TABLE " + b.toString() + " DROP " ;

		if (type == KeyType.PRIMARY_KEY)
		{
			drop += type.getName() + ";" ;
		}
		else if (name != null)
		{
			drop += "KEY " + name + ";" ;
		}
		else
		{
			throw new RuntimeException("Don't know how to drop the key");
		}
		try {
			out.write(drop);
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
}
