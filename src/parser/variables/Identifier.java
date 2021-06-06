package parser.variables;

import interpreter.Scope;
import lexer.Token;
import lexer.TokenType;
import parser.expressions.Expression;
import parser.expressions.Variable;

import java.math.BigDecimal;

public class Identifier extends Expression
{
    public String name;
    public Token token;
    public TokenType field;

    public Identifier(Token token)
    {
        this.name = token.getStringValue();
        this.token = token;
    }

    public Identifier(Token token, TokenType field)
    {
        this.name = token.getStringValue();
        this.token = token;
        this.field = field;
    }

    @Override
    public Object evaluate(Scope scope) throws Exception
    {
        if(field == null)
        {
            if(!scope.contains(this.name))
                //scope.putVar(new Variable(name, token.getType()));
                throw new Exception("Variable " + this.name + " was never assigned! " + this.token.getPosition());
            return scope.getVar(name).value;
        }
        if(!(scope.getVar(name).value instanceof Complex))
            throw new Exception("Attempt to use complex field with the type other than Complex! " + token.getPosition());

        return BigDecimal.valueOf(((Complex) scope.getVar(name).value).getField(field));

    }
}