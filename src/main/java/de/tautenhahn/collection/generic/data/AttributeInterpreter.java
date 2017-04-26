package de.tautenhahn.collection.generic.data;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import de.tautenhahn.collection.generic.ApplicationContext;


/**
 * How to handle an attribute of a described object.
 *
 * @author TT
 */
public abstract class AttributeInterpreter
{

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
    OPTIONAL, SEARCHABLE, EXACT
  }

  protected AttributeInterpreter(String name, Flag... flags)
  {
    this.name = name;
    List<Flag> flagList = Arrays.asList(flags);
    optional = flagList.contains(Flag.OPTIONAL);
    searchable = flagList.contains(Flag.SEARCHABLE);
    exact = flagList.contains(Flag.EXACT);
  }

  private final String name;

  private final boolean optional;

  private final boolean searchable;

  private final boolean exact;

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
   * @param value
   * @param context provide a described object as context to allow the following methods changing behavior.
   *          For instance "Viena Pattern" is legal for French suits but forbidden in German or Spanish suits.
   */
  public abstract String check(String value, DescribedObject context);

  public final Similarity computeCorrellation(String thisValue, String otherValue, DescribedObject context)
  {
    if (otherValue == null || thisValue == null)
    {
      return Similarity.NO_STATEMENT;
    }
    return correllateValue(thisValue, otherValue, context);
  }

  protected abstract Similarity correllateValue(String thisValue, String otherValue, DescribedObject context);

  /**
   * Returns a default question for that attribute. Overwrite in case you want a more specific question. Note
   * that an empty String is filled in instead of a missing value because HTML and JavaScript front ends
   * cannot recognize null values properly and we do not want "undefined" pre-filled into input elements.
   *
   * @param object
   */
  public Question getQuestion(DescribedObject object)
  {
    ApplicationContext ctx = ApplicationContext.getInstance();
    Question result = new Question(name, ctx.getText(name + ".question.text"),
                                   ctx.getText(name + ".question.group"));
    result.setHelptext(ctx.getText(name + ".question.help"));
    result.setValue(Optional.ofNullable(toDisplayValue(object.getAttributes().get(name))).orElse(""));
    return result;
  }

  protected String realValue(String value)
  {
    return value == null ? null : value.trim().isEmpty() ? null : value;
  }

  /**
   * Returns the value to display for representing given internal value. Values may be different if internal
   * value is some artificial key or display value may contain characters not suitable for internal use.
   *
   * @param internalValue
   */
  public String toDisplayValue(String internalValue)
  {
    return internalValue;
  }

  /**
   * Inverse of {@link #toDisplayValue(String)}
   *
   * @param displayValue
   */
  public String toInternalValue(String displayValue)
  {
    return displayValue;
  }

}
