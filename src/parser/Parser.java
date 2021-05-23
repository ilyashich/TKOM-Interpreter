package parser;

import exceptions.ParserException;
import lexer.Lexer;
import lexer.Token;
import lexer.TokenType;
import parser.expressions.*;
import parser.statements.*;
import parser.variables.Complex;

import java.util.ArrayList;

public class Parser
{

    private static final ArrayList<TokenType> relationalOperations;

    private static final ArrayList<TokenType> complexFields;

    private static final ArrayList<TokenType> additiveOperators;

    private static final ArrayList<TokenType> multiplicativeOperators;

    static
    {
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
        while((temp = tryParseFunction()) != null || (temp1 = tryParseImport()) != null)
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

        String fun_id = expect(TokenType.IDENTIFIER).getStringValue();

        expect(TokenType.LEFT_BRACKET);

        ArrayList<String> params = tryParseFunctionParameters();
        
        expect(TokenType.RIGHT_BRACKET);

        StatementBlock statementBlock;

        if((statementBlock = tryParseBlockStatement()) == null)
            throw new Exception("Empty Function Body!");

        return new FunctionDefinition(fun_id, params, statementBlock);

    }

    private ArrayList<String> tryParseFunctionParameters() throws Exception //do Poprawy
    {
        if(currentToken.getType() == TokenType.RIGHT_BRACKET)
            return null;

        ArrayList<String> parameterList = new ArrayList<>();

        parameterList.add(expect(TokenType.IDENTIFIER).getStringValue());

        while(currentToken.getType() == TokenType.COMMA)
        {
            nextToken();
            parameterList.add(expect(TokenType.IDENTIFIER).getStringValue());
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
                (temp = tryParseAssignOrFunctionCall()) != null ){
            statements.add(temp);
        }
        expect(TokenType.RIGHT_CURLY_BRACKET);
        return new StatementBlock(statements);
    }

    private Statement tryParseAssignOrFunctionCall() throws Exception
    {
        if(currentToken.getType() != TokenType.IDENTIFIER)
            return null;
        String identifier = currentToken.getStringValue();
        nextToken();
        Statement statement;
        if((statement = tryParseAssignStatement(identifier)) != null)
            return statement;

        return tryParseFunctionCallStatement(identifier);
    }

    private Statement tryParseAssignStatement(String identifier) throws Exception
    {
        TokenType field = null;

        if(currentToken.getType() == TokenType.DOT)
        {
            nextToken();
            expect(complexFields);
            field = currentToken.getType();
            nextToken();
            if(TokenType.ASSIGNMENT != currentToken.getType())
                throw new ParserException(currentToken, TokenType.ASSIGNMENT, currentToken.getPosition());
        }

        if(currentToken.getType() != TokenType.ASSIGNMENT)
            return null;

        nextToken();

        Expression rhs;

        if((rhs = tryParseLogicExpression()) == null)
            return null;
        expect(TokenType.SEMICOLON);
        if(field == null)
            return new AssignStatement(identifier, rhs);
        return new AssignStatement(identifier, rhs, field);

    }

    private Statement tryParseFunctionCallStatement(String name) throws Exception
    {
        if(currentToken.getType() != TokenType.LEFT_BRACKET)
            return null;
        nextToken();
        Expression temp;
        ArrayList<Expression> params = new ArrayList<>();

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
        expect(TokenType.SEMICOLON);
        return new FunctionCallStatement(name, params);
    }

