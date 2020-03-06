package de.tautenhahn.collection.generic.renderer;

import java.io.InputStream;

/**
 * Creates labels in DOCX format.
 *
 * @author ttautenhahn
 */
public class DocxLabelRenderer extends DocxRenderer<Label>
{
  @Override
  protected InputStream getTemplate()
  {
    return DocxLabelRenderer.class.getResourceAsStream("labels_template.docx");
  }
}
