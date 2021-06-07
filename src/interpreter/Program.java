package interpreter;

import parser.FunctionDefinition;
import parser.statements.ImportStatement;
import java.util.ArrayList;

public class Program
{
    private static ArrayList<FunctionDefinition> functions;
    private final ArrayList<ImportStatement> imports;


    public Program(ArrayList<FunctionDefinition> functions, ArrayList<ImportStatement> imports)
    {
        Program.functions = functions;
        this.imports = imports;

    }

    public Object run() throws Exception
    {
        ArrayList<FunctionDefinition> temp;
        if(this.imports != null)
        {
            for (ImportStatement importStatement : this.imports)
            {
                temp = importStatement.execute();
                functions.addAll(temp);
            }
        }
        for(FunctionDefinition function : functions)
        {
            if(function.getIdentifier().equals("main"))
                return function.execute(null);
        }
        throw new Exception("Function \"main\" wasn't defined ");

    }

    public static ArrayList<FunctionDefinition> getFunctions()
    {
        return functions;
    }

    public void setFunctions(ArrayList<FunctionDefinition> functions)
    {
        Program.functions = functions;
    }

    public ArrayList<ImportStatement> getImports()
    {
        return imports;
    }

}
