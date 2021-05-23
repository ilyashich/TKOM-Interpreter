package parser.statements;

import lexer.TokenType;
import parser.expressions.Expression;

public class AssignStatement extends Statement
{
    public TokenType complexField;
    public String identifier;
    public Expression rhs;


    public AssignStatement(String identifier, Expression rhs)
    {
        this.identifier = identifier;
        this.rhs = rhs;
    }

    public AssignStatement(String identifier, Expression rhs, TokenType field)
    {
        this.identifier = identifier;
        this.rhs = rhs;
        this.complexField = field;
    }
}
