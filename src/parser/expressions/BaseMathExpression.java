package parser.expressions;

import lexer.Token;
import interpreter.Scope;
import parser.variables.Value;
import parser.variables.Complex;

import java.math.BigDecimal;

public class BaseMathExpression extends BinaryExpression
{
    public BaseMathExpression(Expression parentLogicExpression, Token negate)
    {
        super(parentLogicExpression, negate);
    }

    @Override
    public Object evaluate(Scope scope) throws Exception
    {


        Object left = this.left.evaluate(scope);

        if(left instanceof Integer)
        {
            return -(int)left;
        }

        if(left instanceof BigDecimal)
        {
            return ((BigDecimal) left).multiply(new BigDecimal(-1));
        }

        if(left instanceof Complex)
        {
            return  ((Complex)left).inverse();

        }

        if(left instanceof String)
        {
            throw new Exception("Minus operator is not supported with string type. "+ (operator.getPosition()!=null?  operator.getPosition().toString() : ""));
        }

        throw new Exception("Minus operator is not supported with this value. "+ (operator.getPosition()!=null?  operator.getPosition().toString() : ""));
    }

}
