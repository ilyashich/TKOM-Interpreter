package parser.expressions;

import lexer.TokenType;

public class MultiplicativeExpression extends BinaryExpression
{
    public MultiplicativeExpression(Expression left, Expression right, TokenType operator)
    {
        super(left, right, operator);
    }
}
