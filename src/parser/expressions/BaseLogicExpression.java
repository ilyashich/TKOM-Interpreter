package parser.expressions;

import lexer.TokenType;

public class BaseLogicExpression extends BinaryExpression
{
    public BaseLogicExpression(Expression mathExpression, TokenType negate)
    {
        super(mathExpression, negate);
    }
}
