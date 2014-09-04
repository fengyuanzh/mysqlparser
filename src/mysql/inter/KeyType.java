package mysql.inter;

public enum KeyType {

    PRIMARY_KEY , UNIQUE , UNIQUE_KEY , KEY ;
    
    public String getName()
    {
    	switch (this) {
		case PRIMARY_KEY:
			return "PRIMARY KEY";
		case UNIQUE:
			return "UNIQUE";
		case UNIQUE_KEY:
			return "UNIQUE KEY";
		case KEY:
			return "KEY";
		default:
			throw new UnsupportedOperationException();
		}
    }
}
