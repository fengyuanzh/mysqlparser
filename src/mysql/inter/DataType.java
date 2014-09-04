package mysql.inter;

public enum DataType {

    DOUBLE , INTEGER , DATETIME , BOOL , TEXT , VARCHAR, CHAR, DATE, BIGINT, BLOB;
    
    DataType(){};

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
