package parser;

import lexer.TokenType;
import parser.statements.Statement;
import parser.statements.StatementBlock;

import java.util.ArrayList;

public class FunctionDefinition extends Statement
{
    private Identifier identifier;
    private ArrayList<Identifier> params;
    private StatementBlock statementBlock;

    public FunctionDefinition(Identifier identifier, ArrayList<Identifier> params, StatementBlock statementBlock)
    {
        this.identifier = identifier;
        this.params = params;
        this.statementBlock = statementBlock;
    }

    public Identifier getIdentifier()
    {
        return identifier;
    }

    public ArrayList<Identifier> getParams()
    {
        return params;
    }

    public StatementBlock getStatementBlock()
    {
        return statementBlock;
    }
}
