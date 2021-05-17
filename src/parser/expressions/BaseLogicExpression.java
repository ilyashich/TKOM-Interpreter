package parser.expressions;

public class BaseLogicExpression
{
    public boolean negate;
    public MathExpression mathExpression;
    public BaseLogicExpression(boolean negate, MathExpression mathExpression)
    {
        this.negate = negate;
        this.mathExpression = mathExpression;
    }
}
