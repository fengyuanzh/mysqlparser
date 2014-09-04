package mysql.lexer;

public class Number extends Token
{
    public final int value;

    public Number( int v )
    {
        super( Tag.NUM );
        value = v;
    }

    public String toString()
    {
        return "" + value;
    }

}
