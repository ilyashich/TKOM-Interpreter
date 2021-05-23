package parser.expressions;

import lexer.TokenType;

public class AndExpression extends BinaryExpression
{
    public AndExpression(Expression left, Expression right, TokenType operator)
    {
        super(left, right, operator);
    }
}
