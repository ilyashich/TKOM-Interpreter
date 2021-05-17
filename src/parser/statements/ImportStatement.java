package parser.statements;

public class ImportStatement extends Statement
{
    private String fileName;
    public ImportStatement(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }
}
