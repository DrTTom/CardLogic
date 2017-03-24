package de.tautenhahn.collection.generic.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.Question;


/**
 * Contains all data created by a search process. Note that search will be active whenever data is entered,
 * namely
 * <ul>
 * <li>just browsing through the collection</li>
 * <li>submitting a new object</li>
 * <li>editing an existing object</li>
 * </ul>
 *
 * @author TT
 */
public class SearchResult
{

  private final String type;

  private List<Question> questions;

  private String message;

  private int numberTotal;

  private int numberMatching;

  private int numberPossible;

  private List<DescribedObject> matches = new ArrayList<>();

  private Map<String, Map<String, String>> translations;

  private final String primKeyOfEditedObject;

  private final boolean forSubmit;


  /**
   * Creates new object setting some context data which allows state-free front ends.
   *
   * @param type type of described objects(s)
   * @param primKeyOfEditedObject optional, if specified primKey value of the existing object which is
   *          currently edited.
   * @param forSubmit true in case user already requested submitting of entered data but had to do some
   *          corrections before.
   */
  public SearchResult(String type, String primKeyOfEditedObject, boolean forSubmit)
  {
    this.type = type;
    this.primKeyOfEditedObject = primKeyOfEditedObject;
    this.forSubmit = forSubmit;
  }

  /**
   * Returns type of searched objects.
   */
  public String getType()
  {
    return type;
  }

  public List<Question> getQuestions()
  {
    return questions;
  }

  public void setQuestions(List<Question> questions)
  {
    this.questions = questions;
  }


  public String getMessage()
  {
    return message;
  }

  public void setMessages(String message)
  {
    this.message = message;
  }

  public int getNumberTotal()
  {
    return numberTotal;
  }

  public void setNumberTotal(int numberTotal)
  {
    this.numberTotal = numberTotal;
  }

  public List<DescribedObject> getMatches()
  {
    return matches;
  }

  public void setMatches(List<DescribedObject> matches)
  {
    this.matches = matches;
  }

  public int getNumberMatching()
  {
    return numberMatching;
  }


  public void setNumberMatching(int numberMatching)
  {
    this.numberMatching = numberMatching;
  }


  public int getNumberPossible()
  {
    return numberPossible;
  }

  public void setNumberPossible(int numberPossible)
  {
    this.numberPossible = numberPossible;
  }

  public Map<String, Map<String, String>> getTranslations()
  {
    return translations;
  }

  /**
   * Declares how a String can be translated into human.readable text.
   *
   * @param attrName
   * @param orig
   * @param translation
   */
  public void addTranslation(String attrName, String orig, String translation)
  {
    if (translations == null)
    {
      translations = new HashMap<>();
    }
    Map<String, String> target = translations.get(attrName);
    if (target == null)
    {
      target = new HashMap<>();
      translations.put(attrName, target);
    }
    target.put(orig, translation);
  }

  /**
   * Returns primKey if input was done for editing an existing object, null if input was for other purpose
   * (general search, creation of new objects).
   */
  public String getPrimKeyOfEditedObject()
  {
    return primKeyOfEditedObject;
  }

  /**
   * Returns true if user is about to submit a new or changed object. More precisely, the user requested a
   * submit but it failed and currently the user is correcting some data.
   */
  public boolean isForSubmit()
  {
    return forSubmit;
  }

}
