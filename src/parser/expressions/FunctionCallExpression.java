package parser.expressions;

import parser.FunctionDefinition;
import interpreter.Program;
import interpreter.Scope;
import parser.variables.Identifier;
import parser.variables.Value;
import parser.variables.Complex;
import tkom.ExecuteSystemFunction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

public class FunctionCallExpression extends Expression
{
    public String id;
    public ArrayList<Expression> parameters;
    public ArrayList<String> builtInFunctions = new ArrayList<>(Arrays.asList("modulus", "conjugate", "pow"));

    public FunctionCallExpression(String id, ArrayList<Expression> parameters)
    {
        this.id = id;
        this.parameters = parameters;
    }

    @Override
    public Object evaluate(Scope scope) throws Exception
    {
        if(builtInFunctions.contains(id))
        {
            return executeBuiltInFunction(scope, parameters);
        }

        if(ExecuteSystemFunction.functions.contains(id))
        {
            if(parameters.size() > 0)
                return ExecuteSystemFunction.executeSystemFunction(id, parameters.size(), parameters.get(0).evaluate(scope));
            else
                return ExecuteSystemFunction.executeSystemFunction(id, 0, null);
        }

        for(FunctionDefinition functionDefinition: Program.getFunctions())
        {
            if(functionDefinition.getIdentifier().equals(id))
                return functionDefinition.execute(scope, parameters);
        }
        throw new Exception("There's no definition for function " + id + " ");
    }

    private Object executeBuiltInFunction(Scope scope, ArrayList<Expression> params) throws Exception
    {
        if(id.equals("modulus"))
        {
            if(params.size() != 1)
                throw new Exception("Wrong number of arguments in function modulus call");
            Object complex = params.get(0).evaluate(scope);
            if(!(complex instanceof Complex))
            {
                if(params.get(0) instanceof Identifier)
                    throw new Exception("Argument of function modulus must be Complex " + ((Identifier) params.get(0)).token.getPosition());
                if(params.get(0) instanceof Value)
                    throw new Exception("Argument of function modulus must be Complex " + ((Value) params.get(0)).token.getPosition());
            }
            assert complex instanceof Complex;
            return ((Complex) complex).modulus();
        }

        if(id.equals("conjugate"))
        {
            if(params.size() != 1)
                throw new Exception("Wrong number of arguments in function conjugate call");
            Object complex = params.get(0).evaluate(scope);
            if(!(complex instanceof Complex))
            {
                if(params.get(0) instanceof Identifier)
                    throw new Exception("Argument of function conjugate must be Complex " + ((Identifier) params.get(0)).token.getPosition());
                if(params.get(0) instanceof Value)
                    throw new Exception("Argument of function conjugate must be Complex " + ((Value) params.get(0)).token.getPosition());
            }
            assert complex instanceof Complex;
            return ((Complex) complex).conjugate();
        }

        if(id.equals("pow"))
        {
            if(params.size() != 2)
                throw new Exception("Wrong number of arguments in function pow call");
            Object number = params.get(0).evaluate(scope);
            Object power = params.get(1).evaluate(scope);
            if(!(number instanceof Complex) && !(number instanceof Number))
            {
                if(params.get(0) instanceof Identifier)
                    throw new Exception("Argument of function pow must be Complex or number " + ((Identifier) params.get(0)).token.getPosition());
                if(params.get(0) instanceof Value)
                    throw new Exception("Argument of function pow must be Complex or number " + ((Value) params.get(0)).token.getPosition());
            }
            if(!(power instanceof Number))
            {
                if(params.get(1) instanceof Identifier)
                    throw new Exception("Power of function pow must be double " + ((Identifier) params.get(1)).token.getPosition());
                if(params.get(1) instanceof Value)
                    throw new Exception("Power of function pow must be double " + ((Value) params.get(1)).token.getPosition());
            }
            if(number instanceof Complex)
            {
                if(power instanceof Integer)
                    return ((Complex) number).power((Integer) power);
                if(power instanceof BigDecimal)
                    return ((Complex) number).power(((BigDecimal) power).doubleValue());
            }
            if(number instanceof BigDecimal)
            {
                if(power instanceof Integer)
                    return BigDecimal.valueOf(Math.pow(((BigDecimal) number).doubleValue(), (Integer) power));
                if(power instanceof BigDecimal)
                    return BigDecimal.valueOf(Math.pow(((BigDecimal) number).doubleValue(), ((BigDecimal) power).doubleValue()));
            }
            if(number instanceof Integer)
            {
                if(power instanceof Integer)
                    return Math.pow((Integer) number, (Integer) power);
                if(power instanceof BigDecimal)
                    return BigDecimal.valueOf(Math.pow((Integer) number, ((BigDecimal) power).doubleValue()));
            }
        }

        return null;
    }
}
