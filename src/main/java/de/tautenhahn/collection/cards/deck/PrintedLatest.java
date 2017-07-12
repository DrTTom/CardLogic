package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.Year;
import de.tautenhahn.collection.generic.data.question.Question;


/**
 * Some latest year the actual deck could have been printed.
 *
 * @author TT
 */
public class PrintedLatest extends Year
{

  PrintedLatest()
  {
    super("printedLatest", Flag.OPTIONAL);
    addNotBeforeRestriction("maker", "from");
    addNotBeforeRestriction("makerSign", "usedFrom");
    addNotBeforeRestriction("taxStamp", "usedFrom");
    addNotBeforeRestriction("printedEarliest");
    addNotAfterRestriction("maker", "to");
    addNotAfterRestriction("makerSign", "usedTo");
    addNotAfterRestriction("taxStamp", "usedTo");
  }

  @Override
  public Question getQuestion(DescribedObject object)
  {
    return super.getQuestion(object).alignWithPrevious();
  }
}
