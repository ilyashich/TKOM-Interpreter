import interpreter.Program;
import lexer.Lexer;
import parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        if(args.length != 1)
        {
            System.out.println("Wrong number of arguments, it should only be source code file path");
            System.exit(1);
        }

        File input = new File(args[0]);
        InputStream inputStream = new FileInputStream(input);
        Lexer lexer = new Lexer(inputStream);
        Parser parser = new Parser(lexer);

        Program program = parser.tryParseProgram();
        Object returned = program.run();

        System.out.println( "Process returned: " + returned);


    }
}
