package de.tautenhahn.collection.generic.data.question;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Question for one of several allowed values.
 *
 * @author TT
 */
public class ChoiceQuestion extends Question
{
    @Getter
    @Setter
    private Map<String, String> options;

    /**
     * Creates instance.
     *
     * @param type specifies which type of input element is needed
     * @param paramName name of the objects attribute
     * @param text text to display
     * @param form group this question belongs to
     */
    protected ChoiceQuestion(String type, String paramName, String text, String form)
    {
        super(type, paramName, text, form);
    }
}
