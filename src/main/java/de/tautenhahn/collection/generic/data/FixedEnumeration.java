package de.tautenhahn.collection.generic.data;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.question.Question;
import de.tautenhahn.collection.generic.data.question.TextChoiceQuestion;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Enumeration with fixed set of allowed String values. Values of other types require a {@link TypeBasedEnumeration}.
 *
 * @author TT
 */
public abstract class FixedEnumeration extends Enumeration
{

    private final List<String> allowedValues;

    /**
     * Creates new instance.
     *
     * @param name
     * @param matchValue
     * @param allowed
     * @param flags
     */
    protected FixedEnumeration(String name, int matchValue, String[] allowed, Flag... flags)
    {
        super(name, matchValue, flags);
        allowedValues = List.of(allowed);
    }

    /**
     * Returns array of translated values based on given message keys. TODO: change data to use key instead of
     * internationalized value!
     *
     * @param paramName
     * @param keys
     */
    protected static String[] resolveKeys(String paramName, String... keys)
    {
        String[] result = new String[keys.length];
        for (int i = 0; i < keys.length; i++)
        {
            result[i] = ApplicationContext.getInstance().getText(paramName + ".value." + keys[i]);
        }
        return result;
    }

    @Override
    public String check(String value, DescribedObject context)
    {
        return getAllowedValues(context).contains(value) ? null : "msg.error.invalidOption";
    }

    /**
     * Returns choice question where options are in sequence as is and null placeholder is added. Invalid existing value
     * is added at the end.
     */
    @Override
    public Question getQuestion(DescribedObject object)
    {
        TextChoiceQuestion result =
            createQuestion(object, (text, group) -> new TextChoiceQuestion(getName(), text, group));
        result.setOptions(getOptions(object));
        return result;
    }

    @Override
    public List<String> getAllowedValues(DescribedObject context)
    {
        return allowedValues;
    }
}
