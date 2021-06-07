package exceptions;

import lexer.Token;
import lexer.TokenType;
import source.Position;

import java.util.ArrayList;

public class ParserException extends Exception
{

    private final Token currentToken;
    private final ArrayList<TokenType> expected;
    private final Position position;
    private final String msg;

    public ParserException(Token currentToken, TokenType expected, Position position)
    {
        this.currentToken = currentToken;
        this.expected = new ArrayList<>();
        this.expected.add(expected);
        this.position = position;
        this.msg = ("unexpected token " + currentToken +
                " expected tokens: " + expected.toString() +
                " at position " + position.toString());
    }

    public ParserException(Token currentToken, String message)
    {
        this.currentToken = currentToken;
        this.expected = new ArrayList<>();
        this.position = currentToken.getPosition();
        this.msg = message + "." + " Unexpected token " + currentToken;
    }

    public ParserException(Token currentToken, ArrayList<TokenType> expected, Position position)
    {
        this.currentToken = currentToken;
        this.expected = new ArrayList<>();
        this.expected.addAll(expected);
        this.position = position;
        this.msg = ("unexpected token " + currentToken +
                " expected tokens: " + expected.toString().replace("[", "").replace("]", "")
                + " at position " + position.toString());
    }

    @Override
    public String toString() {
        return msg;
    }
}
