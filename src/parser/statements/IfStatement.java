package parser.statements;

import parser.expressions.Expression;
import parser.expressions.LogicExpression;

public class IfStatement extends Statement
{
    private Expression condition;
    private StatementBlock ifBlock;
    private StatementBlock elseBlock;

    public IfStatement(Expression condition, StatementBlock ifBlock, StatementBlock elseBlock)
    {
        this.condition = condition;
        this.ifBlock = ifBlock;
        this.elseBlock = elseBlock;
    }

    public Expression getCondition()
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
