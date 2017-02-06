package de.tautenhahn.collection.generic.data;

import java.util.Arrays;
import java.util.List;


/**
 * Attribute of a described object with information on how it should be handled. <br>
 * TODO define an interpreter which can be re-used.
 *
 * @author TT
 */
public abstract class InterpretedAttribute
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
    /** fixed list of allowed values */
    ENUM,
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



  protected InterpretedAttribute(String name, Flag... flags)
  {
    this.name = name;
    List<Flag> flagList = Arrays.asList(flags);
    optional = flagList.contains(Flag.OPTIONAL);
    searchable = flagList.contains(Flag.SEARCHABLE);
    exact = flagList.contains(Flag.EXACT);

  }

  private final String name;

  protected String value;

  private final boolean optional;

  private final boolean searchable;

  private final boolean exact;



  public String getValue()
  {
    return value;
  }


  public void setValue(String value)
  {
    this.value = value;
  }


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

  abstract boolean isLegalValue();

  public final int computeCorrellation(InterpretedAttribute other)
  {
    if (other == null || other.value == null || value == null || !getClass().isInstance(other))
    {
      return 0;
    }
    return correllateValue(other.value);
  }

  abstract int correllateValue(String otherValue);

}
