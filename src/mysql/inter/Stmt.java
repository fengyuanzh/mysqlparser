package mysql.inter;

import java.io.BufferedWriter;
import java.util.LinkedHashMap;
import java.util.List;

public class Stmt
{
    public String toString()
    {
    	throw new UnsupportedOperationException();
    }
    
	public List<Stmt> traversal(LinkedHashMap<String, Stmt> map)
	{
		throw new UnsupportedOperationException();
	}
	
	public String getKey()
	{
		throw new UnsupportedOperationException();
	}
	
	public void add(BufferedWriter out, Block b )
	{
		throw new UnsupportedOperationException();
	}
	
	public void drop(BufferedWriter out, Block b )
	{
		throw new UnsupportedOperationException();
	}
	
	public void diff(BufferedWriter out, Block targetBlock, Stmt sourceStmt)
	{
		throw new UnsupportedOperationException();
	}
}
