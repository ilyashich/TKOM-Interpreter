package parser.statements;

import lexer.Lexer;
import lexer.TokenType;
import parser.FunctionDefinition;
import parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class ImportStatement
{
    private final String fileName;
    public ImportStatement(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }

    public ArrayList<FunctionDefinition> execute() throws Exception
    {
        File input = new File("src\\imports\\"+fileName);
        InputStream inputStream = new FileInputStream(input);
        Lexer lexer = new Lexer(inputStream);
        Parser parser = new Parser(lexer);
        FunctionDefinition functionDefinition;
        ArrayList<FunctionDefinition> functions = new ArrayList<>();
        while((functionDefinition = parser.tryParseFunction()) != null)
            functions.add(functionDefinition);
        parser.expect(TokenType.EOF);
        return functions;
    }
}
