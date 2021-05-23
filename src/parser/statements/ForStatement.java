package parser.statements;

import parser.expressions.Expression;

public class ForStatement extends Statement
{
    public Statement identifier;
    public Expression logicExpression;
    public int incrementValue;
    public StatementBlock statementBlock;

    public ForStatement(Statement identifier, Expression logicExpression, int incrementValue, StatementBlock statementBlock)
    {
        this.identifier = identifier;
        this.logicExpression = logicExpression;
        this.incrementValue = incrementValue;
        this.statementBlock = statementBlock;
    }

    public Statement getIdentifier()
    {
        return identifier;
    }

    public Expression getLogicExpression()
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
