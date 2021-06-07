package parser.expressions;

import interpreter.Scope;

public abstract class Expression
{
    public abstract Object evaluate(Scope scope) throws Exception;
}
