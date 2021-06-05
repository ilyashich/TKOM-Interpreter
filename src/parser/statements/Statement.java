package parser.statements;

import interpreter.Scope;

public abstract class Statement
{
    public abstract Object execute(Scope scope) throws Exception;
}
