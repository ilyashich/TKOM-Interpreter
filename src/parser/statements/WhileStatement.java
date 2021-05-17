package parser.statements;

import parser.expressions.LogicExpression;

public class WhileStatement extends Statement
{
    public LogicExpression condition;
    public StatementBlock body;

    public WhileStatement(LogicExpression logicExpression, StatementBlock statementBlock)
    {
        this.condition = logicExpression;
        this.body = statementBlock;
    }

    public LogicExpression getCondition()
    {
        return condition;
    }

    public StatementBlock getBody()
    {
        return body;
    }

}
