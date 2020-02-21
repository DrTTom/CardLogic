package de.tautenhahn.collection.generic.data;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.question.Question;
import de.tautenhahn.collection.generic.data.question.TextQuestion;
import de.tautenhahn.collection.generic.persistence.Persistence;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A year when something happened, providing restrictions to put events in correct chronological order.
 *
 * @author TT
 */
public class Year extends AttributeInterpreter
{

    private final List<ReferencedValue> notBefore = new ArrayList<>();

    private final List<ReferencedValue> notAfter = new ArrayList<>();

    Persistence persistence;

    /**
     * Creates new instance.
     *
     * @param name
     * @param flags
     */
    public Year(String name, Flag... flags)
    {
        super(name, flags);
    }

    @Override
    public String check(String value, DescribedObject context)
    {
        if (!value.matches("[12][0-9][0-9][0-9]"))
        {
            return "msg.error.mustBeYear";
        }
        if (persistence == null)
        {
            persistence = ApplicationContext.getInstance().getPersistence();
        }
        int year = Integer.parseInt(value);
        return Optional
            .ofNullable(worstViolation(context, notBefore, year, true))
            .map(a -> "msg.error.tooEarlyFor." + a)
            .orElseGet(() -> Optional
                .ofNullable(worstViolation(context, notAfter, year, false))
                .map(a -> "msg.error.tooLateFor." + a)
                .orElse(null));
    }

    @Override
    protected Similarity correllateValue(String thisValue, String otherValue, DescribedObject content)
    {
        return thisValue.equals(otherValue) ? Similarity.HINT : Similarity.NO_STATEMENT;
    }

    @Override
    public Question getQuestion(DescribedObject object)
    {
        TextQuestion result = createQuestion(object, (text, group) -> new TextQuestion(getName(), text, group));
        result.setFormat(1, 4);
        return result;
    }

    private String worstViolation(DescribedObject ctx, List<ReferencedValue> restr, int value, boolean lowerBound)
    {
        Integer defaultValue = lowerBound ? Integer.valueOf(0) : Integer.valueOf(3000);
        return restr
            .stream()
            .peek(r -> r.setUpValue(ctx, defaultValue, persistence))
            .filter(p -> lowerBound ? p.getValue() > value : p.getValue() < value)
            .max((a, b) -> lowerBound ? a.getValue() - b.getValue() : b.getValue() - a.getValue())
            .map(r -> keyPrefix + r.getPath()[0])
            .orElse(null);
    }

    /**
     * Adds the restriction that the value may not legally be before the value specified by attribute name
     *
     * @param path at least one name, several indicate an attribute of a sub-object
     */
    public void addNotBeforeRestriction(String... path)
    {
        addRestriction(notBefore, path);
    }

    /**
     * Same as {@link #addNotBeforeRestriction(String...)} but value may not be later that the other one.
     *
     * @param path at least one name, several indicate an attribute of a sub-object
     */
    protected void addNotAfterRestriction(String... path)
    {
        addRestriction(notAfter, path);
    }

    private void addRestriction(List<ReferencedValue> type, String... path)
    {
        if (path.length == 0)
        {
            throw new IllegalArgumentException("attribute name must be given");
        }

        type.add(new ReferencedValue(path));
    }

    /**
     * Value to compare to and how it is called.
     */
    @Data
    private static class ReferencedValue
    {
        String[] path;
        int value;

        public ReferencedValue(String[] path)
        {
            this.path = path;
        }

        void setUpValue(DescribedObject obj, int defaultValue, Persistence persistence)
        {
            value = defaultValue;
            DescribedObject current = obj;
            for (int i = 0; i < path.length; i++)
            {
                String attrValue = current == null ? null : current.getAttributes().get(path[i]);
                if (attrValue == null)
                {
                    return;
                }
                if (i == path.length - 1)
                {
                    try
                    {
                        value = Integer.valueOf(attrValue);
                    } catch (NumberFormatException e)
                    {
                        break;
                    }
                } else
                {
                    current = persistence.find(path[i], attrValue);
                }
            }
        }
    }
}
