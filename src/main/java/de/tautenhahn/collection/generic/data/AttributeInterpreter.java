package de.tautenhahn.collection.generic.data;

import java.util.Arrays;
import java.util.List;


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

  public abstract boolean isLegalValue(String value);

  public final int computeCorrellation(String thisValue, String otherValue)
  {
    if (otherValue == null || thisValue == null)
    {
      return 0;
    }
    return correllateValue(thisValue, otherValue);
  }

  abstract int correllateValue(String thisValue, String otherValue);

}
