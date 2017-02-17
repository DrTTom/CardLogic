package de.tautenhahn.collection.generic.data;

/**
 * Describes how similar two objects are. In general, there will be no sensible absolute values.
 *
 * @author TT
 */
public class Similarity implements Comparable<Similarity>
{

  public static final Similarity DIFFERENT = new Similarity(-1);

  public static final Similarity NO_STATEMENT = new Similarity(0);

  public static final Similarity HINT = new Similarity(1);

  public static final Similarity SIMILAR = new Similarity(100);

  public static final Similarity ALMOST_SIMILAR = new Similarity(50);

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

  @Override
  public int compareTo(Similarity o)
  {
    return value - o.value;
  }

  public boolean possiblyEqual()
  {
    return value >= 0;
  }

  public boolean probablyEqual()
  {
    return value >= 50;
  }
}
