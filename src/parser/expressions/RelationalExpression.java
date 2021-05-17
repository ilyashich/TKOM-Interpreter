package parser.expressions;

import lexer.TokenType;

import java.util.ArrayList;

public class RelationalExpression
{
    public ArrayList<BaseLogicExpression> expressions;
    public TokenType relationalOperator;
    public RelationalExpression(ArrayList<BaseLogicExpression> expressions, TokenType relationalOperator)
    {
        this.expressions = expressions;
        this.relationalOperator = relationalOperator;
    }

    public ArrayList<BaseLogicExpression> getExpressions()
    {
        return expressions;
    }

    public TokenType getRelationalOperator()
    {
        return relationalOperator;
    }
}
