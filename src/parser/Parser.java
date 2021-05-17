package parser;

import exceptions.ParserException;
import lexer.Lexer;
import lexer.Token;
import lexer.TokenType;
import parser.expressions.*;
import parser.statements.*;
import parser.variables.Complex;
import parser.variables.Number;
import parser.variables.Text;

import java.util.ArrayList;

public class Parser
{

    private static final ArrayList<TokenType> types;

    private static final ArrayList<TokenType> relationalOperations;

    private static final ArrayList<TokenType> complexFields;

    private static final ArrayList<TokenType> additiveOperators;

    private static final ArrayList<TokenType> multiplicativeOperators;

    static
    {
        types = new ArrayList<>();
        types.add(TokenType.COMPLEX);
        types.add(TokenType.NUMBER);
        types.add(TokenType.TEXT);

        relationalOperations = new ArrayList<>();
        relationalOperations.add(TokenType.GREATER);
        relationalOperations.add(TokenType.LESS);
        relationalOperations.add(TokenType.GREATER_EQUALS);
        relationalOperations.add(TokenType.LESS_EQUALS);
        relationalOperations.add(TokenType.NOT_EQUAL);
        relationalOperations.add(TokenType.EQUALS);

        complexFields = new ArrayList<>();
        complexFields.add(TokenType.COMPLEX_REAL_PART);
        complexFields.add(TokenType.COMPLEX_IMAGINARY_PART);

        additiveOperators = new ArrayList<>();
        additiveOperators.add(TokenType.PLUS);
        additiveOperators.add(TokenType.MINUS);

        multiplicativeOperators= new ArrayList<>();
        multiplicativeOperators.add(TokenType.MULTIPLY);
        multiplicativeOperators.add(TokenType.SLASH);
    }

    private Token currentToken;
    private Lexer lexer;

    public Parser(Lexer lexer) throws Exception
    {
        this.lexer = lexer;
        currentToken = lexer.getNext();
    }

    private void nextToken() throws Exception
    {
        currentToken = lexer.getNext();
    }

    private void expect(ArrayList<TokenType> acceptableTokens) throws ParserException
    {
        if(!acceptableTokens.contains(currentToken.getType()))
            throw new ParserException(currentToken, acceptableTokens, currentToken.getPosition());

    }

    private Token expect(TokenType acceptableToken) throws Exception
    {
        if(acceptableToken != currentToken.getType())
            throw new ParserException(currentToken, acceptableToken, currentToken.getPosition());
        Token prevToken = currentToken;
        nextToken();
        return prevToken;

    }

    public Program tryParseProgram() throws Exception
    {
        FunctionDefinition temp;
        ImportStatement temp1 = null;
        ArrayList<FunctionDefinition> functions = new ArrayList<>();
        ArrayList<ImportStatement> imports = new ArrayList<>();
        while(currentToken.getType() != TokenType.EOF && ((temp = tryParseFunction()) != null || (temp1 = tryParseImport()) != null))
        {
            functions.add(temp);
            imports.add(temp1);
        }
        expect(TokenType.EOF);
        return new Program(functions, imports);
    }

    private ImportStatement tryParseImport() throws Exception
    {
        if(currentToken.getType() != TokenType.IMPORT)
            return null;
        nextToken();

        if(currentToken.getType() != TokenType.TEXT)
            return null;
        String fileName = currentToken.getStringValue();
        nextToken();

        return new ImportStatement(fileName);
    }

    private FunctionDefinition tryParseFunction() throws Exception
    {
        if(currentToken.getType() != TokenType.FUNCTION)
            return null;
        
        nextToken();

        Identifier fun_id = new Identifier(expect(TokenType.IDENTIFIER).getStringValue());

        expect(TokenType.LEFT_BRACKET);

        ArrayList<Identifier> params = tryParseFunctionParameters();
        
        expect(TokenType.RIGHT_BRACKET);

        StatementBlock statementBlock;

        if((statementBlock = tryParseBlockStatement()) == null)
            throw new Exception("Empty Function Body!");

        return new FunctionDefinition(fun_id, params, statementBlock);

    }

