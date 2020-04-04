package de.tautenhahn.collection.generic.data;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.question.Question;
import lombok.Getter;


/**
 * How to handle an attribute of a described object.
 *
 * @author TT
 */
public abstract class AttributeInterpreter
{

  @Getter
  private final String name;

  @Getter
  private final boolean optional;

  @Getter
  private final boolean searchable;

  @Getter
  private final boolean exact;

  String keyPrefix = NOT_SPECIFIED;

  /**
   * Representation of null values in the front end.
   */
  protected static final String NOT_SPECIFIED = "";

  /**
   * Creates new instance.
   *
   * @param name name of attribute
   * @param flags specifies properties of that attribute
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
   * Specify an additional prefix for message keys
   * 
   * @param value free string
   */
  public void setKeyPrefix(String value)
  {
    keyPrefix = value + ".";
  }

  /**
   * Returns null if value is OK and message otherwise. System will call this method only if values is
   * present.
   *
   * @param value internal value as present in the object
   * @param context provide a described object as context to allow the following methods changing behavior.
   *          For instance "Vienna Pattern" is legal for French suits but forbidden in German or Spanish
   *          suits.
   * @return null or error message
   */
  public abstract String check(String value, DescribedObject context);

  /**
   * Returns a value indicating whether two values could be for the same object. See {@link Similarity} for
   * sensible values.
   *
   * @param thisValue one value to compare
   * @param otherValue one value to compare
   * @param context other attributes may influence interpretation of this attributes value
   * @return how similar the values are 
   */
  public final Similarity computeCorrelation(String thisValue, String otherValue, DescribedObject context)
  {
    if (otherValue == null || thisValue == null)
    {
      return Similarity.NO_STATEMENT;
    }
    return correlateValue(thisValue, otherValue, context);
  }

  /**
   * Same as {@link #computeCorrelation(String, String, DescribedObject)} but never called with null
   * arguments.
   * 
   * @param thisValue one value to compare
   * @param otherValue one value to compare
   * @param context other attributes may influence interpretation of this attributes value
   * @return how similar the values are
   */
  protected abstract Similarity correlateValue(String thisValue, String otherValue, DescribedObject context);

  /**
   * Returns the question for that attribute already knowing something about the object asked for.
   *
   * @param object already known values
   * @return may be null if nothing to ask
   */
  public abstract Question getQuestion(DescribedObject object);

  /**
   * Note that an empty String is filled in instead of a missing value because HTML and JavaScript front ends
   * cannot recognize null values properly and we do not want "undefined" pre-filled into input elements.
   *
   * @param object what is already known about the object
   * @param constructor returns the question, expects text and group
   * @return Question for the current fields value.
   */
  protected <T extends Question> T createQuestion(DescribedObject object,
                                                  BiFunction<String, String, T> constructor)
  {
    ApplicationContext ctx = ApplicationContext.getInstance();
    String key = keyPrefix + name + ".question.";
    T result = constructor.apply(ctx.getText(key + "text"), ctx.getText(key + "group"));
    result.setHelptext(ctx.getText(key + "help"));
    result.setValue(Optional.ofNullable(object.getAttributes().get(name)).orElse(NOT_SPECIFIED));
    return result;
  }

  /**
   * Translates empty Strings to null (value not given).
   * 
   * @param value any string
   * @return value or null if value is empty (blank characters ignored)
   */
  protected String dropEmptyString(String value)
  {
    return value == null || value.isBlank() ? null : value;
  }

  /**
   * Returns the value to display for representing given internal value. Values may be different whenever
   * internally used values do not look good. Reasons may be that some artificial keys are used internally.
   * <br>
   * By default, internally missing values are represented by empty String because most front ends can not
   * handle null values in both directions.
   *
   * @param internalValue value as in the object
   * @return value to display
   */
  public String toDisplayValue(String internalValue)
  {
    return internalValue == null ? "" : internalValue;
  }

  /**
   * makes the super() call easier
   */
  public enum Flag
  {
    /**
     * value may be missing
     */
    OPTIONAL,
    /**
     * value contributes to search index
     */
    SEARCHABLE,

    /**
     * value can be exactly determined by given object, i.e. equal objects have always same value
     */
    EXACT,

    /**
     * For an enum value, explicitly stating "none" is allowed like in "No price tag".
     */
    SUPPORTS_NONE
  }
}
