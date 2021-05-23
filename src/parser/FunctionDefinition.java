package parser;

import lexer.TokenType;
import parser.statements.Statement;
import parser.statements.StatementBlock;

import java.util.ArrayList;

public class FunctionDefinition extends Statement
{
    private String identifier;
    private ArrayList<String> params;
    private StatementBlock statementBlock;

    public FunctionDefinition(String identifier, ArrayList<String> params, StatementBlock statementBlock)
    {
        this.identifier = identifier;
        this.params = params;
        this.statementBlock = statementBlock;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public ArrayList<String> getParams()
    {
        return params;
    }

    public StatementBlock getStatementBlock()
    {
        return statementBlock;
    }
}
