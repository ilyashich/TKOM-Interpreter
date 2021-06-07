package parser.expressions;

import lexer.Token;

public abstract class BinaryExpression extends Expression
{
    public Expression left;
    public Expression right;
    public Token operator;

    public BinaryExpression(Expression left, Expression right, Token operator)
    {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public BinaryExpression(Expression left, Token unary)
    {
        this.left = left;
        this.operator = unary;
    }
}
