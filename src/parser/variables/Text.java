package parser.variables;

import parser.Value;

public class Text extends Value
{
    private String text;

    public Text(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }
}
