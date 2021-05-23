package parser.statements;

import parser.Value;
import parser.expressions.Expression;
import parser.expressions.LogicExpression;

import java.util.ArrayList;

public class FunctionCallStatement extends Statement
{
    public String id;
    public ArrayList<Expression> params;

    public FunctionCallStatement(String id, ArrayList<Expression> params)
    {
        this.id = id;
        this.params = params;
    }
}