    private ArrayList<Identifier> tryParseFunctionParameters() throws Exception
    {
        if(currentToken.getType() == TokenType.RIGHT_BRACKET)
            return null;

        ArrayList<Identifier> parameterList = new ArrayList<>();

        parameterList.add(new Identifier(expect(TokenType.IDENTIFIER).getStringValue()));

        while(currentToken.getType() == TokenType.COMMA)
        {
            nextToken();
            parameterList.add(new Identifier(expect(TokenType.IDENTIFIER).getStringValue()));
        }

        return parameterList;

    }

    private StatementBlock tryParseBlockStatement() throws Exception
    {
        if(currentToken.getType() != TokenType.LEFT_CURLY_BRACKET)
            return null;
        nextToken();

        ArrayList<Statement> statements = new ArrayList<>();

        Statement temp;

        while ((temp = tryParseIfStatement()) != null ||
                (temp = tryParseWhileStatement()) != null ||
                (temp = tryParseForStatement()) != null ||
                (temp = tryParseReturnStatement()) != null ||
                (temp = tryParseAssignStatement()) != null) {
            statements.add(temp);
        }
        expect(TokenType.RIGHT_CURLY_BRACKET);
        return new StatementBlock(statements);
    }

    private AssignStatement tryParseAssignStatement() throws Exception
    {
        LogicExpression rhs;
        Identifier identifier;
        TokenType field = null;
        if(currentToken.getType() != TokenType.IDENTIFIER)
            return null;
        identifier = new Identifier(currentToken.getStringValue());
        nextToken();

        if(currentToken.getType() == TokenType.DOT)
        {
            nextToken();
            expect(complexFields);
            field = currentToken.getType();
            nextToken();
        }

        expect(TokenType.ASSIGNMENT);
        if((rhs = tryParseLogicExpression()) == null)
            return null;
        expect(TokenType.SEMICOLON);
        if(field == null)
            return new AssignStatement(identifier, rhs);
        return new AssignStatement(identifier, rhs, field);

    }

    private FunctionCall tryParseFunctionCall(Identifier id) throws Exception
    {
        if(currentToken.getType() != TokenType.LEFT_BRACKET)
            return null;
        nextToken();
        LogicExpression temp;
        ArrayList<LogicExpression> params = new ArrayList<>();

        if((temp = tryParseLogicExpression()) != null)
        {
            params.add(temp);

            while(currentToken.getType() != TokenType.RIGHT_BRACKET && currentToken.getType() == TokenType.COMMA)
            {
                nextToken();
                if((temp = tryParseLogicExpression()) == null)
                    return null;
                params.add(temp);
            }
        }
        expect(TokenType.RIGHT_BRACKET);
        return new FunctionCall(id, params);
    }

    private Complex tryParseComplex() throws Exception
    {
        if(currentToken.getType() != TokenType.COMPLEX)
            return null;

        nextToken();

        expect(TokenType.LEFT_BRACKET);

        int realPart = expect(TokenType.NUMBER).getIntValue();

        expect(TokenType.COMMA);

        int imaginaryPart = expect(TokenType.NUMBER).getIntValue();

        expect(TokenType.RIGHT_BRACKET);

        return new Complex(realPart, imaginaryPart);
    }

    private Text tryParseText() throws Exception
    {
        if (currentToken.getType() != TokenType.TEXT)
            return null;
        String text = currentToken.getStringValue();
        nextToken();
        return new Text(text);
    }

    private Number tryParseNumber() throws Exception
    {
        if (currentToken.getType() != TokenType.NUMBER)
            return null;
        int number = currentToken.getIntValue();
        nextToken();
        return new Number(number);
    }

