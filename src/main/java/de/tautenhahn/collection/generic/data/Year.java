package de.tautenhahn.collection.generic.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.question.Question;
import de.tautenhahn.collection.generic.data.question.TextQuestion;
import de.tautenhahn.collection.generic.persistence.Persistence;


/**
 * A year when something happened, providing restrictions to put events in correct chronological order.
 *
 * @author TT
 */
public class Year extends AttributeInterpreter
{

  private final List<String[]> notBefore = new ArrayList<>();

  private final List<String[]> notAfter = new ArrayList<>();

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
    return Optional.ofNullable(worstViolation(context, notBefore, year, true))
                   .map(a -> "msg.error.tooEarlyFor." + a)
                   .orElseGet(() -> Optional.ofNullable(worstViolation(context, notAfter, year, false))
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

  private String worstViolation(DescribedObject ctx, List<String[]> restr, int value, boolean lowerBound)
  {
    Integer defaultValue = lowerBound ? Integer.valueOf(0) : Integer.valueOf(3000);
    return restr.stream()
                .map(r -> getValue(ctx, r, defaultValue))
                .filter(p -> lowerBound ? getInt(p) > value : getInt(p) < value)
                .max((a, b) -> lowerBound ? getInt(a) - getInt(b) : getInt(b) - getInt(a))
                .map(Pair::getKey)
                .orElse(null);
  }

  private int getInt(Pair<String, Integer> p)
  {
    return p.getValue().intValue();
  }

  private Pair<String, Integer> getValue(DescribedObject obj, String[] attrib, Integer defaultValue)
  {
    DescribedObject current = obj;
    Integer resultValue = defaultValue;
    for ( int i = 0 ; i < attrib.length && current != null ; i++ )
    {
      String value = current.getAttributes().get(attrib[i]);
      if (value == null)
      {
        break;
      }
      if (i == attrib.length - 1)
      {
        try
        {
          resultValue = Integer.valueOf(value);
        }
        catch (NumberFormatException e)
        {
          break;
        }
      }
      else
      {
        current = persistence.find(attrib[i], value);
      }
    }
    return new Pair<>(attrib[0], resultValue);
  }

  /**
   * Adds the restriction that the value may not legally be before the value specified by attribute name
   *
   * @param attribName at least one name, several indicate an attribute of a sub-object
   */
  public void addNotBeforeRestriction(String... attribName)
  {
    if (attribName.length == 0)
    {
      throw new IllegalArgumentException("attribute name must be given");
    }
    notBefore.add(attribName);
  }

  /**
   * Same as {@link #addNotBeforeRestriction(String...)} but value may not be later that the other one.
   *
   * @param attribName
   */
  protected void addNotAfterRestriction(String... attribName)
  {
    if (attribName.length == 0)
    {
      throw new IllegalArgumentException("attribute name must be given");
    }
    notAfter.add(attribName);
  }

  /**
   * Not using existing pair classes due to access restrictions.
   */
  private static class Pair<S, T>
  {

    private final S key;

    private final T value;

    Pair(S key, T value)
    {
      this.key = key;
      this.value = value;
    }

    S getKey()
    {
      return key;
    }

    T getValue()
    {
      return value;
    }
  }
}
