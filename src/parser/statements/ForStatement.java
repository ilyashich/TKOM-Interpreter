package parser.statements;

import interpreter.Scope;
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
            Object iterValue = (int)scope.getVar(iteratorName).value + incrementValue;
            scope.setVarValue(iteratorName, iterValue);
        }
        return null;
    }
}
