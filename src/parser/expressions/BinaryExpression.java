package parser.expressions;

import lexer.TokenType;

public abstract class BinaryExpression extends Expression
{
    public Expression left;
    public Expression right;
    public TokenType operator;

    public BinaryExpression(Expression left, Expression right, TokenType operator)
    {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public BinaryExpression(Expression left, TokenType unary)
    {
        this.left = left;
        this.operator = unary;
    }
}
