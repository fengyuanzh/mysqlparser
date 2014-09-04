package mysql.lexer;

public class Word extends Token
{
    public String lexeme = "";

    public Word( String s, int tag )
    {
        super( tag );
        lexeme = s;
    }

    public String toString()
    {
        return lexeme;
    }
		
public static final Word
    create_ = new Word( "CREATE", Tag.CREATE ), 
    not_ = new Word( "NOT" , Tag. NOT ),
    varchar_  = new Word ( "VARCHAR", Tag.VARCHAR),
    primary_ = new Word( "PRIMARY" , Tag.PRIMARY ),
    unique_ = new Word( "UNIQUE" , Tag.UNIQUE ),
    key_ = new Word( "KEY" , Tag.KEY ),
    alter_ = new Word( "ALTER" , Tag.ALTER ),
    constraint_ = new Word( "CONSTRAINT" , Tag. CONSTRAINT ),
    add_ = new Word( "ADD" , Tag. ADD ),
    foreign_ = new Word( "FOREIGN" , Tag. FOREIGN ),
    table_ = new Word( "TABLE" , Tag.TABLE ),
    null_ = new Word( "NULL" , Tag.NULL ),
    engine_ = new Word( "ENGINE", Tag.ENGINE ),
    innodb_ = new Word( "INNODB", Tag.INNODB ),
    double_ = new Word( "DOUBLE" , Tag.DOUBLE ),
    integer_ = new Word( "INTEGER" , Tag.INTEGER ),
    datetime_ = new Word( "DATETIME" , Tag.DATETIME ),
    bool_ = new Word( "BOOL" , Tag.BOOL ),
    text_ = new Word( "TEXT" , Tag.TEXT ),
    eq_ = new Word( "=" , Tag.EQ ),
    references_ = new Word( "REFERENCES" , Tag.REFERENCES ),
    default_ = new Word( "DEFAULT" , Tag.DEFAULT ),
    char_ = new Word( "CHAR" , Tag.CHAR ),
    date_ = new Word( "DATE" , Tag.DATE ),
    bigint_ = new Word("BIGINT", Tag.BIGINT),
    auto_increment_ = new Word("AUTO_INCREMENT", Tag.AUTO_INCREMENT),
    blog_ = new Word("BLOB", Tag.BLOB),
	myisam_ = new Word( "MyISAM", Tag.MYISAM ),
    eof_ = new Word( "" , Tag.EOF );

}

