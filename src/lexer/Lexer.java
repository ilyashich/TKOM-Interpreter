package lexer;

import exceptions.IdentifierIsTooLargeException;
import exceptions.IntegerIsTooBigException;
import exceptions.StringIsTooLargeException;
import source.Source;
import source.Position;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Lexer
{
    public int MAX_STRING_LENGTH = 100;
    private final Source src;
    private Position textPosition;
    private int currentChar;
    private StringBuilder identifier;
    public static final HashMap<String, TokenType> tokens;
    static
    {
        tokens = new HashMap<>();
        tokens.put("import", TokenType.IMPORT);
        tokens.put("function", TokenType.FUNCTION);
        tokens.put("return", TokenType.RETURN);
        tokens.put("if", TokenType.IF);
        tokens.put("else", TokenType.ELSE);
        tokens.put("for", TokenType.FOR);
        tokens.put("while", TokenType.WHILE);
        tokens.put("(", TokenType.LEFT_BRACKET);
        tokens.put(")", TokenType.RIGHT_BRACKET);
        tokens.put("{", TokenType.LEFT_CURLY_BRACKET);
        tokens.put("}", TokenType.RIGHT_CURLY_BRACKET);
        tokens.put("=", TokenType.ASSIGNMENT);
        tokens.put("==", TokenType.EQUALS);
        tokens.put(">", TokenType.GREATER);
        tokens.put("<", TokenType.LESS);
        tokens.put(">=", TokenType.GREATER_EQUALS);
        tokens.put("<=", TokenType.LESS_EQUALS);
        tokens.put("!=", TokenType.NOT_EQUAL);
        tokens.put(",", TokenType.COMMA);
        tokens.put(".", TokenType.DOT);
        tokens.put(";", TokenType.SEMICOLON);
        tokens.put("/", TokenType.SLASH);
        tokens.put("\\", TokenType.BACKSLASH);
        tokens.put("!", TokenType.NOT);
        tokens.put("+", TokenType.PLUS);
        tokens.put("-", TokenType.MINUS);
        tokens.put("*", TokenType.MULTIPLY);
        tokens.put("&&", TokenType.AND);
        tokens.put("||", TokenType.OR);
        tokens.put("real", TokenType.COMPLEX_REAL_PART);
        tokens.put("imag", TokenType.COMPLEX_IMAGINARY_PART);

    }

    public Lexer(InputStream istream) throws IOException
    {
        src = new Source(istream);
        currentChar = src.getNext();
    }

    private void nextChar() throws IOException
    {
        currentChar = src.getNext();
    }

    private void skipWhites() throws IOException
    {
        do {
            if (currentChar == Source.EOF) break;
            while (Character.isWhitespace(currentChar))
                nextChar();
        } while (Character.isWhitespace(currentChar) || currentChar == Source.EOF);
    }

    private void skipUnknown() throws IOException
    {
        while(!Character.isWhitespace(currentChar))
            nextChar();
    }


    public Token getNext() throws Exception
    {
        skipWhites();
        textPosition = new Position(src.getPosition());
        if(currentChar == Source.EOF)
            return new Token("", TokenType.EOF, textPosition);
        Token token;
        token = buildComment();
        if(token != null) return token;
        token = buildNumber();
        if(token != null) return token;
        token = buildConstString();
        if(token != null) return token;
        token = buildIdentifierOrKeyword();
        if(token != null) return token;
        token = buildOperator();
        if(token != null) return token;
        token = new Token(currentChar, TokenType.UNKNOWN, textPosition);
        skipUnknown();
        return token;

    }

    private Token buildNumber() throws IOException, IntegerIsTooBigException
    {
        identifier = new StringBuilder();
        long val = 0;
        if(Character.isDigit(currentChar))
        {
            while(Character.isDigit(currentChar))
            {
                val = val * 10 + (currentChar - '0');
                identifier.append((char)currentChar);
                nextChar();
            }
            if(val > Integer.MAX_VALUE)
                throw new IntegerIsTooBigException(textPosition);
            int value = Math.toIntExact(val);
            if(identifier.charAt(0) == '0' && identifier.length() > 1)
                return new Token(identifier.toString(), TokenType.UNKNOWN, textPosition);
            else
                return new Token(value, TokenType.NUMBER, textPosition);
        }
        return null;
    }

    private Token buildConstString() throws IOException, StringIsTooLargeException
    {
        identifier = new StringBuilder();
        if(currentChar == '"')
        {
            nextChar();
            do
            {
                if(identifier.length() < MAX_STRING_LENGTH)
                {
                    identifier.append((char) currentChar);
                }
                else
                    throw new StringIsTooLargeException(textPosition);
                nextChar();
            }while (currentChar != '"');
            nextChar();
            String text = identifier.toString();
            return new Token(text, TokenType.TEXT, textPosition);
        }
        return null;
    }

    private Token buildIdentifierOrKeyword() throws IOException, IdentifierIsTooLargeException
    {
        identifier = new StringBuilder();
        if(Character.isLetter(currentChar) || currentChar == '_')
        {

            do{
                if(identifier.length() <= MAX_STRING_LENGTH)
                {
                    identifier.append((char) currentChar);
                }
                else throw new IdentifierIsTooLargeException(textPosition);
                nextChar();
            }while(Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_');
            String text = identifier.toString();
            if(text.equals("_"))
            {
                return new Token("_", TokenType.UNKNOWN, textPosition);
            }
            return new Token(text, tokens.getOrDefault(identifier.toString(), TokenType.IDENTIFIER), textPosition);
        }
        return null;

    }

    private Token buildOperator() throws IOException
    {
        int value = currentChar;
        switch (currentChar)
        {
            case '(':
                nextChar();
                return new Token(value,TokenType.LEFT_BRACKET, textPosition);
            case ')':
                nextChar();
                return new Token(value,TokenType.RIGHT_BRACKET, textPosition);
            case '{':
                nextChar();
                return new Token(value,TokenType.LEFT_CURLY_BRACKET, textPosition);
            case '}':
                nextChar();
                return new Token(value,TokenType.RIGHT_CURLY_BRACKET, textPosition);
            case '=':
                nextChar();
                if (currentChar == '=')
                {
                    nextChar();
                    return new Token("==", TokenType.EQUALS, textPosition);
                }
                else
                    return new Token(value,TokenType.ASSIGNMENT, textPosition);
            case '>':
                nextChar();
                if (currentChar == '=')
                {
                    nextChar();
                    return new Token(">=", TokenType.GREATER_EQUALS, textPosition);
                }
                else
                    return new Token(value,TokenType.GREATER, textPosition);
            case '<':
                nextChar();
                if (currentChar == '=')
                {
                    nextChar();
                    return new Token("<=", TokenType.LESS_EQUALS, textPosition);
                }
                else
                    return new Token(value,TokenType.LESS, textPosition);
            case '!':
                nextChar();
                if (currentChar == '=')
                {
                    nextChar();
                    return new Token("!=", TokenType.NOT_EQUAL, textPosition);
                }
                else
                    return new Token(value,TokenType.NOT, textPosition);
            case '&':
                nextChar();
                if (currentChar == '&')
                {
                    nextChar();
                    return new Token("&&", TokenType.AND, textPosition);
                }
                else
                    return new Token(value,TokenType.UNKNOWN, textPosition);
            case '+':
                nextChar();
                return new Token(value,TokenType.PLUS, textPosition);
            case '-':
                nextChar();
                return new Token(value,TokenType.MINUS, textPosition);
            case '*':
                nextChar();
                return new Token(value,TokenType.MULTIPLY, textPosition);
            case '/':
                nextChar();
                return new Token(value,TokenType.SLASH, textPosition);
            case '\\':
                nextChar();
                return new Token(value,TokenType.BACKSLASH, textPosition);
            case '|':
                nextChar();
                if (currentChar == '|')
                {
                    nextChar();
                    return new Token("||", TokenType.OR, textPosition);
                }
                else
                    return new Token(value,TokenType.UNKNOWN, textPosition);
            case '.':
                nextChar();
                return new Token(value,TokenType.DOT, textPosition);
            case ',':
                nextChar();
                return new Token(value,TokenType.COMMA, textPosition);
            case ';':
                nextChar();
                return new Token(value,TokenType.SEMICOLON, textPosition);
            default:
                return null;

        }
    }

    private Token buildComment() throws IOException
    {
        identifier = new StringBuilder();
        if(currentChar == '/')
        {
            nextChar();
            if(currentChar == '/')
            {
                nextChar();
                do{
                  if(identifier.length() <= MAX_STRING_LENGTH)
                    identifier.append((char)currentChar);
                  nextChar();
                }while(currentChar != Source.NEW_LINE_ASCII && currentChar != Source.EOF);
                String comment = identifier.toString();
                return new Token(comment, TokenType.COMMENT, textPosition);
            }
            return new Token('/', TokenType.SLASH, textPosition);
        }
        return null;
    }


}
