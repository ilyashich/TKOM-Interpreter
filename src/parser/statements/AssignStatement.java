package parser.statements;

import interpreter.Scope;
import lexer.TokenType;
import parser.expressions.Expression;
import parser.expressions.Variable;

public class AssignStatement extends Statement
{
    public TokenType complexField;
    public String identifier;
    public Expression rhs;


    public AssignStatement(String identifier, Expression rhs)
    {
        this.identifier = identifier;
        this.rhs = rhs;
    }

    public AssignStatement(String identifier, Expression rhs, TokenType field)
    {
        this.identifier = identifier;
        this.rhs = rhs;
        this.complexField = field;
    }

    @Override
    public Object execute(Scope scope) throws Exception
    {
        if(!scope.contains(this.identifier))
            scope.putVar(new Variable(this));
//        Variable variable = scope.getVar(this.identifier);
//        TokenType variableType = variable.type;
        Object val = this.rhs.evaluate(scope);

//        if (val instanceof String && variableType != TokenType.TEXT
//                || val instanceof Integer && variableType != TokenType.NUMBER
//                || val instanceof Complex && variableType != TokenType.COMPLEX
//        )
//        {
//            throw new Exception("próba przypisania do zmiennej: " + identifier + ", o typie: "+ variableType.name() + " wartość innego typu");
//        }

        if(complexField != null)
            scope.setComplexFieldValue(this.identifier, this.complexField, val);
        else
            scope.setVarValue(this.identifier, val);
        return null;
    }
}
