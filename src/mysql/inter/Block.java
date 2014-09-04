package mysql.inter;

import java.io.BufferedWriter;
import java.util.LinkedHashMap;
import java.util.List;

public class Block {

	public List<Block> traversal(LinkedHashMap<String, Block> map	)
	{
		return null;
	}
	
    public String getKeyName()
    {
    	throw new UnsupportedOperationException();
    }
	
    public void add( BufferedWriter out )
    {
    	throw new UnsupportedOperationException();
    }
    
    public void drop( BufferedWriter out )
    {
    	throw new UnsupportedOperationException();
    }
    
    public void diff(BufferedWriter out, Block b)
    {
    	
    }
}
