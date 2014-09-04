package mysql;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import mysql.inter.*;
import mysql.lexer.*;

/**
 * 
 * Program -> blocks EOF | EOF
 * blocks -> block, blocks
 * CreateTable -> CREATE TABLE id ( Properties ) [ ENGINE = INNODB ];
 * Properties -> Properties, Property
 * Property -> Declare
 *             | PrimaryKey
 *             | UniqueKey
 *             | Key
 * Declare ->
 *             | id Type NOT NULL DEFAULT String
 *             | id Type NOT NULL DEFAULT Number
 *             | id Type NOT NULL
 *             | id Type
 * PrimaryKey -> [ CONSTRAINT id ] PRIMARY KEY ( ids )
 * ids -> ids, id
 * UniqueKey -> UNIQUE Key
 * Key ->    KEY ( ids )
 * Type -> DOUBLE | INTEGER | DATETIME | BOOL | TEXT | VARCHAR ( Nmuber )
 * Number ->
 * AlterTable ->
 * @author Administrator
 *
 */
public class Parser
{

    public static void main( String[] args ) throws IOException
    {
        LinkedHashMap<String, Block> sourceMap = new LinkedHashMap<String, Block>();
        LinkedHashMap<String, Block> targetMap = new LinkedHashMap<String, Block>();

        Parser sourceParser = new Parser( new Lexer( args[0] ) );
        sourceParser.program().traversal( sourceMap );

        Parser targetParser = new Parser( new Lexer( args[1] ) );
        targetParser.program().traversal( targetMap );

        BufferedWriter out = new BufferedWriter( new FileWriter( args[2], false ) );

        for ( Block sourceBlock : sourceMap.values() )
        {
            Block found = targetMap.get( sourceBlock.getKeyName() );
            if (found == null)
            {
                System.out.println("drop");
                sourceBlock.drop( out );
            }
        }

        for ( Block targetBlock : targetMap.values() )
        {
            Block found = sourceMap.get( targetBlock.getKeyName() );
            if (found == null)
            {
                targetBlock.add( out );
            }
            else
            {
                targetBlock.diff( out, found );
            }

        }

        out.flush();
        out.close();
    }

    private Lexer lex; 

    private Token look; // lookahead

    public Parser( Lexer l ) throws IOException
    {
        lex = l;
        move();
    }

    void move() throws IOException
    {
        look = lex.scan();
    }

    void error( String s )
    {
        throw new Error( "near line: " + Lexer.line + " " + s );
    }

    void match( int t ) throws IOException
    {
        if (look.tag == t)
            move();
        else
            error( "token: " + look );
    }

    boolean test( int t )
    {
        return look.tag == t;
    }

    public Block program() throws IOException
    { // program -> block
        if (test( Tag.EOF ))
        {
            return null;
        }
        else
        {
            return blocks();
        }
    }

    public Block blocks() throws IOException
    {
        Blocks blocks = null;
        if (test( Tag.EOF ))
        {
            return blocks;
        }
        else
        {
            Block b1 = block();
            Block b2 = blocks();
            return new Blocks( b1, b2 );
        }

    }

    Block block() throws IOException
    { // block -> ( decls stmts 3
		String engineType = null;
        if (test( Tag.CREATE ))
        {
            move();
            match( Tag.TABLE );
            Token id = look;
            move();
            match( '(' );
            Stmt s = stmts();
            match( ')' );
            if (test( Tag.ENGINE ))
            {
                move();
                match( Tag.EQ );
				if (test( Tag.INNODB ))
				{
					engineType = "INNODB";
					move();
				}
				else if (test( Tag.MYISAM ))
				{
					engineType = "MyISAM";
					move();
				}
            }
            match( ';' );
            return new CreateTable( id.toString(), engineType, s );
        }
        else
        {
            test( Tag.ALTER );
            move();
            match( Tag.TABLE );
            String tableName = id();
            match( Tag.ADD );
            String constraintName = null;
            if (test( Tag.CONSTRAINT ))
            {
                move();
                constraintName = id();
            }

            if (test( Tag.FOREIGN ))
            {
                move();
                match( Tag.KEY );
                match( '(' );
                List<String> localKeys = ids();
                match( ')' );

                match( Tag.REFERENCES );
                String foreignTable = id();
                match( '(' );
                List<String> foreignKeys = ids();
                match( ')' );

                match( ';' );
                return new AddForeignKey( tableName, foreignTable, constraintName, localKeys, foreignKeys );
            }
            else if (test( Tag.UNIQUE ))
            {
                move();
                match( '(' );
                List<String> localKeys = ids();
                match( ')' );

                match( ';' );
                return new AddUniqueKey( tableName, constraintName, localKeys );
            }
            return null;

        }

    }

