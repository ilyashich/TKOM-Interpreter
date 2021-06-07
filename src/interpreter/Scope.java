package interpreter;

import lexer.TokenType;
import parser.expressions.Variable;
import parser.variables.Complex;

import java.math.BigDecimal;
import java.util.HashMap;

public class Scope
{
    public HashMap<String, Variable> variables = new HashMap<>();

    public Scope() {}


    public boolean contains (String name)
    {
        return variables.containsKey(name);
    }

    public void putVar(Variable var) throws Exception
    {
        if(contains(var.name))
        {
            throw new Exception("Duplicated variable: " + var.name );
        }
        else
        {
            variables.put(var.name,var);
        }
    }
    public Variable getVar(String name) throws Exception
    {
        if(!contains(name)) throw new Exception("Variable definition doesn't exist: " + name);
        return variables.get(name);
    }
    public void setVarValue(String name,Object val)
    {
        variables.get(name).value = val;
    }

    public void setComplexFieldValue(String name, TokenType field, Object value)
    {
        if(value instanceof Integer)
            ((Complex)variables.get(name).value).setField(field, (Integer) value);
        if(value instanceof BigDecimal)
            ((Complex)variables.get(name).value).setField(field, ((BigDecimal) value).doubleValue());
    }
}
