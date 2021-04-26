package source;

public class Position
{
    public int line;
    public int column;

    public Position(int line, int column)
    {
        this.line = line;
        this.column = column;
    }

    public Position(Position pos)
    {
        this.line = pos.line;
        this.column = pos.column;
    }

    public Position()
    {
        line = 1;
        column = 0;
    }

    @Override
    public String toString()
    {
        return "Line: " + line + ", Column: " + column;
    }

}
