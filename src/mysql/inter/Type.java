package mysql.inter;

public enum Type {

    DOUBLE , INTEGER , DATETIME , BOOL , TEXT , VARCHAR, CHAR, DATE, BIGINT, BLOB;
    
    Type(){};

    @Override
    public String toString()
    {
    	return this.name();
    }
    
    
    public String toString(int charLength)
    {
    	if (this == VARCHAR || this == CHAR )
    	{
    		return this.name() + " " + "(" + charLength + ")";
    	}
    	return this.name();
    }
    
    
}