    private ReturnStatement tryParseReturnStatement() throws Exception
    {
        if(currentToken.getType() != TokenType.RETURN)
            return null;
        nextToken();
        LogicExpression logicExpression;

        if((logicExpression = tryParseLogicExpression()) == null)
            throw new Exception("Empty For Condition!");

        expect(TokenType.SEMICOLON);

        return new ReturnStatement(logicExpression);
    }

    private ForStatement tryParseForStatement() throws Exception
    {
        if(currentToken.getType() != TokenType.FOR)
            return null;
        nextToken();

        expect(TokenType.LEFT_BRACKET);

        AssignStatement iterator;

        if((iterator = tryParseAssignStatement()) == null)
            throw new Exception("Empty iterator assign in for loop!");


        LogicExpression logicExpression;

        if((logicExpression = tryParseLogicExpression()) == null)
            throw new Exception("Empty For Condition!");

        expect(TokenType.SEMICOLON);

        int incrementValue = expect(TokenType.NUMBER).getIntValue();

        expect(TokenType.RIGHT_BRACKET);

        StatementBlock statementBlock;

        if((statementBlock = tryParseBlockStatement()) == null)
            throw new Exception("Empty For Body!");

        return new ForStatement(iterator.identifier, logicExpression, incrementValue, statementBlock);


    }

    private WhileStatement tryParseWhileStatement() throws Exception
    {
        if(currentToken.getType() != TokenType.WHILE)
            return null;
        nextToken();

        expect(TokenType.LEFT_BRACKET);

        LogicExpression logicExpression;

        if((logicExpression = tryParseLogicExpression()) == null)
            throw new Exception("Empty While Condition!");

        expect(TokenType.RIGHT_BRACKET);

        StatementBlock statementBlock;

        if((statementBlock = tryParseBlockStatement()) == null)
            throw new Exception("Empty While Body!");

        return new WhileStatement(logicExpression, statementBlock);

    }

    private IfStatement tryParseIfStatement() throws Exception
    {
        if(currentToken.getType() != TokenType.IF)
            return null;
        nextToken();

        expect(TokenType.LEFT_BRACKET);

        LogicExpression Condition;

        if((Condition = tryParseLogicExpression()) == null)
            throw new Exception("Empty If Condition!");

        expect(TokenType.RIGHT_BRACKET);

        StatementBlock ifBody;

        if((ifBody = tryParseBlockStatement()) == null)
            throw new Exception("Empty If Body!");

        StatementBlock elseBody = null;

        if(currentToken.getType() == TokenType.ELSE)
        {
            nextToken();

            if ((elseBody = tryParseBlockStatement()) == null)
                throw new Exception("Empty Else Body!");
        }

        return new IfStatement(Condition, ifBody, elseBody);

    }

    private LogicExpression tryParseLogicExpression() throws Exception
    {
        ArrayList<AndExpression> expressions = new ArrayList<>();
        AndExpression andExpression;
        if((andExpression = tryParseAndExpression()) == null)
            return null;

        expressions.add(andExpression);

        while(currentToken.getType() == TokenType.OR)
        {
            nextToken();
            if((andExpression = tryParseAndExpression()) == null)
                throw new Exception("There's no expression after || sign!");

            expressions.add(andExpression);
        }
        return new LogicExpression(expressions);
    }

    private AndExpression tryParseAndExpression() throws Exception
    {
        ArrayList<RelationalExpression> expressions = new ArrayList<>();
        RelationalExpression relationalExpression;
        if((relationalExpression = tryParseRelationalExpression()) == null)
            return null;

        expressions.add(relationalExpression);

        while(currentToken.getType() == TokenType.AND)
        {
            nextToken();
            if((relationalExpression = tryParseRelationalExpression()) == null)
                throw new Exception("There's no expression after && sign!");

            expressions.add(relationalExpression);
        }
        return new AndExpression(expressions);
    }

