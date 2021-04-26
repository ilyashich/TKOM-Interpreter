package exceptions;

import source.Position;

public class IdentifierIsTooLargeException extends Exception
{
    private Position position;
    public IdentifierIsTooLargeException(Position position)
    {
        this.position = position;
    }

    @Override
    public String toString()
    {
        return "Identifier is too large at position: " + position;
    }
}
