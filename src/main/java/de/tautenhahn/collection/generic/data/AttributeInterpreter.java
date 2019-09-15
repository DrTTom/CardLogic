package de.tautenhahn.collection.generic.data;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.question.Question;


/**
 * How to handle an attribute of a described object.
 *
 * @author TT
 */
public abstract class AttributeInterpreter
{

  private final String name;

  private final boolean optional;

  private final boolean searchable;

  private final boolean exact;

  /**
   * Creates new instance.
   *
   * @param name
   * @param flags
   */
  protected AttributeInterpreter(String name, Flag... flags)
  {
    this.name = name;
    List<Flag> flagList = Arrays.asList(flags);
    optional = flagList.contains(Flag.OPTIONAL);
    searchable = flagList.contains(Flag.SEARCHABLE);
    exact = flagList.contains(Flag.EXACT);
  }


  /**
   * Returns the attribute name.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns true if value may be omitted.
   */
  public boolean isOptional()
  {
    return optional;
  }

  /**
   * Returns true if value should be added to a search index, thus enabling free text search. Otherwise, the
   * attribute is searchable via its {@link #computeCorrellation(String, String, DescribedObject)} method.
   */
  public boolean isSearchable()
  {
    return searchable;
  }

  /**
   * Returns true if for the object this attribute can always be determined with certainty.
   */
  public boolean isExact()
  {
    return exact;
  }

  /**
   * Returns null if value is OK and message otherwise. System will call this method only if values is
   * present.
   *
   * @param value   internal value as present in the object
   * @param context provide a described object as context to allow the following methods changing behavior.
   *                For instance "Vienna Pattern" is legal for French suits but forbidden in German or Spanish
   *                suits.
   */
  public abstract String check(String value, DescribedObject context);

  /**
   * Returns a value indicating whether two values could be for the same object.
   *
   * @param thisValue
   * @param otherValue
   * @param context
   */
  public final Similarity computeCorrellation(String thisValue, String otherValue, DescribedObject context)
  {
    if (otherValue == null || thisValue == null)
    {
      return Similarity.NO_STATEMENT;
    }
    return correllateValue(thisValue, otherValue, context);
  }

  /**
   * Same as {@link #computeCorrellation(String, String, DescribedObject)} but never called with null
   * arguments.
   */
  protected abstract Similarity correllateValue(String thisValue, String otherValue, DescribedObject context);

  /**
   * Returns the question for that attribute already knowing something about the object asked for.
   *
   * @param object
   */
  public abstract Question getQuestion(DescribedObject object);


  /**
   * Note that an empty String is filled in instead of a missing value because HTML and JavaScript front ends
   * cannot recognize null values properly and we do not want "undefined" pre-filled into input elements.
   *
   * @param object
   * @param constructor returns the question, expects text and group
   */
  protected <T extends Question> T createQuestion(DescribedObject object,
                                                  BiFunction<String, String, T> constructor)
  {
    ApplicationContext ctx = ApplicationContext.getInstance();
    T result = constructor.apply(ctx.getText(name + ".question.text"), ctx.getText(name + ".question.group"));
    result.setHelptext(ctx.getText(name + ".question.help"));
    result.setValue(toDisplayValue(object.getAttributes().get(name)));
    return result;
  }

  /**
   * Translates empty Strings to null (value not given).
   */
  protected String dropEmptyString(String value)
  {
    return value == null ? null : value.chars().allMatch(Character::isWhitespace) ? null : value;
  }

  /**
   * Returns the value to display for representing given internal value. Values may be different whenever
   * internally used values do not look good. Reasons may be that some artificial keys are used internally.
   * <br>
   * By default, internally missing values are represented by empty String because most front ends can not
   * handle null values in both directions.
   *
   * @param internalValue
   */
  public String toDisplayValue(String internalValue)
  {
    return internalValue == null ? "" : internalValue;
  }

  /**
   * Inverse of {@link #toDisplayValue(String)}
   *
   * @param displayValue value in font end
   */
  public String toInternalValue(String displayValue)
  {
    return dropEmptyString(displayValue);
  }

  /**
   * Marker interface to indicate that internal and display values may differ.
   */
  public interface Translating
  {
    // marker interface only
  }

  /** makes the super() call easier */
  public enum Flag
  {
    /** value may be missing */
    OPTIONAL,
    /** value contributes to search index */
    SEARCHABLE,

    /** value can be exactly determined by given object, i.e. equal objects have always same value */
    EXACT
  }

}
