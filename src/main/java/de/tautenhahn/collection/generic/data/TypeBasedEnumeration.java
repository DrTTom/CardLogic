package de.tautenhahn.collection.generic.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.question.ImageChoiceQuestion;
import de.tautenhahn.collection.generic.data.question.ObjectChoiceQuestion;
import de.tautenhahn.collection.generic.persistence.Persistence;
import de.tautenhahn.collection.generic.persistence.PersistenceChangeListener;


/**
 * Enumeration with allowed values defined by existing objects of another type.
 *
 * @author TT
 */
public abstract class TypeBasedEnumeration extends Enumeration implements PersistenceChangeListener
{

  /**
   * Persistence holding the auxiliary objects the foreign key points to.
   */
  protected final Persistence persistence;

  /**
   * Caching auxiliary objects of base type: key is name of object, value is its primary key.
   */
  private final Map<String, String> nameByPrimKey = new HashMap<>();

  private final Map<String, String> imageByPrimKey = new HashMap<>();

  private final boolean supportAbsent;

  static final String WITHOUT = "ohne";

  private String width;

  private String height;

  /**
   * Specify at which size images are to be displayed if all objects have images.
   *
   * @param width
   * @param height
   */
  public void enableImage(String width, String height)
  {
    this.width = width;
    this.height = height;
  }

  /**
   * Creates new instance.
   *
   * @param name attribute name, must be equal to the type
   * @param matchValue relevance of a match
   * @param flags specify further properties
   */
  public TypeBasedEnumeration(String name, int matchValue, Flag... flags)
  {
    super(name, matchValue, flags);
    persistence = ApplicationContext.getInstance().getPersistence();
    persistence.addListener(this);
    supportAbsent = List.of(flags).contains(Flag.SUPPORTS_NONE);
    setupMaps();
  }

  @Override
  public List<String> getAllowedValues(DescribedObject context)
  {
    ArrayList<String> result = new ArrayList<>(nameByPrimKey.keySet());
    Optional.ofNullable(context)
            .map(o -> o.getAttributes().get(getName()))
            .filter(name -> !result.contains(name))
            .ifPresent(result::add);
    return result;
  }

  @Override
  public String toDisplayValue(String primKey)
  {
    return Optional.ofNullable(primKey).map(k -> nameByPrimKey.getOrDefault(k, k)).orElse(NULL_PLACEHOLDER);
  }

  @Override
  public String check(String value, DescribedObject context)
  {
    return nameByPrimKey.containsKey(value) ? null : "msg.error.invalidOption";
  }

  @Override
  public ObjectChoiceQuestion getQuestion(DescribedObject object)
  {
    Map<String, String> options = new LinkedHashMap<>();
    getOptions(object).entrySet()
                      .stream()
                      .filter(e -> !NOT_SPECIFIED.equals(e.getKey()) && !WITHOUT.equals(e.getKey()))
                      .sorted(Map.Entry.comparingByValue())
                      .forEach(e -> options.put(e.getKey(), e.getValue()));
    options.put(NOT_SPECIFIED, NULL_PLACEHOLDER);
    if (supportAbsent)
    {
      options.put(WITHOUT, toDisplayValue(WITHOUT));
    }

    if (width == null)
    {
      ObjectChoiceQuestion result = createQuestion(object,
                                                   (text, group) -> new ObjectChoiceQuestion(getName(), text,
                                                                                             group,
                                                                                             getName()));
      result.setOptions(options);
      return result;
    }
    ImageChoiceQuestion result = createQuestion(object,
                                                (text, group) -> new ImageChoiceQuestion(getName(), text,
                                                                                         group, getName()));
    result.setOptions(options);
    result.setFormat(width, height);
    result.setUrls(options.keySet()
                          .stream()
                          .collect(Collectors.toMap(Function.identity(),
                                                    k -> imageByPrimKey.getOrDefault(k, NOT_SPECIFIED))));
    return result;
  }

  private void setupMaps()
  {
    nameByPrimKey.clear();
    imageByPrimKey.clear();
    for ( String key : persistence.getKeyValues(getName()) )
    {
      DescribedObject obj = persistence.find(getName(), key);
      nameByPrimKey.put(obj.getPrimKey(),
                        Optional.ofNullable(obj.getAttributes().get(DescribedObject.NAME_KEY))
                                .orElse(obj.getPrimKey()));

      Optional.ofNullable(obj.getAttributes().get(DescribedObject.IMAGE_KEY))
              .ifPresent(img -> imageByPrimKey.put(key, img));
    }
    if (supportAbsent)
    {
      nameByPrimKey.put(WITHOUT, ApplicationContext.getInstance().getText(keyPrefix + getName() + ".none"));
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
}
