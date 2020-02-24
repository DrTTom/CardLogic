package de.tautenhahn.collection.cards.labels;

import java.io.OutputStream;
import java.util.List;


/**
 * Translates labels into some printable form.
 * 
 * @author t.tautenhahn
 */
public interface LabelRenderer
{
  /**
   * Writes the given labels into the target stream.
   * @param labels
   * @param target
   * @throws Exception
   */
  void render(List<Label> labels, OutputStream target) throws Exception;

  /**
   *
   * @return media type used in output
   */
  String getMediaType();
}

