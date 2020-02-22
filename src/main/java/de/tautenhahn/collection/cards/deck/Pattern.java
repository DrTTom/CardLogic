package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.TypeBasedEnumWithForeignKey;


/**
 * Standard patterns or "originell".
 *
 * @author TT
 */
public class Pattern extends TypeBasedEnumWithForeignKey
{

  /**
   * creates immutable instance
   */
  public Pattern()
  {
    super("pattern", "suits", 50, Flag.OPTIONAL);
  }

  @Override
  public String check(String value, DescribedObject context)
  {
    return "orig".equals(value) ? null : super.check(value, context);
  }
}
