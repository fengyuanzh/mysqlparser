package mysql.inter;

import java.util.LinkedHashMap;
import java.util.List;

public class Stmts extends Stmt
{
    public Stmt s1;
    public Stmt s2;
    
    public Stmts(Stmt s1, Stmt s2)
    {
        this.s1 = s1;
        this.s2 = s2;
    }
    
    public String toString()
    {
    	return s1.toString();
    }

	public List<Stmt> traversal(LinkedHashMap<String, Stmt> map)
	{
		map.put(s1.getKey(), s1);

		if (s2 != null)
		{
			s2.traversal( map );
		}
		
		return null;
	}
    
}
