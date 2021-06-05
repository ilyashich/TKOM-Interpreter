package parser.statements;

import interpreter.Scope;
import parser.expressions.Expression;

public class IfStatement extends Statement
{
    private final Expression condition;
    private final StatementBlock ifBlock;
    private final StatementBlock elseBlock;

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

    @Override
    public Object execute(Scope scope) throws Exception
    {

        if((Boolean) condition.evaluate(scope))
        {
            //wykonywanie statmentów
            for (Statement statement : ifBlock.statements)
            {
                if(statement instanceof ReturnStatement)
                {
                    return statement;
                }
                statement.execute(scope);
            }
        }
        else
        {
            if(elseBlock != null)
            {
                //wykonywanie statmentów
                for (Statement statement : elseBlock.statements)
                {
                    if(statement instanceof ReturnStatement)
                    {
                        return statement;
                    }
                    statement.execute(scope);
                }
            }
        }
        return null;
    }
}
