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
    @Test
    public void sixteenth() throws Exception
    {
        String body = "a=5; if(a<6){ a=7;} return a;";
        Assert.assertEquals(test(body, true), 7);
    }
    @Test
    public void seventeenth() throws Exception
    {
        String body = "a=5; if(a>6){ a=7;} else{a=10;} return a;";
        Assert.assertEquals(test(body, true), 10);
    }
    @Test
    public void eighteenth() throws Exception
    {
        String body = "c = Complex(0.5, 2); while(c.real <= 1){ c.imag = c.imag + 1; c.real = c.real + 0.1;} return c;";
        Assert.assertTrue(((Complex)test(body, true)).equals(new Complex(1.1,8)));
    }
    @Test
    public void nineteenth() throws Exception
    {
        String body = "a=5.5; for(i = 1; i != 7; i = i+3){a = a*2;} return a;";
        Assert.assertEquals(test(body, true), new BigDecimal("22.0"));
    }
    @Test
    public void twentieth() throws Exception
    {
        String body = "a=5.5; b = 2; c= a+b; return c;";
        Assert.assertEquals(test(body, true), new BigDecimal("7.5"));
    }
    @Test
    public void twentyFirst() throws Exception
    {
        String body = "a=5; b = -2; return a-b;";
        Assert.assertEquals(test(body, true), 7);
    }
    @Test
    public void twentySecond() throws Exception
    {
        String body = "a=1.2; b = -2.5; return a-b;";
        Assert.assertEquals(test(body, true), new BigDecimal("3.7"));
    }
    @Test
    public void twentyThird() throws Exception
    {
        String body = "a=-5;  return -a;";
        Assert.assertEquals(test(body, true), 5);
    }
    @Test
    public void twentyFourth() throws Exception
    {
        String body = "a=5;  return -a;";
        Assert.assertEquals(test(body, true), -5);
    }
    @Test
    public void twentyFifth() throws Exception
    {
        String body = "c = Complex(-8, 3.6);  return -c;";
        Complex test = (Complex)test(body, true);
        Assert.assertEquals(test.getReal(), 8.0, 0.0001);
        Assert.assertEquals(test.getImag(), -3.6, 0.0001);
    }
    @Test
    public void twentySixth() throws Exception
    {
        String body = "c = Complex(1, 2); d = conjugate(c); e = c*d;  return e;";
        Complex test = (Complex)test(body, true);
        Assert.assertEquals(test.getReal(), 5.0, 0.0001);
        Assert.assertEquals(test.getImag(), 0.0, 0.0001);
    }
    @Test
    public void twentySeventh() throws Exception
    {
        String body = "c = Complex(1, 2); d = modulus(c); return d;";
        Assert.assertEquals(test(body, true), BigDecimal.valueOf(Math.sqrt(5)));
    }
    @Test
    public void twentyEighth() throws Exception
    {
        String body = "c = Complex(1, 2); d = conjugate(c); return d != c;";
        Assert.assertEquals(test(body, true), true);
    }
    @Test
    public void twentyNinth() throws Exception
    {
        String body = "a = \"Hello\"; c = Complex(1,2); d = a + \" \" + c.imag + \" times\"; return d;";
        Assert.assertEquals(test(body, true), "Hello 2.0 times");
    }
    @Test
    public void thirtieth() throws Exception
    {
        String body = "a = \"\\\\\";  return a;";
        Assert.assertEquals(test(body, true), "\\");
    }
    @Test
    public void thirtyFirst() throws Exception
    {
        String body = "a = \"\\n\";  return a;";
        Assert.assertEquals(test(body, true), "\n");
    }
    @Test
    public void thirtySecond() throws Exception
    {
        String body = "function myFunction(a,b)" +
                        "{" +
                            "return a/b;"
                        +"}"+
                        "function main()" +
                        "{" +
                            "return myFunction(5.0,10.0);"
                        +"}";
        Assert.assertEquals(test(body, false), new BigDecimal("0.5"));
    }
    @Test
    public void thirtyThird() throws Exception
    {
        String body = "function myFunction(a)" +
                "{" +
                "if(a==1){return a+1;}"
                +"return a;"
                +"}"+
                "function main()" +
                "{" +
                "return myFunction(1);"
                +"}";
        Assert.assertEquals(test(body, false), 2);
    }
    @Test
    public void thirtyFourth() throws Exception
    {
        String body = "_hello_ = 10;  return _hello_;";
        Assert.assertEquals(test(body, true), 10);
    }
}
