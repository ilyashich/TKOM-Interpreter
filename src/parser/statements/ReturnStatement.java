package parser.statements;

import parser.expressions.Expression;
import parser.expressions.LogicExpression;

public class ReturnStatement extends Statement
{
    public Expression expression;
    public ReturnStatement(Expression expression)
    {
        this.expression = expression;
    }

    public Expression getExpression()
    {
        return expression;
    }
}
