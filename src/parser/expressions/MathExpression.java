package parser.expressions;

import lexer.Token;
import lexer.TokenType;
import interpreter.Scope;
import parser.variables.Complex;

import java.math.BigDecimal;

public class MathExpression extends BinaryExpression
{
    public MathExpression(Expression left, Expression right, Token operator)
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
            if(operator.getType() == TokenType.PLUS)
                return (int)left + (int)right ;
            else
                return (int)left - (int)right;

        }

        if(left instanceof BigDecimal && right instanceof BigDecimal)
        {
            if(operator.getType() == TokenType.PLUS)
                return ((BigDecimal) left).add((BigDecimal) right);
            else
                return ((BigDecimal) left).subtract((BigDecimal) right);

        }

        if(left instanceof BigDecimal && right instanceof Integer)
        {
            if(operator.getType() == TokenType.PLUS)
                return ((BigDecimal) left).add(new BigDecimal((int) right));
            else
                return ((BigDecimal) left).subtract(new BigDecimal((int) right));

        }

        if(left instanceof Integer && right instanceof BigDecimal)
        {
            if(operator.getType() == TokenType.PLUS)
                return (new BigDecimal((int) left)).add((BigDecimal) right);
            else
                return (new BigDecimal((int) left)).subtract((BigDecimal) right);

        }

        if(left instanceof Complex || right instanceof Complex) //dodawanie macierzy
        {
            if(left instanceof Complex && right instanceof Complex)
            {
                if(operator.getType() == TokenType.PLUS)
                    return ((Complex) left).add(((Complex) right));
                else
                    return ((Complex) left).subtract(((Complex) right));
            }
            if(left instanceof Complex && right instanceof Number)
            {
                if(operator.getType() == TokenType.PLUS)
                    return ((Complex) left).add((double) right);
                else
                    return ((Complex) left).subtract(((double) right));
            }
            if(right instanceof Complex && left instanceof Number)
            {
                if(operator.getType() == TokenType.PLUS)
                    return ((Complex) right).add((double) left);
                else
                    return ((Complex) right).subtractComplexFromInteger(((double) left));
            }
            throw new Exception("You can only add or subtract complex with complex or integer. " + (operator!=null?  operator.getPosition().toString() : ""));
        }
        if(left instanceof String || right instanceof String)
        {
            return left.toString() + right.toString();
        }

        throw new Exception("Addition and subtraction are not supported for these types. " + (operator.getPosition()!=null?  operator.getPosition().toString() : ""));
    }
}
