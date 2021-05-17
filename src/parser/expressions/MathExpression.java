package parser.expressions;

import lexer.TokenType;

import java.util.ArrayList;

public class MathExpression
{
    public ArrayList<MultiplicativeExpression> expressions;
    public ArrayList<TokenType> operators;
    public MathExpression(ArrayList<MultiplicativeExpression> expressions, ArrayList<TokenType> operators)
    {
        this.expressions = expressions;
        this.operators = operators;
    }
}
