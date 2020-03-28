package de.tautenhahn.collection.cards.labels;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.renderer.Label;
import lombok.AllArgsConstructor;


/**
 * Creates labels to print out.
 *
 * @author t.tautenhahn
 */
@AllArgsConstructor
public class DeckLabelCreator implements de.tautenhahn.collection.generic.renderer.LabelCreator
{

  /**
   * @param data object with type "deck"
   * @return sensible information to be shown on a label
   */
  @Override
  public Label createLabel(DescribedObject data)
  {
    Label result = new Label();
    result.setHeader(sanitize("Nr. " + data.getPrimKey() + " " + data.getAttributes().get("name")));
    List<String> lines = new ArrayList<>();
    lines.add(Optional.ofNullable(data.getAttributes().get("maker"))
                      .map(key -> ApplicationContext.getInstance().getPersistence().find("maker", key))
                      .map(m -> m.getAttributes().get("name"))
                      .orElse("Hersteller unbekannt"));
    lines.add(getTime(data));
    if (!addOptional("designer", "Entwurf: ", lines, data))
    {
      addOptional("remark", "", lines, data);
    }
    addOptional("refCat", "siehe ", lines, data);
    result.setLines(lines.stream().map(this::sanitize).collect(Collectors.toList()));
    return result;
  }


  // TODO: move sanitize method into EasyDatas DocxAdapter!
  private String sanitize(String input)
  {
    return input.replace("&", "&amp;");
  }

  private boolean addOptional(String name, String prefix, List<String> lines, DescribedObject data)
  {
    return Optional.ofNullable(data.getAttributes().get(name))
                   .map(s -> prefix + s)
                   .filter(lines::add)
                   .isPresent();
  }

  private String getTime(DescribedObject data)
  {
    String from = data.getAttributes().get("printedEarliest");
    String to = data.getAttributes().get("printedLatest");
    if (from == null)
    {
      return to == null ? "(keine Datierung)" : "spätestens " + to;
    }
    return to == null ? "frühestens " + from : to.equals(from) ? "gedruckt " + to : from + " - " + to;
  }
}

