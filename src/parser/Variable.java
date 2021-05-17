package parser;

import lexer.TokenType;

public class Variable extends Value
{
    public Identifier identifier;
    public TokenType complexAttr;

    public Variable(Identifier identifier, TokenType complexAttr)
    {
        this.identifier = identifier;
        this.complexAttr = complexAttr;
    }

    public Variable(Identifier identifier)
    {
        this.identifier = identifier;
    }
}