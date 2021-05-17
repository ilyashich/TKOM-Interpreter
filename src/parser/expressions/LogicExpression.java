package parser.expressions;

import java.util.ArrayList;

public class LogicExpression
{
    public ArrayList<AndExpression> expressions;
    public LogicExpression(ArrayList<AndExpression> expressions)
    {
        this.expressions = expressions;
    }
}
