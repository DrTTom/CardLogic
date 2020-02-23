package de.tautenhahn.collection.cards.labels;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.tautenhahn.collection.cards.CardApplicationContext;
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
   * TODO: use a test instead!
   */
  public static void main(String[] args) throws Exception
  {
    CardApplicationContext.register();
    ApplicationContext.getInstance().getPersistence().init("cards");

    LabelCreator instance = new LabelCreator();
    List<Label> labels = ApplicationContext.getInstance()
                                           .getPersistence()
                                           .findAll("deck")
                                           .map(instance::createLabel)
                                           .collect(Collectors.toList());
    try (OutputStream out = new FileOutputStream("checkme.pdf"))
    {
      out.write(labels.toString().getBytes(StandardCharsets.UTF_8));
      // new FreePdfRenderer().render(labels, out);
    }
  }

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
                      .orElse("unbekannt"));
    lines.add("gedruckt irgendwann");
    lines.add(data.getAttributes().get("remark"));
    result.setLines(lines);
    return result;
  }

}

