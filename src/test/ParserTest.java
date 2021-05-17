package test;

import lexer.Lexer;
import lexer.TokenType;
import org.junit.Assert;
import org.junit.Test;
import parser.Parser;
import parser.Program;
import parser.expressions.LogicExpression;
import parser.statements.*;
import parser.variables.Complex;
import parser.variables.Number;

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
        Assert.assertEquals(test.getFunctions().get(0).getIdentifier().name, "main");
        Assert.assertEquals(test.getFunctions().get(0).getParams().get(0).name, "a");
        Assert.assertEquals(test.getFunctions().get(0).getParams().get(1).name, "b");
        Assert.assertEquals(test.getFunctions().get(0).getParams().get(2).name, "c");
    }

    @Test
    public void parseIf() throws Exception
    {

        IfStatement statement = (IfStatement) setupBlockStatement("if(a + b < 5){ }").get(0);
        Assert.assertTrue(statement instanceof IfStatement);
        Assert.assertTrue(statement.getCondition() instanceof LogicExpression);
        Assert.assertEquals(statement.getCondition().expressions.get(0).expressions.get(0).relationalOperator, TokenType.LESS);
        Assert.assertEquals(statement.getCondition().expressions.get(0).expressions.get(0).getExpressions().get(0).mathExpression.operators.get(1), TokenType.PLUS);

    }

    @Test
    public void parseWhile() throws Exception
    {

        WhileStatement statement = (WhileStatement) setupBlockStatement("while(a != 5){ a = a+5; }").get(0);
        Assert.assertTrue(statement instanceof WhileStatement);
        Assert.assertTrue(statement.getCondition() instanceof LogicExpression);
        Assert.assertEquals(statement.getCondition().expressions.get(0).expressions.get(0).relationalOperator, TokenType.NOT_EQUAL);
        Assert.assertTrue(statement.body.statements.get(0) instanceof AssignStatement);

    }

    @Test
    public void parseFor() throws Exception
    {

        ForStatement statement = (ForStatement) setupBlockStatement("for(i=0; i <= 5; 3){}").get(0);
        Assert.assertTrue(statement instanceof ForStatement);
        Assert.assertTrue(statement.getLogicExpression() instanceof LogicExpression);
        Assert.assertEquals(statement.getIdentifier().name, "i");
        Assert.assertEquals(statement.getLogicExpression().expressions.get(0).expressions.get(0).relationalOperator, TokenType.LESS_EQUALS);
        Assert.assertEquals(statement.getIncrementValue(), 3);

    }

    @Test
    public void parseAssignFunctionCall() throws Exception
    {

        AssignStatement statement = (AssignStatement) setupBlockStatement("a = hello(a+b, 7);").get(0);
        Assert.assertTrue(statement instanceof AssignStatement);
        Assert.assertTrue(statement.rhs.expressions.get(0).expressions.get(0).expressions.get(0).mathExpression.expressions.get(0).expressions.get(0).value instanceof FunctionCall);
        Assert.assertEquals(statement.identifier.name, "a");

    }

    @Test
    public void parseAssignComplex() throws Exception
    {

        AssignStatement statement = (AssignStatement) setupBlockStatement("b = Complex(2, 5);").get(0);
        Assert.assertTrue(statement instanceof AssignStatement);
        Assert.assertTrue(statement.rhs.expressions.get(0).expressions.get(0).expressions.get(0).mathExpression.expressions.get(0).expressions.get(0).value instanceof Complex);
        Assert.assertEquals(statement.identifier.name, "b");

    }

    @Test
    public void parseAssignComplexAttribute() throws Exception
    {

        AssignStatement statement = (AssignStatement) setupBlockStatement("b.imag = 5;").get(0);
        Assert.assertTrue(statement instanceof AssignStatement);
        Assert.assertTrue(statement.rhs.expressions.get(0).expressions.get(0).expressions.get(0).mathExpression.expressions.get(0).expressions.get(0).value instanceof Number);
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

        ReturnStatement statement = (ReturnStatement) setupBlockStatement("return 0;").get(0)
;        Assert.assertTrue(statement instanceof ReturnStatement);
        Assert.assertTrue(statement.getExpression().expressions.get(0).expressions.get(0).expressions.get(0).mathExpression.expressions.get(0).expressions.get(0).value instanceof Number);

    }
}
