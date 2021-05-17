package parser.statements;

import parser.Identifier;
import parser.expressions.LogicExpression;

public class ForStatement extends Statement
{
    public Identifier identifier;
    public LogicExpression logicExpression;
    public int incrementValue;
    public StatementBlock statementBlock;

    public ForStatement(Identifier identifier, LogicExpression logicExpression, int incrementValue, StatementBlock statementBlock)
    {
        this.identifier = identifier;
        this.logicExpression = logicExpression;
        this.incrementValue = incrementValue;
        this.statementBlock = statementBlock;
    }

    public Identifier getIdentifier()
    {
        return identifier;
    }

    public LogicExpression getLogicExpression()
    {
        return logicExpression;
    }

    public int getIncrementValue()
    {
        return incrementValue;
    }

    public StatementBlock getStatementBlock()
    {
        return statementBlock;
    }
}
