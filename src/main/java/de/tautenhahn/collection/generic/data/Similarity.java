package de.tautenhahn.collection.generic.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


/**
 * Describes how similar two objects are. In general, there will be no sensible absolute values.
 *
 * @author TT
 */
@ToString
@EqualsAndHashCode
public class Similarity
{

  /**
   * Object are surely not equal.
   */
  public static final Similarity DIFFERENT = new Similarity(-1);

  /**
   * No hint whether objects are equal or not,
   */
  public static final Similarity NO_STATEMENT = new Similarity(0);

  /**
   * Weak hint that objects may be equal.
   */
  public static final Similarity HINT = new Similarity(1);

  /**
   * Objects match better than best 10% of randomly chosen objects
   */
  public static final Similarity SIMILAR = new Similarity(100);

  /**
   * half as good as {@link #SIMILAR}
   */
  public static final Similarity ALMOST_SIMILAR = new Similarity(50);

  @Getter
  private final int value;

  /**
   * Creates immutable instance with given value.
   *
   * @param value -1 for definitely different, 0 for no hint, positive values indicate the amount of
   *          similarity.
   */
  public Similarity(int value)
  {
    this.value = value;
  }

  /**
   * Returns a combined Similarity guess.
   * 
   * @param other value to add
   * @return verdict
   */
  public Similarity add(Similarity other)
  {
    if (value < 0 || other.value < 0)
    {
      return DIFFERENT;
    }
    if (other.value == 0)
    {
      return this;
    }
    return new Similarity(value + other.value < 0 ? Integer.MAX_VALUE : value + other.value);
  }

  /**
   * @return true if objects may be equal.
   */
  public boolean possiblyEqual()
  {
    return value >= 0;
  }
}
