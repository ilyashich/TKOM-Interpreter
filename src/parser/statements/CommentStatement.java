package parser.statements;

import interpreter.Scope;

public class CommentStatement extends Statement
{
    public String comment;

    public CommentStatement(String comment)
    {
        this.comment = comment;
    }

    @Override
    public Object execute(Scope scope) throws Exception
    {
        return null;
    }
}
