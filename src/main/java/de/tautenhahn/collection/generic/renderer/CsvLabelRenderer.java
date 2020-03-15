package de.tautenhahn.collection.generic.renderer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Primitive implementation of a renderer: just output csv
 *
 * @author ttautenhahn
 */
public class CsvLabelRenderer implements DataRenderer<Label>
{

  @Override
  public void render(List<Label> labels, OutputStream target) throws IOException
  {
    try (OutputStreamWriter writer = new OutputStreamWriter(target, StandardCharsets.UTF_8))
    {
      for ( Label l : labels )
      {
        writer.write("\"" + l.getHeader().replace("\"", "\"\""));
        writer.write("\",\"");
        writer.write(l.getLines()
                      .stream()
                      .map(v -> v.replace("\"", "\"\""))
                      .collect(Collectors.joining("\",\"")));
        writer.write("\"\n");
      }
    }
  }

  @Override
  public String getMediaType()
  {
    return "text/csv; charset=utf-8";
  }
}
