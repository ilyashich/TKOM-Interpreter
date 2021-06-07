package parser.statements;

import interpreter.Scope;
import parser.expressions.Expression;

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

    @Override
    public Object execute(Scope scope) throws Exception
    {
        return expression.evaluate(scope);
    }
}
