package parser.statements;

import interpreter.Scope;
import parser.expressions.Expression;

public class WhileStatement extends Statement
{
    public Expression condition;
    public StatementBlock body;

    public WhileStatement(Expression logicExpression, StatementBlock statementBlock)
    {
        this.condition = logicExpression;
        this.body = statementBlock;
    }

    public Expression getCondition()
    {
        return condition;
    }

    public StatementBlock getBody()
    {
        return body;
    }

    @Override
    public Object execute(Scope scope) throws Exception
    {

        while((Boolean) condition.evaluate(scope))
        {
            for (Statement statement : body.statements)
            {
                if(statement instanceof ReturnStatement)
                {
                    return statement;
                }
                statement.execute(scope);
            }
        }
        return null;

    }

}
