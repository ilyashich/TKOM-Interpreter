package parser.expressions;

import lexer.Token;
import lexer.TokenType;
import interpreter.Scope;
import parser.variables.Complex;

import java.math.BigDecimal;
import java.math.MathContext;

public class MultiplicativeExpression extends BinaryExpression
{
    public MultiplicativeExpression(Expression left, Expression right, Token operator)
    {
        super(left, right, operator);
    }

    @Override
    public Object evaluate(Scope scope) throws Exception
    {

        Object left = this.left.evaluate(scope);
        Object right = this.right.evaluate(scope);

        if(left instanceof Integer && right instanceof Integer)
        {
            if(operator.getType() == TokenType.MULTIPLY)
                return (int)left * (int)right;
            else
                return (int)left / (int)right;
        }
        if(left instanceof BigDecimal && right instanceof BigDecimal)
        {
            if(operator.getType() == TokenType.MULTIPLY)
                return ((BigDecimal) left).multiply((BigDecimal) right);
            else
                return ((BigDecimal) left).divide((BigDecimal) right, MathContext.DECIMAL64);
        }
        if(left instanceof BigDecimal && right instanceof Integer)
        {
            if(operator.getType() == TokenType.MULTIPLY)
                return ((BigDecimal) left).multiply(new BigDecimal((int)right));
            else
                return ((BigDecimal) left).divide(new BigDecimal((int)right), MathContext.DECIMAL64);
        }
        if(left instanceof Integer && right instanceof BigDecimal)
        {
            if(operator.getType() == TokenType.MULTIPLY)
                return (new BigDecimal((int)left)).multiply((BigDecimal) right);
            else
                return (new BigDecimal((int)left)).divide((BigDecimal) right, MathContext.DECIMAL64);
        }

        if(left instanceof Complex || right instanceof Complex) //dodawanie macierzy
        {
            if(left instanceof Complex && right instanceof Complex)
            {
                if(operator.getType() == TokenType.MULTIPLY)
                    return ((Complex) left).multiply(((Complex) right));
                else
                    return ((Complex) left).divide(((Complex) right));
            }
            if(left instanceof Complex && right instanceof Number)
            {
                if(operator.getType() == TokenType.MULTIPLY)
                    return ((Complex) left).multiply((double) right);
                else
                    return ((Complex) left).divide(((double) right));
            }
            if(right instanceof Complex && left instanceof Number)
            {
                if(operator.getType() == TokenType.MULTIPLY)
                    return ((Complex) right).multiply((double) left);
                else
                    return ((Complex) right).divideIntegerByComplex(((double) left));
            }
            throw new Exception("You can only multiply or divide complex with complex or integer. " + (operator!=null?  operator.getPosition().toString() : ""));
        }
        if(left instanceof String || right instanceof String)
        {
            throw new Exception("Multiplication and division is not supported for string type. " + (operator.getPosition()!=null?  operator.getPosition().toString() : ""));
        }

        throw new Exception("Multiplication and division is not supported for thes types. " + (operator.getPosition()!=null?  operator.getPosition().toString() : "") );
    }
}
