package parser.expressions;

import lexer.Token;
import lexer.TokenType;
import interpreter.Scope;
import parser.variables.Complex;

import java.math.BigDecimal;

public class RelationalExpression extends BinaryExpression
{
    public RelationalExpression(Expression left, Expression right, Token operator)
    {
        super(left, right, operator);
    }

    @Override
    public Object evaluate(Scope scope) throws Exception
    {
        Object left = this.left.evaluate(scope);
        Object right = this.right.evaluate(scope);
        TokenType op = this.operator.getType();



        if(left instanceof Integer && right instanceof Integer)
        {
            boolean equals = (int)left == (int)right;
            boolean leftIsLessThanRight = (int)left < (int)right;

            return compare(op, equals, leftIsLessThanRight);

        }

        if(left instanceof BigDecimal && right instanceof BigDecimal)
        {
            boolean equals = ((BigDecimal) left).compareTo((BigDecimal) right) == 0;
            boolean leftIsLessThanRight = ((BigDecimal) left).compareTo((BigDecimal) right) == -1;

            return compare(op, equals, leftIsLessThanRight);

        }

        if(left instanceof BigDecimal && right instanceof Integer)
        {
            boolean equals = ((BigDecimal) left).compareTo(new BigDecimal((int)right)) == 0;
            boolean leftIsLessThanRight = ((BigDecimal) left).compareTo(new BigDecimal((int)right)) == -1;

            return compare(op, equals, leftIsLessThanRight);

        }

        if(left instanceof Integer && right instanceof BigDecimal)
        {
            boolean equals = ((BigDecimal) right).compareTo(new BigDecimal((int)left)) == 0;
            boolean leftIsLessThanRight = ((BigDecimal) right).compareTo(new BigDecimal((int)left)) == 1;

            return compare(op, equals, leftIsLessThanRight);

        }

        if(left instanceof Complex && right instanceof Complex)
        {
            if(op == TokenType.EQUALS && ((Complex)left).equals((Complex)right))
            {
                return true;
            }
            if(op == TokenType.NOT_EQUAL && !((Complex)left).equals((Complex)right))
            {
                return true;
            }
            if(op == TokenType.EQUALS && !((Complex)left).equals((Complex)right))
            {
                return false;
            }
            if(op == TokenType.NOT_EQUAL && ((Complex)left).equals((Complex)right))
            {
                return false;
            }
            throw new Exception("Only == and != are supported for complex numbers " + (operator.getPosition()!=null?  operator.getPosition().toString() : ""));
        }

        if(left instanceof String && right instanceof String)
        {
            if(op == TokenType.EQUALS && left.equals(right))
            {
                return true;
            }
            if(op == TokenType.NOT_EQUAL && !left.equals(right))
            {
                return true;
            }
            if(op == TokenType.EQUALS)
            {
                return false;
            }
            if(op == TokenType.NOT_EQUAL)
            {
                return false;
            }
            throw new Exception("Only == and != are supported for strings " + (operator.getPosition()!=null?  operator.getPosition().toString() : ""));

        }

        throw new Exception("Both sides of comparison must be the same type! " + (operator.getPosition()!=null?  operator.getPosition().toString() : ""));
    }

    private Object compare(TokenType op, boolean equals, boolean leftIsLessThanRight)
    {
        switch (op)
        {
            case GREATER:
                if(!equals && !leftIsLessThanRight)
                    return true;
                break;
            case GREATER_EQUALS:
                if(equals || !leftIsLessThanRight)
                    return true;
                break;
            case LESS:
                if(leftIsLessThanRight)
                    return true;
                break;
            case LESS_EQUALS:
                if(equals || leftIsLessThanRight)
                    return true;
                break;
            case EQUALS:
                if(equals)
                    return true;
                break;
            case NOT_EQUAL:
                if(!equals)
                    return true;
                break;
        }
        return false;
    }
}
