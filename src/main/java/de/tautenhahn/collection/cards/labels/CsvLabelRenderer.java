package de.tautenhahn.collection.cards.labels;

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
public class CsvLabelRenderer implements LabelRenderer
{
    @Override
    public void render(List<Label> labels, OutputStream target) throws Exception
    {
        try (OutputStreamWriter writer = new OutputStreamWriter(target, StandardCharsets.UTF_8))
        {
            for (Label l : labels)
            {
                writer.write(l.getHeader().replace(",", "\\,"));
                writer.write(',');
                writer.write(l.lines.stream().map(line -> line.replace(",", "\\,")).collect(Collectors.joining(",")));
            }
        }
    }

    @Override
    public String getMediaType()
    {
        return "text/comma-separated-values";
    }
}
