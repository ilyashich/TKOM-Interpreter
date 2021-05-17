package parser.expressions;

import java.util.ArrayList;

public class AndExpression
{
    public ArrayList<RelationalExpression> expressions;
    public AndExpression(ArrayList<RelationalExpression> expressions)
    {
        this.expressions = expressions;
    }
}
