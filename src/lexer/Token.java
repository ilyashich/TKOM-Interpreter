package lexer;

import source.Position;

public class Token
{
    private final TokenType type;
    private String stringValue;
    private int intValue;
    private final Position position;
    private enum Value{STRING, NUMBER}
    private final Value val;

    public Token(String value, TokenType type, Position position)
    {
        this.stringValue = value;
        this.position = position;
        this.type = type;
        this.val = Value.STRING;
    }

    public Token(int value, TokenType type, Position position)
    {
        this.intValue = value;
        this.position = position;
        this.type = type;
        this.val = Value.NUMBER;
    }

    @Override
    public String toString()
    {
        String temp = "";
        if(val == Value.STRING)
            temp = stringValue;
        if(val == Value.NUMBER)
            temp = Integer.toString(intValue);
        return "Value: " + temp + ", Token type: " + type
                + ", Line: " + position.line + ", Column: " + position.column;
    }

    public TokenType getType()
    {
        return type;
    }

    public String getStringValue()
    {
        return stringValue;
    }

    public int getIntValue()
    {
        return intValue;
    }

    public Position getPosition()
    {
        return position;
    }
}
