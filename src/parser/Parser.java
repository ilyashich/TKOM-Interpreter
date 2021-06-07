package parser;

import exceptions.ParserException;
import interpreter.Program;
import lexer.Lexer;
import lexer.Token;
import lexer.TokenType;
import parser.expressions.*;
import parser.statements.*;
import parser.variables.Complex;
import parser.variables.Identifier;
import parser.variables.Value;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Parser
{

    private static final ArrayList<TokenType> relationalOperations;

    private static final ArrayList<TokenType> complexFields;

    private static final ArrayList<TokenType> additiveOperators;

    private static final ArrayList<TokenType> multiplicativeOperators;

    private static final ArrayList<TokenType> numberOrDouble;

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

        numberOrDouble = new ArrayList<>();
        numberOrDouble.add(TokenType.NUMBER);
        numberOrDouble.add(TokenType.FLOAT);
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

    public void expect(ArrayList<TokenType> acceptableTokens) throws ParserException
    {
        if(!acceptableTokens.contains(currentToken.getType()))
            throw new ParserException(currentToken, acceptableTokens, currentToken.getPosition());

    }

    public Token expect(TokenType acceptableToken) throws Exception
    {
        if(acceptableToken != currentToken.getType())
            throw new ParserException(currentToken, acceptableToken, currentToken.getPosition());
        Token prevToken = currentToken;
        nextToken();
        return prevToken;

    }

    public Program tryParseProgram() throws Exception
    {
        Object temp;
        ArrayList<FunctionDefinition> functions = new ArrayList<>();
        ArrayList<ImportStatement> imports = new ArrayList<>();
        while((temp = tryParseFunction()) != null || (temp = tryParseImport()) != null)
        {
            if(temp instanceof FunctionDefinition)
                functions.add((FunctionDefinition) temp);
            if(temp instanceof ImportStatement)
                imports.add((ImportStatement) temp);
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

    public FunctionDefinition tryParseFunction() throws Exception
    {
        if(currentToken.getType() != TokenType.FUNCTION)
            return null;
        
        nextToken();

        String fun_id = expect(TokenType.IDENTIFIER).getStringValue();

        expect(TokenType.LEFT_BRACKET);

        ArrayList<String> params = tryParseFunctionParameters();
        
        expect(TokenType.RIGHT_BRACKET);

        StatementBlock statementBlock;

        Token temp = currentToken;

        if((statementBlock = tryParseBlockStatement()) == null)
            throw new ParserException(temp, "Empty Function Body!");

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
                (temp = tryParseAssignOrFunctionCall()) != null ||
                (temp = tryParseComment()) != null){
            statements.add(temp);
        }
        expect(TokenType.RIGHT_CURLY_BRACKET);
        if(statements.size() == 0)
            return null;
        return new StatementBlock(statements);
    }

    private Statement tryParseComment() throws Exception
    {
        if(currentToken.getType() != TokenType.COMMENT)
            return null;
        CommentStatement comment = new CommentStatement(currentToken.getStringValue());
        nextToken();
        return comment;
    }

    private Statement tryParseAssignOrFunctionCall() throws Exception
    {
        if(currentToken.getType() != TokenType.IDENTIFIER)
            return null;
        String identifier = currentToken.getStringValue();
        nextToken();
        Statement statement;
        if((statement = tryParseAssignStatement(identifier)) != null)
        {
            expect(TokenType.SEMICOLON);
            return statement;
        }

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

        BigDecimal realPart = tryParseComplexFields();

        nextToken();

        expect(TokenType.COMMA);

        BigDecimal imaginaryPart = tryParseComplexFields();

        nextToken();

        expect(TokenType.RIGHT_BRACKET);

        return new Complex(realPart.doubleValue(), imaginaryPart.doubleValue());
    }

    private BigDecimal tryParseComplexFields() throws Exception
    {
        if(currentToken.getType() == TokenType.MINUS)
        {
            nextToken();
            expect(numberOrDouble);
            if(currentToken.getType() == TokenType.NUMBER)
            {
                return new BigDecimal(-currentToken.getIntValue());
            }
            else
            {
                return new BigDecimal("-" + currentToken.getDoubleValue());
            }
        }
        else
        {
            expect(numberOrDouble);
            if(currentToken.getType() == TokenType.NUMBER)
            {
                return new BigDecimal(currentToken.getIntValue());
            }
            else
            {
                return currentToken.getDoubleValue();
            }
        }
    }

    private Statement tryParseReturnStatement() throws Exception
    {
        if(currentToken.getType() != TokenType.RETURN)
            return null;
        nextToken();
        Expression logicExpression;

        Token temp = currentToken;

        if((logicExpression = tryParseLogicExpression()) == null)
            throw new ParserException(temp, "Empty return statement!");

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

        Token temp = currentToken;

        if((assignStatement = tryParseAssignStatement(id)) == null)
            throw new ParserException(temp, "Empty iterator assign in for loop!");

        expect(TokenType.SEMICOLON);

        Expression logicExpression;

        temp = currentToken;

        if((logicExpression = tryParseLogicExpression()) == null)
            throw new ParserException(temp, "Empty For Condition!");

        expect(TokenType.SEMICOLON);

        Statement assignIncrement;

        String incr = expect(TokenType.IDENTIFIER).getStringValue();

        if((assignIncrement = tryParseAssignStatement(incr)) == null)
            throw new ParserException(temp, "Empty iterator assign in for loop!");

        expect(TokenType.RIGHT_BRACKET);

        StatementBlock statementBlock;

        temp = currentToken;

        if((statementBlock = tryParseBlockStatement()) == null)
            throw new ParserException(temp, "Empty For Body!");

        return new ForStatement(assignStatement, logicExpression, assignIncrement, statementBlock);


    }

    private Statement tryParseWhileStatement() throws Exception
    {
        if(currentToken.getType() != TokenType.WHILE)
            return null;
        nextToken();

        expect(TokenType.LEFT_BRACKET);

        Expression logicExpression;

        Token temp = currentToken;

        if((logicExpression = tryParseLogicExpression()) == null)
            throw new ParserException(temp, "Empty While Condition!");

        expect(TokenType.RIGHT_BRACKET);

        StatementBlock statementBlock;

        temp = currentToken;

        if((statementBlock = tryParseBlockStatement()) == null)
            throw new ParserException(temp, "Empty While Body!");

        return new WhileStatement(logicExpression, statementBlock);

    }

    private Statement tryParseIfStatement() throws Exception
    {
        if(currentToken.getType() != TokenType.IF)
            return null;
        nextToken();

        expect(TokenType.LEFT_BRACKET);

        Expression Condition;

        Token temp = currentToken;

        if((Condition = tryParseLogicExpression()) == null)
            throw new ParserException(temp, "Empty If Condition!");

        expect(TokenType.RIGHT_BRACKET);

        StatementBlock ifBody;

        temp = currentToken;

        if((ifBody = tryParseBlockStatement()) == null)
            throw new ParserException(temp, "Empty If Body!");

        StatementBlock elseBody = null;

        if(currentToken.getType() == TokenType.ELSE)
        {
            nextToken();

            temp = currentToken;

            if ((elseBody = tryParseBlockStatement()) == null)
                throw new ParserException(temp, "Empty Else Body!");
        }

        return new IfStatement(Condition, ifBody, elseBody);

    }

    private Expression tryParseLogicExpression() throws Exception
    {
        Expression andExpression;
        Expression rightAndExpression;
        Token temp, temp2;
        if((andExpression = tryParseAndExpression()) == null)
            return null;

        while(currentToken.getType() == TokenType.OR)
        {
            temp2 = currentToken;
            nextToken();
            temp = currentToken;
            if((rightAndExpression = tryParseAndExpression()) == null)
                throw new ParserException(temp, "There's no expression after || sign!");

            andExpression = new LogicExpression(andExpression, rightAndExpression, temp2);
        }
        return andExpression;
    }

    private Expression tryParseAndExpression() throws Exception
    {
        Expression relationalExpression;
        Expression rightRelationalExpression;
        Token temp, temp2;
        if((relationalExpression = tryParseRelationalExpression()) == null)
            return null;

        while(currentToken.getType() == TokenType.AND)
        {
            temp2 = currentToken;
            nextToken();
            temp = currentToken;
            if((rightRelationalExpression = tryParseRelationalExpression()) == null)
                throw new ParserException(temp, "There's no expression after && sign!");

            relationalExpression = new AndExpression(relationalExpression, rightRelationalExpression, temp2);
        }
        return relationalExpression;
    }

    private Expression tryParseRelationalExpression() throws Exception
    {
        Expression baseLogicExpression;
        Expression rightBaseLogicExpression;
        Token temp, relationalOperator;
        if((baseLogicExpression = tryParseBaseLogicExpression()) == null)
            return null;

        if(relationalOperations.contains(currentToken.getType()))
        {
            relationalOperator = currentToken;
            nextToken();
            temp = currentToken;
            if((rightBaseLogicExpression = tryParseBaseLogicExpression()) == null)
                throw new ParserException(temp, "There's no expression after relational operator!");

            baseLogicExpression = new RelationalExpression(baseLogicExpression, rightBaseLogicExpression, relationalOperator);
        }
        return baseLogicExpression;
    }

    private Expression tryParseBaseLogicExpression() throws Exception
    {
        Expression mathExpression;
        Token negate = null;
        if(currentToken.getType() == TokenType.NOT)
        {
            negate = currentToken;
            nextToken();
        }
        if((mathExpression = tryParseMathExpression()) == null)
            return null;
        if(negate != null)
        {
            return new BaseLogicExpression(mathExpression, negate);
        }
        return mathExpression;
    }

    private Expression tryParseMathExpression() throws Exception
    {
        Expression multiplicativeExpression;
        Expression rightMultiplicativeExpression;
        Token temp, operator;
        if((multiplicativeExpression = tryParseMultiplicativeExpression()) == null)
            return null;

        while(additiveOperators.contains(currentToken.getType()))
        {
            operator = currentToken;
            nextToken();
            temp = currentToken;
            if((rightMultiplicativeExpression = tryParseMultiplicativeExpression()) == null)
                throw new ParserException(temp, "There's no expression after additive operator!");

            multiplicativeExpression = new MathExpression(multiplicativeExpression, rightMultiplicativeExpression, operator);
        }

        return multiplicativeExpression;
    }

    private Expression tryParseMultiplicativeExpression() throws Exception
    {

        Expression baseMathExpression;
        Expression rightBaseMathExpression;
        Token temp, operator;
        if((baseMathExpression = tryParseBaseMathExpression()) == null)
            return null;

        while(multiplicativeOperators.contains(currentToken.getType()))
        {
            operator = currentToken;
            nextToken();
            temp = currentToken;
            if((rightBaseMathExpression = tryParseBaseMathExpression()) == null)
                throw new ParserException(temp, "There's no expression after multiplicative operator!");

            baseMathExpression = new MultiplicativeExpression(baseMathExpression, rightBaseMathExpression, operator);
        }

        return baseMathExpression;
    }

    private Expression tryParseBaseMathExpression() throws Exception
    {
        Expression value;
        Expression parentLogicExpression;
        Token minus = null;
        if(currentToken.getType() == TokenType.MINUS)
        {
            minus = currentToken;
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
        Token token;
        if(currentToken.getType() == TokenType.NUMBER)
        {
            int number = currentToken.getIntValue();
            token = currentToken;
            nextToken();
            return new Value(token, number);
        }

        if(currentToken.getType() == TokenType.FLOAT)
        {
            BigDecimal number = currentToken.getDoubleValue();
            token = currentToken;
            nextToken();
            return new Value(token, number);
        }

        if(currentToken.getType() == TokenType.TEXT)
        {
            String text = currentToken.getStringValue();
            token = currentToken;
            nextToken();
            return new Value(token, text);
        }

        token = currentToken;

        if((complex = tryParseComplex()) != null)
            return new Value(token, complex);

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
