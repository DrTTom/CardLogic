package de.tautenhahn.collection.generic.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.question.Question;


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

  private final String primKeyOfEditedObject;

  private final boolean forSubmit;

  private List<Question> questions;

  private String message;

  private int numberTotal;

  private int numberMatching;

  private int numberPossible;

  private List<DescribedObject> matches = new ArrayList<>();

  private Map<String, Map<String, String>> translations;

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

  /**
   * Returns the list of questions which is context sensitive.
   */
  public List<Question> getQuestions()
  {
    return questions;
  }

  void setQuestions(List<Question> questions)
  {
    this.questions = questions;
  }

  /**
   * May return an error message if search was not performed.
   */
  public String getMessage()
  {
    return message;
  }

  void setMessage(String message)
  {
    this.message = message;
  }

  /**
   * Returns the total number of objects of current type.
   */
  public int getNumberTotal()
  {
    return numberTotal;
  }

  void setNumberTotal(int numberTotal)
  {
    this.numberTotal = numberTotal;
  }

  /**
   * Returns a list of the best results. The list may be truncated or even empty if there are too many
   * results.
   */
  public List<DescribedObject> getMatches()
  {
    return matches;
  }

  void setMatches(List<DescribedObject> matches)
  {
    this.matches = matches;
  }

  /**
   * Returns the number of objects which have a certain minimal similarity to the search criteria.
   */
  public int getNumberMatching()
  {
    return numberMatching;
  }

  void setNumberMatching(int numberMatching)
  {
    this.numberMatching = numberMatching;
  }

  /**
   * Returns the number of objects which could possibly match the search criteria.
   */
  public int getNumberPossible()
  {
    return numberPossible;
  }

  void setNumberPossible(int numberPossible)
  {
    this.numberPossible = numberPossible;
  }

  /**
   * Returns human-readable translations for attribute values by key attribute name and value.
   */
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