    Stmt stmts() throws IOException
    {
        return new Stmts( stmt(), restStmts() );
    }

    Stmt restStmts() throws IOException
    {
        if (test( ',' ))
        {
            move();
            return stmts();
        }
        else if (test( ')' ))
        {
            return null;
        }
        else
        {
            error( "token: " + look );
        }
        return null;

    }

    Stmt stmt() throws IOException
    {
        // Expr x; Stmt s, sl, s2;
        // Stmt savedstmt ; // save enclosing loop for breaks

        if (test( Tag.ID ))
        {
            DataType type = null;
            String defaultValue = null;
            Token name = look;
            boolean nullable = true;
            int charLength = 0;
            move();
            if (test( Tag.DOUBLE ))
            {
                move();
                type = DataType.DOUBLE;
            }
            else if (test( Tag.INTEGER ))
            {
                move();
                type = DataType.INTEGER;
            }
            else if (test( Tag.BIGINT ))
            {
                move();
                type = DataType.BIGINT;
            }
            else if (test( Tag.DATETIME ))
            {
                move();
                type = DataType.DATETIME;
            }
            else if (test( Tag.DATE ))
            {
                move();
                type = DataType.DATE;
            }
            else if (test( Tag.BOOL ))
            {
                move();
                type = DataType.BOOL;
            }
            else if (test( Tag.TEXT ))
            {
                move();
                type = DataType.TEXT;
            }
            else if (test( Tag.BLOB ))
            {
                move();
                type = DataType.BLOB;
            }
            else if (test( Tag.VARCHAR ))
            {
                type = DataType.VARCHAR;
                move();
                match( '(' );
                if (test( Tag.NUM ))
                {
                    Token num = look;
                    move();
                    charLength = Integer.parseInt( num.toString() );
                }
                match( ')' );
            }
            else if (test( Tag.CHAR ))
            {
                type = DataType.CHAR;
                move();
                match( '(' );
                if (test( Tag.NUM ))
                {
                    Token num = look;
                    move();
                    charLength = Integer.parseInt( num.toString() );
                }
                match( ')' );
            }

            if (test( Tag.NOT ))
            {
                move();
                match( Tag.NULL );
                nullable = false;
            }

            if (test( Tag.DEFAULT ))
            {
                move();
                defaultValue = look.toString();
                move();
            }
            else if (test( Tag.AUTO_INCREMENT ))
            {
                move();
            }
            return new Declare( name.toString(), type, nullable, defaultValue, charLength );

        }
        else
        {
            String keyName = null;
            String constraintName = null;
            KeyType keyType = null;
            List<String> ids = null;
            if (test( Tag.CONSTRAINT ))
            {
                move();
                test( Tag.ID );
                constraintName = look.toString();
                move();
            }
            if (test( Tag.PRIMARY ))
            {
                move();
                match( Tag.KEY );
                keyType = KeyType.PRIMARY_KEY;
            }

            if (test( Tag.UNIQUE ))
            {
                move();
                if (test( Tag.KEY ))
                {
                    move();
                    keyType = KeyType.UNIQUE_KEY;
                }
                else
                {
                    keyType = KeyType.UNIQUE;
                }

            }
            if (test( Tag.KEY ))
            {
                move();
                keyType = KeyType.KEY;
            }

            if (test( Tag.ID ))
            {
                Token name = look;
                move();
                keyName = name.toString();

            }

            match( '(' );
            {
                ids = ids();
            }
            match( ')' );

            return new Key( constraintName, keyName, keyType, ids );
        }

    }

    public List<String> ids() throws IOException
    {
        List<String> ids = new ArrayList<String>();
        ids.add( id() );
        ids.addAll( restIDs() );
        return ids;
    }

    public String id() throws IOException
    {
        Token t = look;
        {
            match( Tag.ID );
        }
        return t.toString();
    }

    public List<String> restIDs() throws IOException
    {
        if (test( ')' ))
        {
            return new ArrayList<String>();
        }
        match( ',' );
        return ids();

    }

}