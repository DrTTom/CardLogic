package de.tautenhahn.collection.cards.labels;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.DescribedObject;
import lombok.AllArgsConstructor;


/**
 * Creates labels to print out. TODO: implement generic generation, move this class.
 *
 * @author t.tautenhahn
 */
@AllArgsConstructor
public class LabelCreator
{

  /**
   * @param data some data of supported type
   * @return sensible information to be shown on a label
   */
  public Label createLabel(DescribedObject data)
  {
    Label result = new Label();
    result.setHeader("Nr. " + data.getPrimKey() + " " + data.getAttributes().get("name"));
    List<String> lines = new ArrayList<>();
    lines.add(Optional.ofNullable(data.getAttributes().get("maker"))
                      .map(key -> ApplicationContext.getInstance().getPersistence().find("maker", key))
                      .map(m -> m.getAttributes().get("name"))
                      .orElse("Hersteller unbekannt"));
    lines.add(getTime(data));
    if (!addOptional("designer", lines, data))
    {
      addOptional("remark", lines, data);
    }
    addOptional("refCat", lines, data);
    result.setLines(lines);
    return result;
  }

  private boolean addOptional(String name, List<String> lines, DescribedObject data)
  {
    return Optional.ofNullable(data.getAttributes().get(name)).filter(lines::add).isPresent();
  }

  private String getTime(DescribedObject data)
  {
    String from = data.getAttributes().get("printedEarliest");
    String to = data.getAttributes().get("printedLatest");
    return from == null ? (to == null ? "(keine Datierung)" : "spätestens " + to)
      : (to == null ? "frühestens " + from : (to.equals(from) ? "gedruckt " + to : from + " - " + to));
  }
}

