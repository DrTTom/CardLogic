package de.tautenhahn.collection.generic.data.question;

import lombok.Data;

/**
 * Defines how to get information from the front end / how to ask the user.
 *
 * @author TT
 */
@Data
public class Question
{

    private final String paramName;

    private final String text;

    private final String form;

    private final String type;

    private String value;
    private String helptext;

    private boolean alignWithPrevious;

    private String problem;

    /**
     * Creates instance setting the mandatory parameters.
     *
     * @param paramName
     * @param text
     * @param form
     */
    protected Question(String type, String paramName, String text, String form)
    {
        this.type = type;
        this.paramName = paramName;
        this.text = text;
        this.form = form;
    }

    /**
     * Returns the answer if already known.
     */
    public String getValue()
    {
        return "".equals(value) ? null: value;
    }

    /**
     * @param value
     * @see #getValue()
     */
    public void setValue(String value)
    {
        this.value = value==null ? "": value;
    }
}
