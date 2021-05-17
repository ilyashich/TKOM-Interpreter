package parser.statements;

import lexer.TokenType;
import parser.Identifier;
import parser.expressions.LogicExpression;

public class AssignStatement extends Statement
{
    public TokenType complexField;
    public Identifier identifier;
    public LogicExpression rhs;


    public AssignStatement(Identifier identifier, LogicExpression rhs)
    {
        this.identifier = identifier;
        this.rhs = rhs;
    }

    public AssignStatement(Identifier identifier, LogicExpression rhs, TokenType field)
    {
        this.identifier = identifier;
        this.rhs = rhs;
        this.complexField = field;
    }

    public TokenType getComplexField()
    {
        return complexField;
    }

    public Identifier getIdentifier()
    {
        return identifier;
    }

    public LogicExpression getRhs()
    {
        return rhs;
    }
}
