package parser.expressions;

import lexer.TokenType;

public class LogicExpression extends BinaryExpression
{
    public LogicExpression(Expression left, Expression right, TokenType operator)
    {
        super(left, right, operator);
    }
}
