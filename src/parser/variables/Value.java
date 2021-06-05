package parser.variables;

import interpreter.Scope;
import lexer.Token;
import lexer.TokenType;
import parser.expressions.Expression;

public class Value extends Expression
{
    public TokenType tokenType;
    public Token token;
    public Object value;

    public Value(TokenType tokenType, Object value)
    {
        this.tokenType = tokenType;
        this.value = value;
    }

    public Value(Token token, Object value)
    {
        this.tokenType = token.getType();
        this.token = token;
        this.value = value;
    }

    @Override
    public String toString()
    {
        return  value.toString();
    }

    @Override
    public Object evaluate(Scope scope) throws Exception
    {
        return value;
    }
}
