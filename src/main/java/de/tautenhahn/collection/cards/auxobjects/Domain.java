package de.tautenhahn.collection.cards.auxobjects;

import de.tautenhahn.collection.generic.data.FixedEnumeration;


/**
 * Just a Germany - centered way to divide makers into groups.
 *
 * @author TT
 */
public class Domain extends FixedEnumeration
{

  /**
   * Creates instance.
   */
  protected Domain()
  {
    super("domain", 0, new String[]{"DE", "EU", "WW"}, Flag.EXACT);
  }
}
