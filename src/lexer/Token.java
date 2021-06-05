package lexer;

import source.Position;

import java.math.BigDecimal;

public class Token
{
    private final TokenType type;
    private String stringValue;
    private int intValue;
    private BigDecimal doubleValue;
    private final Position position;
    private enum Value{STRING, NUMBER, FLOAT, UNKNOWN}
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

    public Token(BigDecimal value, TokenType type, Position position)
    {
        this.doubleValue = value;
        this.position = position;
        this.type = type;
        this.val = Value.FLOAT;
    }

    public Token(TokenType type, Position position)
    {
        this.type = type;
        this.position = position;
        this.val = Value.UNKNOWN;
    }

    @Override
    public String toString()
    {
        if(val == Value.UNKNOWN)
            return "Token type: " + type
                    + ", Line: " + position.line + ", Column: " + position.column;

        String temp = "";
        if(val == Value.STRING)
            temp = stringValue;
        if(val == Value.NUMBER)
            temp = Integer.toString(intValue);
        if(val == Value.FLOAT)
            temp = Double.toString(intValue);
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

    public BigDecimal getDoubleValue()
    {
        return doubleValue;
    }

    public Position getPosition()
    {
        return position;
    }
}
