package mysql.inter;

import java.util.List;
import java.util.LinkedHashMap;

public class Blocks extends Block{

	Block b1;
	Block b2;
    public Blocks(Block b1, Block b2)
    {
        this.b1 = b1;
        this.b2 = b2;
    }
    
	public List<Block> traversal( LinkedHashMap<String, Block> map )
    {
        map.put( b1.getKeyName(), b1 );

        if (b2 != null)
        {
            b2.traversal( map );
        }

        return null;
    }
}
