package parser.statements;

import parser.expressions.Expression;
import parser.expressions.LogicExpression;

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

}
