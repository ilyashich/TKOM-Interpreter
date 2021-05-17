package parser.expressions;

import lexer.TokenType;

import java.util.ArrayList;

public class MultiplicativeExpression
{
    public ArrayList<BaseMathExpression> expressions;
    public ArrayList<TokenType> operators;
    public MultiplicativeExpression(ArrayList<BaseMathExpression> expressions, ArrayList<TokenType> operators)
    {
        this.expressions = expressions;
        this.operators = operators;
    }
}
