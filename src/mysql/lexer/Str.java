package mysql.lexer;

public class Str extends Token
{
    public final String value;
    
    public Str( String v )
    {
        super( Tag.STRING );
        value = v;
    }

    public String toString()
    {
        return value;
    }

}
