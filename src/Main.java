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

    public static synchronized void loadLibrary(java.io.File jar) {
        try {
            java.net.URL url = jar.toURI().toURL();
            java.lang.reflect.Method method = java.net.URLClassLoader.class.getDeclaredMethod("addURL", java.net.URL.class);
            method.setAccessible(true); /*promote the method to public access*/
            method.invoke(Thread.currentThread().getContextClassLoader(), url);
        } catch (Exception ex) {
            throw new RuntimeException("Cannot load library from jar file '" + jar.getAbsolutePath() + "'. Reason: " + ex.getMessage());
        }
    }
}
