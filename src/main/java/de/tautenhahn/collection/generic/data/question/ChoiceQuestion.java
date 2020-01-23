package de.tautenhahn.collection.generic.data.question;

import java.util.Map;

/**
 * Question for one of several allowed values.
 *
 * @author TT
 */
public class ChoiceQuestion extends Question
{

    private Map<String, String> options;

    /**
     * Creates instance.
     *
     * @param type
     * @param paramName
     * @param text
     * @param form
     */
    protected ChoiceQuestion(Type type, String paramName, String text, String form)
    {
        super(type, paramName, text, form);
    }

    /**
     * Returns list of options to display in a selection input element.
     *
     * @return keys are internal values, values are display names
     */
    public Map<String, String> getOptions()
    {
        return options;
    }

    /**
     * Specifies which options to display to the user.
     *
     * @param options keys are internal values, values are display names
     */
    public void setOptions(Map<String, String> options)
    {
        this.options = options;
    }
}
