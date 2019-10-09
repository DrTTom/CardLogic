package de.tautenhahn.collection.generic.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.question.ImageChoiceQuestion;
import de.tautenhahn.collection.generic.data.question.ObjectChoiceQuestion;
import de.tautenhahn.collection.generic.data.question.Question;
import de.tautenhahn.collection.generic.persistence.Persistence;
import de.tautenhahn.collection.generic.persistence.PersistenceChangeListener;


/**
 * Enumeration with allowed values defined by existing objects of another type.
 *
 * @author TT
 */
public abstract class TypeBasedEnumeration extends Enumeration
  implements PersistenceChangeListener, AttributeInterpreter.Translating
{

  /**
   * Persistence holding the auxiliary objects the foreign key points to.
   */
  protected Persistence persistence;

  /**
   * For all auxiliary objects of current type: key is name of object, value is its primary key.
   */
  protected Map<String, String> primKeyByName = new TreeMap<>();

  /**
   * Inverse map to {@link #primKeyByName}
   */
  protected Map<String, String> nameByPrimKey = new HashMap<>();

  /**
   * URLs of some images to display (if any)
   */
  protected Map<String, String> imageByName = new HashMap<>();

  /**
   * Creates new instance.
   *
   * @param name attribute name, must be equal to the type
   * @param matchValue
   * @param flags
   */
  public TypeBasedEnumeration(String name, int matchValue, Flag... flags)
  {
    super(name, matchValue, flags);
    persistence = ApplicationContext.getInstance().getPersistence();
    persistence.addListener(this);
    setupMaps();
  }

  @Override
  public List<String> getAllowedValues(DescribedObject context)
  {
    ArrayList<String> result = new ArrayList<>();
    result.add("");
    result.addAll(primKeyByName.keySet());
    Optional.ofNullable(context)
            .map(o -> o.getAttributes().get(getName()))
            .map(key -> nameByPrimKey.get(key))
            .filter(name -> !result.contains(name))
            .ifPresent(name -> result.add(name));
    return result;
  }

  @Override
  public String toDisplayValue(String primKey)
  {
    return Optional.ofNullable(primKey).map(k -> nameByPrimKey.getOrDefault(k, k)).orElse(NULL_PLACEHOLDER);
  }

  @Override
  public String toInternalValue(String name)
  {
    if (NULL_PLACEHOLDER.equals(name))
    {
      return null;
    }
    return Optional.ofNullable(name).map(k -> primKeyByName.getOrDefault(k, k)).orElse(null);
  }

  @Override
  public String check(String value, DescribedObject context)
  {
    return nameByPrimKey.containsKey(value) ? null : "msg.error.invalidOption";
  }

  @Override
  public Question getQuestion(DescribedObject object)
  {
    ObjectChoiceQuestion result = createQuestion(object,
                                                 (text, group) -> new ObjectChoiceQuestion(getName(), text,
                                                                                           group, getName()));
    List<String> options = new ArrayList<>();
    getAllowedValues(object).forEach(v -> options.add(toDisplayValue(v)));
    options.sort((a, b) -> a.compareTo(b));
    options.add(NULL_PLACEHOLDER);
    result.setOptions(options);
    return result;
  }

  private void setupMaps()
  {
    primKeyByName.clear();
    nameByPrimKey.clear();
    imageByName.clear();
    for ( String key : persistence.getKeyValues(getName()) )
    {
      DescribedObject obj = persistence.find(getName(), key);
      String name = Optional.ofNullable(obj.getAttributes().get(DescribedObject.NAME_KEY))
                            .orElse(obj.getPrimKey());
      primKeyByName.put(name, obj.getPrimKey()); // TODO: handle alternative names here as well
      nameByPrimKey.put(obj.getPrimKey(), name);
      Optional.ofNullable(obj.getAttributes().get(DescribedObject.IMAGE_KEY))
              .ifPresent(img -> imageByName.put(name, img));
    }
  }

  @Override
  public void onChange(String type)
  {
    if (getName().equals(type) || "*".equals(type))
    {
      setupMaps();
    }
  }

  /**
   * Returns an image choice question. To be called from {@link #getQuestion(DescribedObject)} in case the
   * type this attribute is based upon mainly constists of an image.
   *
   * @param object
   * @param width
   * @param height
   */
  protected ImageChoiceQuestion getImageQuestion(DescribedObject object, String width, String height)
  {
    ImageChoiceQuestion result = createQuestion(object,
                                                (text, group) -> new ImageChoiceQuestion(getName(), text,
                                                                                         group));
    Map<String, String> urlByValue = new LinkedHashMap<>();
    urlByValue.put(NULL_PLACEHOLDER, "");
    List<String> options = new ArrayList<>();
    options.add(NULL_PLACEHOLDER);
    getAllowedValues(object).stream().map(v -> toDisplayValue(v)).sorted().forEach(dv -> {
      urlByValue.put(dv, imageByName.get(dv));
      options.add(dv);
    });
    result.setOptions(options);
    result.setUrls(urlByValue);
    result.setFormat(width, height);
    return result;
  }
}
