package parser.expressions;

import lexer.Token;
import interpreter.Scope;

public class AndExpression extends BinaryExpression
{
    public AndExpression(Expression left, Expression right, Token operator)
    {
        super(left, right, operator);
    }

    @Override
    public Object evaluate(Scope scope) throws Exception
    {
        Object left = this.left.evaluate(scope);
        Object right = this.right.evaluate(scope);


        if(left instanceof Boolean && right instanceof Boolean)
        {
            return   (Boolean) left && (Boolean) right;
        }
        throw new Exception("Both sides must have logical value. " + (operator.getPosition()!=null?  operator.getPosition().toString() : "") );
    }
}
