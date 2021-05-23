package test;

import lexer.Lexer;
import lexer.TokenType;
import org.junit.Assert;
import org.junit.Test;
import parser.Identifier;
import parser.Parser;
import parser.Program;
import parser.Value;
import parser.expressions.*;
import parser.statements.*;
import parser.variables.Complex;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ParserTest
{
    private Program test;

    private void init(String txt) throws Exception
    {
        InputStream in = new ByteArrayInputStream(txt.getBytes(StandardCharsets.UTF_8));
        Lexer lexer = new Lexer(in);
        Parser parser = new Parser(lexer);
        test = parser.tryParseProgram();
    }

    public ArrayList<Statement> setupBlockStatement(String body) throws Exception
    {
        String code = "function main(){" + body + "}";
        InputStream in = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
        Lexer lexer = new Lexer(in);
        Parser parser = new Parser(lexer);
        return  parser.tryParseProgram().getFunctions().get(0).getStatementBlock().statements;
    }


    @Test
    public void parseFunction() throws Exception
    {
        String txt =
                "function main(a,b,c){}";
        init(txt);
        Assert.assertEquals(test.getFunctions().get(0).getIdentifier(), "main");
        Assert.assertEquals(test.getFunctions().get(0).getParams().get(0), "a");
        Assert.assertEquals(test.getFunctions().get(0).getParams().get(1), "b");
        Assert.assertEquals(test.getFunctions().get(0).getParams().get(2), "c");
    }

    @Test
    public void parseIf() throws Exception
    {

        IfStatement statement = (IfStatement) setupBlockStatement("if(a + b < 5){ }").get(0);
        Assert.assertTrue(statement instanceof IfStatement);
        Assert.assertTrue(statement.getCondition() instanceof RelationalExpression);
        Assert.assertEquals(((RelationalExpression) statement.getCondition()).operator, TokenType.LESS);
        Assert.assertEquals(((MathExpression) ((RelationalExpression) statement.getCondition()).left).operator, TokenType.PLUS);

    }

    @Test
    public void parseWhile() throws Exception
    {

        WhileStatement statement = (WhileStatement) setupBlockStatement("while(a != 5){ a = a+5; }").get(0);
        Assert.assertTrue(statement instanceof WhileStatement);
        Assert.assertTrue(statement.getCondition() instanceof RelationalExpression);
        Assert.assertEquals(((RelationalExpression) statement.getCondition()).operator, TokenType.NOT_EQUAL);
        Assert.assertTrue(((AssignStatement) statement.body.statements.get(0)).rhs instanceof MathExpression);

    }

    @Test
    public void parseFor() throws Exception
    {

        ForStatement statement = (ForStatement) setupBlockStatement("for(i=0; i <= 5; 3){}").get(0);
        Assert.assertTrue(statement instanceof ForStatement);
        Assert.assertTrue(statement.getLogicExpression() instanceof RelationalExpression);
        Assert.assertEquals(((AssignStatement)statement.getIdentifier()).identifier, "i");
        Assert.assertEquals(((RelationalExpression) statement.getLogicExpression()).operator, TokenType.LESS_EQUALS);
        Assert.assertEquals(statement.getIncrementValue(), 3);

    }

    @Test
    public void parseAssignFunctionCall() throws Exception
    {

        AssignStatement statement = (AssignStatement) setupBlockStatement("a = hello(a+b, 7);").get(0);
        Assert.assertTrue(statement instanceof AssignStatement);
        Assert.assertTrue(statement.rhs instanceof FunctionCallExpression);
        Assert.assertEquals(((Value)((FunctionCallExpression) statement.rhs).parameters.get(1)).value, 7);
        Assert.assertEquals(((MathExpression)((FunctionCallExpression) statement.rhs).parameters.get(0)).operator, TokenType.PLUS);
        Assert.assertEquals(((Identifier)((MathExpression)((FunctionCallExpression) statement.rhs).parameters.get(0)).left).name, "a");
        Assert.assertEquals(statement.identifier, "a");

    }

    @Test
    public void parseAssignComplex() throws Exception
    {

        AssignStatement statement = (AssignStatement) setupBlockStatement("b = Complex(2, 5);").get(0);
        Assert.assertTrue(statement instanceof AssignStatement);
        Assert.assertTrue(statement.rhs instanceof Value);
        Assert.assertEquals(((Complex) ((Value) statement.rhs).value).compare(new Complex(2,5)), 0);
        Assert.assertEquals(statement.identifier, "b");

    }

    @Test
    public void parseAssignComplexAttribute() throws Exception
    {

        AssignStatement statement = (AssignStatement) setupBlockStatement("b.imag = 5;").get(0);
        Assert.assertTrue(statement instanceof AssignStatement);
        Assert.assertTrue(statement.rhs instanceof Value);
        Assert.assertEquals(statement.identifier, "b");
        Assert.assertEquals(statement.complexField, TokenType.COMPLEX_IMAGINARY_PART);

    }

    @Test
    public void parseImport() throws Exception
    {

        String txt = "import\"io\"";
        init(txt);
        Assert.assertTrue(test.getImports().get(0) instanceof ImportStatement);
        Assert.assertEquals(test.getImports().get(0).getFileName(), "io");

    }

    @Test
    public void parseReturn() throws Exception
    {

        ReturnStatement statement = (ReturnStatement) setupBlockStatement("return 0;").get(0);
        Assert.assertTrue(statement instanceof ReturnStatement);
        Assert.assertTrue(statement.expression instanceof Value);
        Assert.assertEquals(((Value) statement.expression).value, 0);

    }

    @Test
    public void parseFunctionCallStatement() throws Exception
    {

        FunctionCallStatement statement = (FunctionCallStatement) setupBlockStatement("MyFunction(a.real,b,c);").get(0);
        Assert.assertTrue(statement instanceof FunctionCallStatement);
        Assert.assertTrue(statement.params.get(0) instanceof Identifier);
        Assert.assertEquals(((Identifier)statement.params.get(0)).name, "a");
        Assert.assertEquals(((Identifier)statement.params.get(0)).field, TokenType.COMPLEX_REAL_PART);

    }
}
