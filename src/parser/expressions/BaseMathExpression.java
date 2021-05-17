package parser.expressions;

import parser.Value;

public class BaseMathExpression
{
    public Value value;
    public LogicExpression parentLogicExpression;
    public boolean isMinus;
    public BaseMathExpression(boolean isMinus, Value value)
    {
        this.value = value;
        this.isMinus = isMinus;
    }
    public BaseMathExpression(boolean isMinus, LogicExpression parentLogicExpression)
    {
        this.parentLogicExpression = parentLogicExpression;
        this.isMinus = isMinus;
    }

}
