package parser.statements;

import parser.expressions.LogicExpression;

public class ReturnStatement extends Statement
{
    public LogicExpression expression;
    public ReturnStatement(LogicExpression expression)
    {
        this.expression = expression;
    }

    public LogicExpression getExpression()
    {
        return expression;
    }
}
