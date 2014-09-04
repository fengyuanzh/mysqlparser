package mysql.inter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class CreateTable extends Block
{
	public String tableName;
	public List<String> declares = new ArrayList<String>();
	public List<String> keys = new ArrayList<String>();
	public Stmt stmt = null;
	public LinkedHashMap<String, Stmt> stmtMap = new LinkedHashMap<String, Stmt>();
	public String engineType;
    public CreateTable(String tableName, String engineType, Stmt stmt )
    {
    	this.tableName = tableName;
		this.engineType = engineType;
    	this.stmt = stmt;
    	stmt.traversal(stmtMap);

    }
    
    public String getKeyName()
    {
    	String name  = this.getClass().getSimpleName();
    	return name + "-" + tableName ;
    }
    
    @Override
    public void add(BufferedWriter out)
    {
    	
    	String add = "CREATE TABLE " + tableName + " (\r\n";
    	
    	String stmts = add;
    	Stmt[] sts = new Stmt[stmtMap.size()];
    	stmtMap.values().toArray(sts);
		for ( int i = 0; i < sts.length; i++ )
        {
			if (i  == sts.length - 1)
			{
        		stmts = stmts + "\t" + sts[i] +"\r\n";
			}
			else
			{
				stmts = stmts +  "\t" + sts[i] + ",\r\n";
			}
        }
		stmts = stmts + ")\r\n"  + "ENGINE = " + engineType + ";";

    	try {
			out.write(stmts);
	    	out.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Override
    public void drop(BufferedWriter out)
    {
    	
    	String drop = "DROP TABLE IF EXISTS " + tableName + ";";
    	try {
			out.write(drop);
	    	out.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void diff(BufferedWriter out, Block sourceB)
    {
    	LinkedHashMap<String, Stmt> sourceMap = ((CreateTable)sourceB).stmtMap;
    	
    	// Drop from source
        for ( Stmt sourceStmt : ((CreateTable)sourceB).stmtMap.values())
        {
            if ( sourceStmt instanceof Declare)
            {
                Stmt targetStmt = stmtMap.get(((Declare) sourceStmt).getKey());
                if (targetStmt == null)
                {
                    sourceStmt.drop(out, this);
                }
            }
            else
            {
                Stmt targetStmt = stmtMap.get(( sourceStmt).getKey());
                if (targetStmt == null)
                {
                    sourceStmt.drop(out, this);
                }

            }
        }
    	// Add to source
		for ( Stmt targetStmt : stmtMap.values())
    	{
    		if ( targetStmt instanceof Declare)
    		{
    			Stmt sourceStmt = sourceMap.get(targetStmt.getKey());
    			if (sourceStmt == null)
    			{
    				targetStmt.add(out, this);
    			}
    			else
    			{
    				targetStmt.diff(out, this, sourceStmt);
    			}
    		}
    		else
    		{
    			Stmt sourceStmt = sourceMap.get(targetStmt.getKey());
    			if (sourceStmt == null)
    			{
    				targetStmt.add(out, this);
    			}

    		}
    	}
		
		
		
    }
    
    public String toString()
    {
    	return tableName;
    }
}
