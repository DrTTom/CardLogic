package de.tautenhahn.collection.generic.data;

import de.tautenhahn.collection.generic.data.question.Question;
import de.tautenhahn.collection.generic.data.question.TextQuestion;


/**
 * Generic free text property.
 *
 * @author TT
 */
public class FreeText extends AttributeInterpreter
{

  private final int len;

  private final int lines;

  /**
   * Creates new instance
   *
   * @param name
   * @param flags
   */
  public FreeText(String name, int len, int lines, Flag... flags)
  {
    super(name, flags);
    this.len = len;
    this.lines = lines;
  }

  @Override
  public String check(String value, DescribedObject context)
  {
    return null;
  }

  @Override
  protected Similarity correllateValue(String thisValue, String otherValue, DescribedObject context)
  {
    return thisValue.equals(otherValue) ? Similarity.HINT : Similarity.NO_STATEMENT;
  }

  @Override
  public Question getQuestion(DescribedObject object)
  {
    return createQuestion(object, (text, group) -> new TextQuestion(getName(), text, group, len, lines));
  }
}
