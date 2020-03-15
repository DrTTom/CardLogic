package de.tautenhahn.collection.generic.renderer;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.Test;


/**
 * Checks CSV creation.
 * 
 * @author t.tautenhahn
 */
public class CsvRendererTest
{

  @Test
  void renderCsv() throws IOException
  {
    DataRenderer<Label> systemUnderTest = new CsvLabelRenderer();
    Label label = new Label();
    label.header = "Instance \"schön\"";
    label.lines = List.of("first line", "second line, different");
    try (ByteArrayOutputStream target = new ByteArrayOutputStream())
    {
      systemUnderTest.render(List.of(label, label), target);
      String csvLine = "\"Instance \"\"schön\"\"\",\"first line\",\"second line, different\"\n";
      assertThat(new String(target.toByteArray(), StandardCharsets.UTF_8)).isEqualTo(csvLine + csvLine);
      assertThat(systemUnderTest.getMediaType()).contains("utf-8");
    }
  }
}
