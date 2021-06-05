package parser;

import interpreter.Scope;
import parser.expressions.Expression;
import parser.expressions.Variable;
import parser.statements.ReturnStatement;
import parser.statements.Statement;
import parser.statements.StatementBlock;

import java.util.ArrayList;

public class FunctionDefinition extends Statement
{
    private final String identifier;
    private final ArrayList<String> params;
    private final StatementBlock statementBlock;

    public FunctionDefinition(String identifier, ArrayList<String> params, StatementBlock statementBlock)
    {
        this.identifier = identifier;
        this.params = params;
        this.statementBlock = statementBlock;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public ArrayList<String> getParams()
    {
        return params;
    }

    public StatementBlock getStatementBlock()
    {
        return statementBlock;
    }

    @Override
    public Object execute(Scope scope) throws Exception
    {
        return execute(scope,null);
    }
    public Object execute(Scope callingScope, ArrayList<Expression> arguments) throws Exception
    {
        //dodawanie parametrów
        int i=0;
        Scope newScope = new Scope();
        if(!identifier.equals("main"))
        {
            for (String item: params)
            {
                setVariablesValueInterScope(newScope,callingScope,arguments.get(i++),item);
            }
        }

        //wykonywanie statmentów
        for (Statement statement : statementBlock.statements)
        {
            if(statement instanceof ReturnStatement)
            {
                return statement.execute(newScope);
            }
            Object returned = statement.execute(newScope);

            if(returned instanceof ReturnStatement)
            {
                return ((ReturnStatement) returned).execute(newScope);
            }
        }
        return null;
    }

    private void setVariablesValueInterScope(Scope innerScope, Scope parentScope ,Expression value,String name) throws Exception
    {
//        Variable variable = innerScope.getVar(name);
//        TokenType variableType = variable.type;
        Object val = value.evaluate(parentScope);
        if(!innerScope.contains(name))
            innerScope.putVar(new Variable(name));

//        if (val instanceof String && variableType != TokenType.TEXT
//                || val instanceof Integer && variableType != TokenType.NUMBER
//                || val instanceof Complex && variableType != TokenType.COMPLEX
//        ) {
//            throw new Exception("Podczas wywołania funkcji "+identifier +": Błąd typ wartości inny niż deklarowany typ parametru: " + name);
//        }
        innerScope.setVarValue(name, val);
    }
}
