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

  void render(List<Label> labels, OutputStream target) throws Exception;
}

