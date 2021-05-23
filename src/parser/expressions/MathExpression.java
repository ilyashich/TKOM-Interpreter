package parser.expressions;

import lexer.TokenType;

public class MathExpression extends BinaryExpression
{
    public MathExpression(Expression left, Expression right, TokenType operator)
    {
        super(left, right, operator);
    }
}
