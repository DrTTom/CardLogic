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

    private final Type type;

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
    protected Question(Type type, String paramName, String text, String form)
    {
        this.type = type;
        this.paramName = paramName;
        this.text = text;
        this.form = form;
    }

    /**
     * Returns the type of question.
     */
    public Type getType()
    {
        return type;
    }

    /**
     * Returns an additional explaining text.
     */
    public String getHelptext()
    {
        return helptext;
    }

    /**
     * @param helptext
     * @see #getHelptext()
     */
    public void setHelptext(String helptext)
    {
        this.helptext = helptext;
    }

    /**
     * Returns name of a form this question belongs to. Should match some view template.
     */
    public String getForm()
    {
        return form;
    }

    /**
     * Returns name of parameter to return the answer in.
     */
    public String getParamName()
    {
        return paramName;
    }

    /**
     * Returns question text.
     */
    public String getText()
    {
        return text;
    }

    /**
     * Returns the answer if already known.
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @param value
     * @see #getValue()
     */
    public void setValue(String value)
    {
        this.value = value;
    }

    /**
     * Returns a human-readable message in case the value is wrong, null if OK.
     */
    public String getProblem()
    {
        return problem;
    }

    /**
     * @param problem
     * @see Question#getProblem()
     */
    public void setProblem(String problem)
    {
        this.problem = problem;
    }

    /**
     * Returns true if question should displayed together with previous one.
     */
    public boolean isAlignWithPrevious()
    {
        return alignWithPrevious;
    }

    /**
     * Declares the question to be displayed together with the previous one.
     */
    public Question alignWithPrevious()
    {
        alignWithPrevious = true;
        return this;
    }

    /**
     * Type of input required to answer the question.
     */
    public enum Type
    {
        /**
         * free text
         */
        TEXT,
        /**
         * choice among given text phrases
         */
        TEXT_CHOICE,
        /**
         * choice among given images
         */
        IMAGE_CHOICE,
        /**
         * choice among given objects by name
         */
        OBJECT_CHOICE,
        /**
         * file upload
         */
        FILE
    }
}
