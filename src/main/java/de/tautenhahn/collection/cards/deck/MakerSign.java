package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.TypeBasedEnumWithForeignKey;
import de.tautenhahn.collection.generic.data.question.Question;


/**
 * Defines the makers logo on the playing cards.
 *
 * @author TT
 */
public class MakerSign extends TypeBasedEnumWithForeignKey
{

  /**
   * Creates immutable instance.
   */
  public MakerSign()
  {
    super("makerSign", "maker", 50, Flag.OPTIONAL, Flag.EXACT);
  }

  @Override
  public Question getQuestion(DescribedObject object)
  {
    return getImageQuestion(object, "auto", "90");
  }
}
