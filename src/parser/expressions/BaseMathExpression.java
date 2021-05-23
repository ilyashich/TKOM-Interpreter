package parser.expressions;

import lexer.TokenType;
import parser.Value;

public class BaseMathExpression extends BinaryExpression
{
    public BaseMathExpression(Value value, TokenType negate)
    {
        super(value,negate);
    }
    public BaseMathExpression(Expression parentLogicExpression, TokenType negate)
    {
        super(parentLogicExpression, negate);
    }

}
