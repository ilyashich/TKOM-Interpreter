package parser.expressions;

import java.util.ArrayList;

public class FunctionCallExpression extends Expression
{
    public String id;
    public ArrayList<Expression> parameters;

    public FunctionCallExpression(String id, ArrayList<Expression> parameters)
    {
        this.id = id;
        this.parameters = parameters;
    }
}
