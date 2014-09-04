package mysql.inter;

import java.util.List;

public class AlterTable extends Block
{
    public String tableName;
    
	protected String generateKeys(List<String> list) {
		String keys = "";
    	for (int i = 0; i<list.size(); i++ )
    	{
    		if (i == 0)
    		{
    			keys = list.get(i);
    		}
    		else
    		{
    			keys += ("," + list.get(i));
    		}
    	}
    	return keys;
	}
}
