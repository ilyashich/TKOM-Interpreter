package parser.statements;

import parser.expressions.LogicExpression;

public class IfStatement extends Statement
{
    private LogicExpression condition;
    private StatementBlock ifBlock;
    private StatementBlock elseBlock;

    public IfStatement(LogicExpression condition, StatementBlock ifBlock, StatementBlock elseBlock)
    {
        this.condition = condition;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    public LogicExpression getCondition()
    {
        return condition;
    }

    public StatementBlock getIfBlock()
    {
        return ifBlock;
    }

    public StatementBlock getElseBlock()
    {
        return elseBlock;
    }
}
