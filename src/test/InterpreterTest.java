package test;

import interpreter.Program;
import lexer.Lexer;
import org.junit.Assert;
import org.junit.Test;
import parser.Parser;
import parser.variables.Complex;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

public class InterpreterTest
{
    private Object test(String statementBlock, boolean isMain) throws Exception
    {
        if(isMain)
        {
            statementBlock = "function main(){" + statementBlock + "}";
        }
        InputStream inputStream = new ByteArrayInputStream(statementBlock.getBytes(StandardCharsets.UTF_8));
        Lexer lexer = new Lexer(inputStream);
        Parser parser = new Parser(lexer);

        Program program = parser.tryParseProgram();
        return program.run();

    }
    @Test
    public void first() throws Exception
    {
        String body = "return 1;";
        Assert.assertEquals(test(body, true), 1);
    }
    @Test
    public void second() throws Exception
    {
        String body = "return -1;";
        Assert.assertEquals(test(body, true), -1);
    }
    @Test
    public void third() throws Exception
    {
        String body = "return \"Hello\";";
        Assert.assertEquals(test(body, true), "Hello");
    }
    @Test
    public void fourth() throws Exception
    {
        String body = "return 0.45;";
        Assert.assertEquals(test(body, true), new BigDecimal("0.45"));
    }
    @Test
    public void fifth() throws Exception
    {
        String body = "return -0.45;";
        Assert.assertEquals(test(body, true), new BigDecimal("-0.45"));
    }
    @Test
    public void sixth() throws Exception
    {
        String body = "return 1.456;";
        Assert.assertEquals(test(body, true), new BigDecimal("1.456"));
    }
    @Test
    public void seventh() throws Exception
    {
        String body = "return -1.456;";
        Assert.assertEquals(test(body, true),  new BigDecimal("-1.456"));
    }
    @Test
    public void eighth() throws Exception
    {
        String body = "c = Complex(1,2); return c;";
        Assert.assertTrue(((Complex)test(body, true)).equals(new Complex(1,2)));
    }
    @Test
    public void ninth() throws Exception
    {
        String body = "c = Complex(1.0, 2.0); return c;";
        Assert.assertTrue(((Complex)test(body, true)).equals(new Complex(1.0,2.0)));
    }
    @Test
    public void tenth() throws Exception
    {
        String body = "c = Complex(1, 2); return c.real;";
        Assert.assertEquals(test(body, true), new BigDecimal("1.0"));
    }
    @Test
    public void eleventh() throws Exception
    {
        String body = "c = Complex(1, 2); return c.imag;";
        Assert.assertEquals(test(body, true), new BigDecimal("2.0"));
    }
    @Test
    public void twelfth() throws Exception
    {
        String body = "c = Complex(1.5, 2.7); return c.real;";
        Assert.assertEquals(test(body, true), new BigDecimal("1.5"));
    }
    @Test
    public void thirteenth() throws Exception
    {
        String body = "c = Complex(1.5, 2.7); return c.imag;";
        Assert.assertEquals(test(body, true), new BigDecimal("2.7"));
    }
    @Test
    public void fourteenth() throws Exception
    {
        String body = "c = Complex(-0.4589,2.22222222); return c;";
        Assert.assertTrue(((Complex)test(body, true)).equals(new Complex(-0.4589,2.22222222)));
    }
    @Test
    public void fifteenth() throws Exception
    {
        String body = "c = Complex(1.0005, -2.45); return c;";
        Assert.assertTrue(((Complex)test(body, true)).equals(new Complex(1.0005,-2.45)));
    }
}
