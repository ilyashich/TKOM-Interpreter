package exceptions;

import lexer.Token;
import lexer.TokenType;
import source.Position;

import java.util.ArrayList;

public class ParserException extends Exception {

    private Token currentToken;
    private ArrayList<TokenType> expected;
    private Position position;
    private String msg;

    public ParserException(Token currentToken, TokenType expected, Position position) {
        this.currentToken = currentToken;
        this.expected = new ArrayList<>();
        this.expected.add(expected);
        this.position = position;
        this.msg = ("unexpected token " + currentToken +
                " expected tokens: " + expected.toString() +
                " at position " + position.toString());
    }

    public ParserException(String msg) {
        this.msg = msg;
    }

    public ParserException(Token currentToken, ArrayList<TokenType> expected, Position position) {
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
