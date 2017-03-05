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
   * Type to determine display / handling. TODO: replace by subtypes which create questions.
   */
  public enum Type
  {
    /** free text */
    TEXT,
    /** number */
    NUMERIC,
    /** list of allowed values taken from persistence, values backed up by objects */
    FOREIGN_KEY,
    /** reference to an image to display */
    IMAGE_REF,
    /** reference to some file to download */
    BLOB_REF
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

  public boolean isOptional()
  {
    return optional;
  }

  public boolean isSearchable()
  {
    return searchable;
  }

  public boolean isExact()
  {
    return exact;
  }

  /**
   * Returns null if value is OK and message otherwise.
   *
   * @param value
   * @param context provide a described object as context to allow the following methods changing behavior.
   *          For instance "Viena Pattern" is legal for French suits but forbidden in German or Spanish suits.
   */
  public final String checkValue(String value, DescribedObject context)
  {
    if (value == null)
    {
      return isOptional() ? null : "msg.error.missingValue";
    }
    return checkSpecific(value, context);
  }

  /**
   * Same as {@link #checkValue(String, DescribedObject)} but implementation may assume value is present.
   */
  protected abstract String checkSpecific(String value, DescribedObject context);

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
   * cannot recognize null values properly.
   *
   * @param object
   */
  public Question getQuestion(DescribedObject object)
  {
    ApplicationContext ctx = ApplicationContext.getInstance();
    Question result = new Question(name, ctx.getText(name + ".question.text"),
                                   ctx.getText(name + ".question.group"));
    result.setHelptext(ctx.getText(name + ".question.help"));
    result.setValue(Optional.ofNullable(object.getAttributes().get(name)).orElse(""));
    return result;
  }

}