    private RelationalExpression tryParseRelationalExpression() throws Exception
    {
        ArrayList<BaseLogicExpression> expressions = new ArrayList<>();
        BaseLogicExpression baseLogicExpression;
        if((baseLogicExpression = tryParseBaseLogicExpression()) == null)
            return null;

        expressions.add(baseLogicExpression);

        TokenType relationalOperator= null;
        if(relationalOperations.contains(currentToken.getType()))
        {
            relationalOperator = currentToken.getType();
            nextToken();
            if((baseLogicExpression = tryParseBaseLogicExpression()) == null)
                throw new Exception("There's no expression after relational operator!");

            expressions.add(baseLogicExpression);
        }
        return new RelationalExpression(expressions, relationalOperator);
    }

    private BaseLogicExpression tryParseBaseLogicExpression() throws Exception
    {
        MathExpression mathExpression;
        boolean negate = currentToken.getType() == TokenType.NOT;
        if((mathExpression = tryParseMathExpression()) == null)
            return null;
        return new BaseLogicExpression(negate, mathExpression);
    }

    private MathExpression tryParseMathExpression() throws Exception
    {
        ArrayList<MultiplicativeExpression> expressions = new ArrayList<>();
        ArrayList<TokenType> operators = new ArrayList<>();
        MultiplicativeExpression multiplicativeExpression;
        if((multiplicativeExpression = tryParseMultiplicativeExpression()) == null)
            return null;

        expressions.add(multiplicativeExpression);
        operators.add(null);

        while(additiveOperators.contains(currentToken.getType()))
        {
            operators.add(currentToken.getType());
            nextToken();
            if((multiplicativeExpression = tryParseMultiplicativeExpression()) == null)
                throw new Exception("There's no expression after additive operator!");

            expressions.add(multiplicativeExpression);
        }

        return new MathExpression(expressions,operators);
    }

    private MultiplicativeExpression tryParseMultiplicativeExpression() throws Exception
    {
        ArrayList<BaseMathExpression> expressions = new ArrayList<>();
        ArrayList<TokenType> operators = new ArrayList<>();
        BaseMathExpression baseMathExpression;
        if((baseMathExpression = tryParseBaseMathExpression()) == null)
            return null;

        expressions.add(baseMathExpression);
        operators.add(null);
        while(multiplicativeOperators.contains(currentToken.getType()))
        {
            operators.add(currentToken.getType());
            nextToken();
            if((baseMathExpression = tryParseBaseMathExpression()) == null)
                throw new Exception("There's no expression after multiplicative operator!");

            expressions.add(baseMathExpression);
        }

        return new MultiplicativeExpression(expressions, operators);
    }

    private BaseMathExpression tryParseBaseMathExpression() throws Exception
    {
        Value value;
        LogicExpression parentLogicExpression;
        boolean isMinus = currentToken.getType() == TokenType.MINUS;
        if(currentToken.getType() != TokenType.LEFT_BRACKET)
        {
            if ((value = tryParseValue()) == null)
                return null;
            return new BaseMathExpression(isMinus, value);
        }
        nextToken();
        if((parentLogicExpression = tryParseLogicExpression()) == null)
            return null;
        expect(TokenType.RIGHT_BRACKET);
        return new BaseMathExpression(isMinus, parentLogicExpression);
    }

    private Value tryParseValue() throws Exception
    {
        Number number;
        Complex complex;
        Variable variable;
        FunctionCall functionCall;
        if((number = tryParseNumber()) != null )
            return number;

        if((complex = tryParseComplex()) != null)
            return  complex;

        if(currentToken.getType() != TokenType.IDENTIFIER)
            return null;

        Identifier name = new Identifier(currentToken.getStringValue());

        nextToken();

        if((functionCall = tryParseFunctionCall(name)) != null)
            return  functionCall;

        variable = tryParseVariable(name);

        return variable;

    }

    private Variable tryParseVariable(Identifier name) throws Exception
    {
        TokenType field = null;
        if(currentToken.getType() == TokenType.DOT)
        {
            nextToken();
            expect(complexFields);
            field = currentToken.getType();
        }
        if(field == null)
            return new Variable(name);
        return new Variable(name, field);
    }


}
