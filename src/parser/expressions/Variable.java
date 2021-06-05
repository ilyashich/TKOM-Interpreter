package parser.expressions;

import lexer.TokenType;
import interpreter.Scope;
import parser.statements.AssignStatement;

public class Variable extends Expression
{
    public String name;
    public TokenType type;
    public Object value;

    public Variable(String name, TokenType type)
    {
        this.name = name;
        this.type = type;

    }
    public Variable(String name)
    {
        this.name = name;

    }
    public Variable(AssignStatement assignStatement)
    {
        this.name = assignStatement.identifier;
//        if(assignStatement.rhs instanceof Value)
//            this.type = ((Value) assignStatement.rhs).tokenType;
//        if(assignStatement.rhs instanceof Identifier)
//            this.type = ((Identifier) assignStatement.rhs).token.getType();
//        if(assignStatement.rhs instanceof FunctionCallExpression)
//            this.type = TokenType.NUMBER;
        this.value = null;
    }


    @Override
    public Object evaluate(Scope scope)
    {
        return null;
    }
}