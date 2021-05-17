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
        tokens.put("Complex", TokenType.COMPLEX);
        tokens.put("real", TokenType.COMPLEX_REAL_PART);
        tokens.put("imag", TokenType.COMPLEX_IMAGINARY_PART);

    }

    public static final HashMap<Character, TokenType> operators;
    static
    {
        operators = new HashMap<>();
        operators.put('(', TokenType.LEFT_BRACKET);
        operators.put(')', TokenType.RIGHT_BRACKET);
        operators.put('{', TokenType.LEFT_CURLY_BRACKET);
        operators.put('}', TokenType.RIGHT_CURLY_BRACKET);
        operators.put(',', TokenType.COMMA);
        operators.put('.', TokenType.DOT);
        operators.put(';', TokenType.SEMICOLON);
        operators.put('\\', TokenType.BACKSLASH);
        operators.put('+', TokenType.PLUS);
        operators.put('-', TokenType.MINUS);
        operators.put('*', TokenType.MULTIPLY);
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
            while (Character.isWhitespace(currentChar))
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
        return token;

    }

    private Token buildNumber() throws IOException, IntegerIsTooBigException
    {
        identifier = new StringBuilder();
        long val = 0;
        if (!Character.isDigit(currentChar))
            return null;

        if (currentChar == '0')
        {
            nextChar();

            if(!Character.isDigit(currentChar))
                return new Token(0, TokenType.NUMBER, textPosition);

            while(Character.isDigit(currentChar))
            {
                nextChar();
            }
            return new Token(TokenType.UNKNOWN, textPosition);
        }
        else
        {
            while (Character.isDigit(currentChar) && val < Integer.MAX_VALUE)
            {
                val = val * 10 + (currentChar - '0');
                nextChar();
            }
        }

        if(val > Integer.MAX_VALUE)
            throw new IntegerIsTooBigException(textPosition);
        else
            return new Token((int) val, TokenType.NUMBER, textPosition);
    }

    private Token buildConstString() throws IOException, StringIsTooLargeException
    {
        if(currentChar == '"')
        {
            identifier = new StringBuilder();

            nextChar();
            do
            {
                if(identifier.length() < MAX_STRING_LENGTH)
                {
                    if(currentChar == '\\')
                    {
                        nextChar();
                        switch (currentChar)
                        {
                            case '"':
                                identifier.append('"');
                                break;
                            case 'n':
                                identifier.append('\n');
                                break;
                            case 't':
                                identifier.append('\t');
                                break;
                            case '\\':
                                identifier.append('\\');
                                break;
                            default:
                                break;
                        }
                    }
                    else
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

        if(Character.isLetter(currentChar) || currentChar == '_')
        {
            identifier = new StringBuilder();

            do{
                if(identifier.length() < MAX_STRING_LENGTH)
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
            case '|':
                nextChar();
                if (currentChar == '|')
                {
                    nextChar();
                    return new Token("||", TokenType.OR, textPosition);
                }
                else
                    return new Token(value,TokenType.UNKNOWN, textPosition);
            default:
                if(operators.containsKey((char)currentChar))
                {
                    int temp = currentChar;
                    nextChar();
                    return new Token(temp, operators.get((char) temp), textPosition);
                }
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
