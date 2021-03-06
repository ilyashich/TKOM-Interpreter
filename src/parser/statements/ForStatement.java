package parser.statements;

import interpreter.Scope;
import parser.expressions.Expression;

import javax.swing.plaf.nimbus.State;

public class ForStatement extends Statement
{
    public Statement identifier;
    public Expression logicExpression;
    public Statement increment;
    public StatementBlock statementBlock;

    public ForStatement(Statement identifier, Expression logicExpression, Statement increment, StatementBlock statementBlock)
    {
        this.identifier = identifier;
        this.logicExpression = logicExpression;
        this.increment = increment;
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

    public Statement getIncrementValue()
    {
        return increment;
    }

    public StatementBlock getStatementBlock()
    {
        return statementBlock;
    }

    @Override
    public Object execute(Scope scope) throws Exception
    {
        Object id = identifier.execute(scope);
        String iteratorName = ((AssignStatement)identifier).identifier;
        while((Boolean) logicExpression.evaluate(scope))
        {
            for (Statement statement : statementBlock.statements)
            {
                if(statement instanceof ReturnStatement)
                {
                    return statement;
                }
                statement.execute(scope);
            }
            Object incr = increment.execute(scope);
        }
        return null;
    }
}
