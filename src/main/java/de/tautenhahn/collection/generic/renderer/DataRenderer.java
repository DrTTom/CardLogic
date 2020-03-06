package de.tautenhahn.collection.generic.renderer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


/**
 * Translates objects into some printable form.
 * 
 * @author t.tautenhahn
 * @param <T> type of supported object
 */
public interface DataRenderer<T>
{
  /**
   * Writes the given labels into the target stream.
   * @param data
   * @param target
   * @throws Exception
   */
  void render(List<T> data, OutputStream target) throws IOException;

  /**
   *
   * @return media type used in output
   */
  String getMediaType();
}

