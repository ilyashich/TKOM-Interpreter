package test;

import lexer.Lexer;
import lexer.Token;
import lexer.TokenType;
import org.junit.Assert;
import org.junit.Test;
import source.Position;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class LexerTest
{
    public ArrayList<Token> getTokens(InputStream input) throws Exception
    {
        Lexer lexer = new Lexer(input);
        ArrayList<Token> tokens = new ArrayList<>();
        Token token = lexer.getNext();
        tokens.add(token);
        while(token.getType() != TokenType.EOF)
        {
            token = lexer.getNext();
            tokens.add(token);
        }
        if((tokens.get(tokens.size()-1).getType() != TokenType.EOF))
            tokens.add(token);
        return tokens;
    }
    @Test
    public void fromFile() throws Exception
    {
        File file = new File("src\\test\\test.txt");
        InputStream input = new FileInputStream(file);
        ArrayList<Token> tokens = getTokens(input);

        Position position = new Position(3, 12);
        int i = 0;
        Assert.assertEquals(TokenType.FUNCTION, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.TEXT, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.COMMENT, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.IDENTIFIER, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.ASSIGNMENT, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.NUMBER, tokens.get(i).getType());
        Assert.assertEquals(position.column, tokens.get(i).getPosition().column);
        Assert.assertEquals(position.line, tokens.get(i).getPosition().line);
        ++i;
        Assert.assertEquals(TokenType.RETURN, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.NOT_EQUAL, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.NUMBER, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.IF, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.IDENTIFIER, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.MINUS, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.NUMBER, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.WHILE, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.LEFT_BRACKET, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.RIGHT_BRACKET, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.FOR, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.LEFT_CURLY_BRACKET, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.IDENTIFIER, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.ASSIGNMENT, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.NUMBER, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.RIGHT_CURLY_BRACKET, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.UNKNOWN, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.EOF, tokens.get(i).getType());

    }
    @Test
    public void comment() throws Exception
    {
        String comment = "//kopn";
        InputStream input = new ByteArrayInputStream(comment.getBytes(StandardCharsets.UTF_8));
        ArrayList<Token> tokens = getTokens(input);
        Assert.assertEquals(TokenType.COMMENT, tokens.get(0).getType());
    }
    @Test
    public void number() throws Exception
    {
        String numbers = "25000025 000000025 02 0";
        InputStream input = new ByteArrayInputStream(numbers.getBytes(StandardCharsets.UTF_8));
        ArrayList<Token> tokens = getTokens(input);
        Assert.assertEquals(TokenType.NUMBER, tokens.get(0).getType());
        Assert.assertEquals(TokenType.UNKNOWN, tokens.get(1).getType());
        Assert.assertEquals(TokenType.UNKNOWN, tokens.get(2).getType());
        Assert.assertEquals(TokenType.NUMBER, tokens.get(3).getType());
    }
    @Test
    public void consString() throws Exception
    {
        String text = "\"kopnASLKJFDKLSAJDF8782R7028R08WELFHWFYAP8WEFF\"   \"Hello\"";
        InputStream input = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        ArrayList<Token> tokens = getTokens(input);
        Assert.assertEquals(TokenType.TEXT, tokens.get(0).getType());
        Assert.assertEquals("Hello", tokens.get(1).getStringValue());
        Assert.assertEquals(TokenType.EOF, tokens.get(2).getType());
    }
    @Test
    public void operator() throws Exception
    {
        String operators = "+*/-= ==>=<=<>!!= && ||";
        InputStream input = new ByteArrayInputStream(operators.getBytes(StandardCharsets.UTF_8));
        ArrayList<Token> tokens = getTokens(input);
        int i = 0;
        Assert.assertEquals(TokenType.PLUS, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.MULTIPLY, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.SLASH, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.MINUS, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.ASSIGNMENT, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.EQUALS, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.GREATER_EQUALS, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.LESS_EQUALS, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.LESS, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.GREATER, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.NOT, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.NOT_EQUAL, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.AND, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.OR, tokens.get(i).getType());
    }
    @Test
    public void identifier() throws Exception
    {
        String identifier = "_1ident_1_";
        InputStream input = new ByteArrayInputStream(identifier.getBytes(StandardCharsets.UTF_8));
        ArrayList<Token> tokens = getTokens(input);
        Assert.assertEquals(TokenType.IDENTIFIER, tokens.get(0).getType());
    }
    @Test
    public void keyWord() throws Exception
    {
        String keywords = "import function if else while for return";
        InputStream input = new ByteArrayInputStream(keywords.getBytes(StandardCharsets.UTF_8));
        ArrayList<Token> tokens = getTokens(input);
        int i = 0;
        Assert.assertEquals(TokenType.IMPORT, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.FUNCTION, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.IF, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.ELSE, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.WHILE, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.FOR, tokens.get(i++).getType());
        Assert.assertEquals(TokenType.RETURN, tokens.get(i).getType());
    }
    @Test
    public void brackets() throws Exception
    {
        String brackets = "}{ (  )";
        InputStream input = new ByteArrayInputStream(brackets.getBytes(StandardCharsets.UTF_8));
        ArrayList<Token> tokens = getTokens(input);
        Assert.assertEquals(TokenType.RIGHT_CURLY_BRACKET, tokens.get(0).getType());
        Assert.assertEquals('}', tokens.get(0).getIntValue());
        Assert.assertEquals(TokenType.LEFT_CURLY_BRACKET, tokens.get(1).getType());
        Assert.assertEquals(TokenType.LEFT_BRACKET, tokens.get(2).getType());
        Assert.assertEquals(TokenType.RIGHT_BRACKET, tokens.get(3).getType());
    }
    @Test
    public void eof() throws Exception
    {
        String empty = "";
        InputStream input = new ByteArrayInputStream(empty.getBytes(StandardCharsets.UTF_8));
        ArrayList<Token> tokens = getTokens(input);
        Assert.assertEquals(TokenType.EOF, tokens.get(0).getType());
    }
    @Test
    public void complexFields() throws Exception
    {
        String empty = "real  imag";
        InputStream input = new ByteArrayInputStream(empty.getBytes(StandardCharsets.UTF_8));
        ArrayList<Token> tokens = getTokens(input);
        Assert.assertEquals(TokenType.COMPLEX_REAL_PART, tokens.get(0).getType());
        Assert.assertEquals(TokenType.COMPLEX_IMAGINARY_PART, tokens.get(1).getType());
    }
}
