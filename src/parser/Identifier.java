package parser;

import lexer.Token;
import lexer.TokenType;
import parser.expressions.Expression;

public class Identifier extends Expression
{
    public String name;
    public Token token;
    public TokenType field;

    public Identifier(Token token)
    {
        this.name = token.getStringValue();
        this.token = token;
    }

    public Identifier(Token token, TokenType field)
    {
        this.name = token.getStringValue();
        this.token = token;
        this.field = field;
    }
}