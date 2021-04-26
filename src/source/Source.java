package source;

import java.io.IOException;
import java.io.InputStream;
public class Source
{
    public static final int NEW_LINE_ASCII = 10;
    public static final int EOF = -1;
    private Position position;
    private final InputStream inputStream;

    public Source(InputStream inputStream)
    {
        this.position = new Position();
        this.inputStream = inputStream;
    }

    public int getNext() throws IOException
    {
        int character;
        if((character = inputStream.read()) != EOF)
        {
            if (character == NEW_LINE_ASCII)
            {
                position.column = 0;
                position.line++;
            }
            else
                position.column++;
            return character;
        }
        else inputStream.close();
        return EOF;
    }

    public Position getPosition()
    {
        return position;
    }
}
