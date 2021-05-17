package parser.statements;

import parser.Identifier;
import parser.Value;
import parser.expressions.LogicExpression;

import java.util.ArrayList;

public class FunctionCall extends Value
{
    public Identifier id;
    public ArrayList<LogicExpression> params;
    public FunctionCall(Identifier id, ArrayList<LogicExpression> params)
    {
        this.id = id;
        this.params = params;
    }

    public Identifier getId()
    {
        return id;
    }

    public ArrayList<LogicExpression> getParams()
    {
        return params;
    }
}
