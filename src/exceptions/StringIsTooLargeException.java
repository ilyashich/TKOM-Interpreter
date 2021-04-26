package exceptions;

import source.Position;

public class StringIsTooLargeException extends Exception
{
    private Position position;
    public StringIsTooLargeException(Position position)
    {
        this.position = position;
    }

    @Override
    public String toString()
    {
        return "String is too large at position: " + position;
    }
}
