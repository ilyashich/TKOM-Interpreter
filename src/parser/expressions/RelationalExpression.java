package parser.expressions;

import lexer.TokenType;

public class RelationalExpression extends BinaryExpression
{
    public RelationalExpression(Expression left, Expression right, TokenType operator)
    {
        super(left, right, operator);
    }
}
