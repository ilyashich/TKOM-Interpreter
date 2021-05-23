package parser;

import parser.statements.ImportStatement;
import java.util.ArrayList;

public class Program
{
    private ArrayList<FunctionDefinition> functions;
    private ArrayList<ImportStatement> imports;
    private Object value;


    public Program(ArrayList<FunctionDefinition> functions, ArrayList<ImportStatement> imports)
    {
        this.functions = functions;
        this.imports = imports;
        this.value = 1;
    }

    public ArrayList<FunctionDefinition> getFunctions()
    {
        return functions;
    }

    public ArrayList<ImportStatement> getImports()
    {
        return imports;
    }

    public Object getValue()
    {
        return value;
    }

}