    private FunctionCallExpression tryParseFunctionCallExpression(String id) throws Exception
    {
        if(currentToken.getType() != TokenType.LEFT_BRACKET)
            return null;
        nextToken();
        Expression temp;
        ArrayList<Expression> params = new ArrayList<>();

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
        return new FunctionCallExpression(id, params);
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

    private Statement tryParseReturnStatement() throws Exception
    {
        if(currentToken.getType() != TokenType.RETURN)
            return null;
        nextToken();
        Expression logicExpression;

        if((logicExpression = tryParseLogicExpression()) == null)
            throw new Exception("Empty return statement!");

        expect(TokenType.SEMICOLON);

        return new ReturnStatement(logicExpression);
    }

    private Statement tryParseForStatement() throws Exception
    {
        if(currentToken.getType() != TokenType.FOR)
            return null;
        nextToken();

        expect(TokenType.LEFT_BRACKET);

        Statement assignStatement;

        String id = expect(TokenType.IDENTIFIER).getStringValue();

        if((assignStatement = tryParseAssignStatement(id)) == null)
            throw new Exception("Empty iterator assign in for loop!");


        Expression logicExpression;

        if((logicExpression = tryParseLogicExpression()) == null)
            throw new Exception("Empty For Condition!");

        expect(TokenType.SEMICOLON);

        int incrementValue = expect(TokenType.NUMBER).getIntValue();

        expect(TokenType.RIGHT_BRACKET);

        StatementBlock statementBlock;

        if((statementBlock = tryParseBlockStatement()) == null)
            throw new Exception("Empty For Body!");

        return new ForStatement(assignStatement, logicExpression, incrementValue, statementBlock);


    }

    private Statement tryParseWhileStatement() throws Exception
    {
        if(currentToken.getType() != TokenType.WHILE)
            return null;
        nextToken();

        expect(TokenType.LEFT_BRACKET);

        Expression logicExpression;

        if((logicExpression = tryParseLogicExpression()) == null)
            throw new Exception("Empty While Condition!");

        expect(TokenType.RIGHT_BRACKET);

        StatementBlock statementBlock;

        if((statementBlock = tryParseBlockStatement()) == null)
            throw new Exception("Empty While Body!");

        return new WhileStatement(logicExpression, statementBlock);

    }

    private Statement tryParseIfStatement() throws Exception
    {
        if(currentToken.getType() != TokenType.IF)
            return null;
        nextToken();

        expect(TokenType.LEFT_BRACKET);

        Expression Condition;

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

    private Expression tryParseLogicExpression() throws Exception
    {
        Expression andExpression;
        Expression rightAndExpression;
        if((andExpression = tryParseAndExpression()) == null)
            return null;

        while(currentToken.getType() == TokenType.OR)
        {
            nextToken();
            if((rightAndExpression = tryParseAndExpression()) == null)
                throw new Exception("There's no expression after || sign!");

            andExpression = new LogicExpression(andExpression, rightAndExpression, TokenType.OR);
        }
        return andExpression;
    }

    private Expression tryParseAndExpression() throws Exception
    {
        Expression relationalExpression;
        Expression rightRelationalExpression;
        if((relationalExpression = tryParseRelationalExpression()) == null)
            return null;

        while(currentToken.getType() == TokenType.AND)
        {
            nextToken();
            if((rightRelationalExpression = tryParseRelationalExpression()) == null)
                throw new Exception("There's no expression after && sign!");

            relationalExpression = new AndExpression(relationalExpression, rightRelationalExpression, TokenType.AND);
        }
        return relationalExpression;
    }

    private Expression tryParseRelationalExpression() throws Exception
    {
        Expression baseLogicExpression;
        Expression rightBaseLogicExpression;
        if((baseLogicExpression = tryParseBaseLogicExpression()) == null)
            return null;

        TokenType relationalOperator;
        if(relationalOperations.contains(currentToken.getType()))
        {
            relationalOperator = currentToken.getType();
            nextToken();
            if((rightBaseLogicExpression = tryParseBaseLogicExpression()) == null)
                throw new Exception("There's no expression after relational operator!");

            baseLogicExpression = new RelationalExpression(baseLogicExpression, rightBaseLogicExpression, relationalOperator);
        }
        return baseLogicExpression;
    }

    private Expression tryParseBaseLogicExpression() throws Exception
    {
        Expression mathExpression;
        Token temp = currentToken;
        if((mathExpression = tryParseMathExpression()) == null)
            return null;
        if(temp.getType() == TokenType.NOT)
        {
            return new BaseLogicExpression(mathExpression, TokenType.NOT);
        }
        return mathExpression;
    }

    private Expression tryParseMathExpression() throws Exception
    {
        Expression multiplicativeExpression;
        Expression rightMultiplicativeExpression;
        TokenType operator;
        if((multiplicativeExpression = tryParseMultiplicativeExpression()) == null)
            return null;

        while(additiveOperators.contains(currentToken.getType()))
        {
            operator = currentToken.getType();
            nextToken();
            if((rightMultiplicativeExpression = tryParseMultiplicativeExpression()) == null)
                throw new Exception("There's no expression after additive operator!");

            multiplicativeExpression = new MathExpression(multiplicativeExpression, rightMultiplicativeExpression, operator);
        }

        return multiplicativeExpression;
    }

    private Expression tryParseMultiplicativeExpression() throws Exception
    {

        Expression baseMathExpression;
        Expression rightBaseMathExpression;
        TokenType operator;
        if((baseMathExpression = tryParseBaseMathExpression()) == null)
            return null;

        while(multiplicativeOperators.contains(currentToken.getType()))
        {
            operator = currentToken.getType();
            nextToken();
            if((rightBaseMathExpression = tryParseBaseMathExpression()) == null)
                throw new Exception("There's no expression after multiplicative operator!");

            baseMathExpression = new MultiplicativeExpression(baseMathExpression, rightBaseMathExpression, operator);
        }

        return baseMathExpression;
    }

    private Expression tryParseBaseMathExpression() throws Exception
    {
        Expression value;
        Expression parentLogicExpression;
        TokenType minus = null;
        if(currentToken.getType() == TokenType.MINUS)
        {
            minus = currentToken.getType();
            nextToken();
        }
        if(currentToken.getType() != TokenType.LEFT_BRACKET)
        {
            if ((value = tryParseValue()) == null)
                return null;
            if(minus == null)
                return value;
            return new BaseMathExpression(value, minus);
        }
        nextToken();
        if((parentLogicExpression = tryParseLogicExpression()) == null)
            return null;
        expect(TokenType.RIGHT_BRACKET);
        if(minus == null)
            return parentLogicExpression;
        return new BaseMathExpression(parentLogicExpression, minus);
    }

    private Expression tryParseValue() throws Exception
    {
        Complex complex;
        Expression variable;
        FunctionCallExpression functionCall;
        if(currentToken.getType() == TokenType.NUMBER)
        {
            int number = currentToken.getIntValue();
            nextToken();
            return new Value(TokenType.NUMBER, number);
        }

        if((complex = tryParseComplex()) != null)
            return new Value(TokenType.COMPLEX, complex);

        if(currentToken.getType() != TokenType.IDENTIFIER)
            return null;

        String name = currentToken.getStringValue();
        Token t = currentToken;

        nextToken();

        if((functionCall = tryParseFunctionCallExpression(name)) != null)
            return  functionCall;

        variable = tryParseVariable(t);

        return variable;

    }

    private Expression tryParseVariable(Token token) throws Exception
    {
        TokenType field = null;
        if(currentToken.getType() == TokenType.DOT)
        {
            nextToken();
            expect(complexFields);
            field = currentToken.getType();
            nextToken();
        }
        if(field == null)
            return new Identifier(token);
        return new Identifier(token, field);
    }


}
