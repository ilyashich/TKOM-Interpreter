package parser;

import lexer.TokenType;
import parser.expressions.Expression;

public class Value extends Expression
{
    public TokenType tokenType;
    public Object value;

    public Value(TokenType tokenType, Object value)
    {
        this.tokenType = tokenType;
        this.value = value;
    }

    @Override
    public String toString()
    {
        return  value.toString();
    }
}
