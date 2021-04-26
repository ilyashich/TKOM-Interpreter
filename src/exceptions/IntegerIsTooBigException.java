package exceptions;

import source.Position;

public class IntegerIsTooBigException extends Exception
{
    private Position position;
    public IntegerIsTooBigException(Position position)
    {
        this.position = position;
    }

    @Override
    public String toString()
    {
        return "Integer is too big at position: " + position;
    }
}
