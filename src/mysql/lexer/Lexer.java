package mysql.lexer;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.Hashtable;

public class Lexer
{
    PushbackReader pushbackReader = null;

    File file = null;

    BufferedReader reader = null;

    public static int line = 1;

    char peek = ' ';

    Hashtable<String, Token> words = new Hashtable<String, Token>();

    void reserve( Word w )
    {
        words.put( w.lexeme, w );
    }

    public Lexer( String fileName )
    {
        reserve( Word.create_ );
        reserve( Word.not_ );
        reserve( Word.varchar_ );
        reserve( Word.primary_ );
        reserve( Word.unique_ );
        reserve( Word.key_ );
        reserve( Word.alter_ );
        reserve( Word.constraint_ );
        reserve( Word.add_ );
        reserve( Word.foreign_ );
        reserve( Word.table_ );
        reserve( Word.null_ );
        reserve( Word.engine_ );
        reserve( Word.innodb_ );
        reserve( Word.double_ );
        reserve( Word.integer_ );
        reserve( Word.datetime_ );
        reserve( Word.bool_ );
        reserve( Word.text_ );
        reserve( Word.eq_ );
        reserve( Word.references_ );
        reserve( Word.char_ );
        reserve( Word.default_ );
        reserve( Word.date_ );
        reserve( Word.bigint_ );
        reserve( Word.auto_increment_ );
        reserve( Word.blog_ );
		reserve( Word.myisam_ );

        try
        {
            file = new File( fileName );
            reader = new BufferedReader( new FileReader( fileName ) );
            pushbackReader = new PushbackReader( reader );

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    void readch() throws IOException
    {
        // peek = (char) System.in.read();
        peek = (char) pushbackReader.read();
    }

    void pushback( char c )
    {
        try
        {
            pushbackReader.unread( c );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    boolean readch( char c ) throws IOException
    {
        readch();
        if (peek != c)
            return false;
        peek = ' ';
        return true;
    }

    public Token scan() throws IOException
    {
        int state = 0;

        while ( true )
        {
            readch();
            if (peek == '\n')
            {
                line = line + 1;
            }
            if (peek == '#')
            {
                state = 1;
            }
            if (state == 1 && peek == '\n')
            {
                state = 0;
            }
            else if (state == 1)
            {
                continue;
            }

            if (peek == ' ' || peek == '\t')
            {
            }
            else if (peek == '\n' || peek == '\r')
            {
            }
            else
            {
                break;
            }
        }

        switch ( peek )
        {
        case '=':
            return Word.eq_;
        case 65535:
            return Word.eof_;
        }

        if (peek == '\'')
        {
            String v = "";
            do
            {
                v += peek;
                readch();
            }
            while ( peek != '\'' );
            return new Str( v );
        }

        if (Character.isDigit( peek ))
        {
            int v = 0;
            do
            {
                v = 10 * v + Character.digit( peek, 10 );
                readch();
            }
            while ( Character.isDigit( peek ) );

            if (peek != '.')
            {
                pushback( peek );
                return new Number( v );
            }
            float x = v;
            float d = 10;

            for ( ;; )
            {
                readch();
                if (!Character.isDigit( peek ))
                {
                    break;
                }
                x = x + Character.digit( peek, 10 ) / d;
                d = d * 10;
            }
            pushback( peek );
            return new Real( x );
        }

        if (Character.isLetter( peek ))
        {
            StringBuffer b = new StringBuffer();
            do
            {
                b.append( peek );
                readch();
            }
            while ( isValidCharOfID( peek ) );
            pushback( peek );
            String s = b.toString();
            Word w = (Word) words.get( s );
            if (w != null)
            {
                return w;
            }
            w = new Word( s, Tag.ID );
            words.put( s, w );
            return w;
        }

        Token tok = new Token( peek );
        peek = ' ';
        return tok;
    }

    private boolean isValidCharOfID( char c )
    {
        if (c == '_')
        {
            return true;
        }
        else
        {
            return Character.isLetterOrDigit( peek );
        }
    }
}
