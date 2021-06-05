package parser.expressions;

import lexer.Token;
import interpreter.Scope;

public class BaseLogicExpression extends BinaryExpression
{
    public BaseLogicExpression(Expression mathExpression, Token negate)
    {
        super(mathExpression, negate);
    }

    @Override
    public Object evaluate(Scope scope) throws Exception
    {
        Object left = this.left.evaluate(scope);

        if(left instanceof Boolean)
        {
            return  !((Boolean) left) ;
        }
        throw new Exception("Logic negation is only possible with logical values. " + (operator.getPosition()!=null?  operator.getPosition().toString() : "") );
    }
}
