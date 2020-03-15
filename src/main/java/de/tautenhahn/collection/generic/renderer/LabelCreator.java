package de.tautenhahn.collection.generic.renderer;

import de.tautenhahn.collection.generic.data.DescribedObject;


/**
 * Creates label data objects.
 * 
 * @author t.tautenhahn
 */
public interface LabelCreator
{

  /**
   * Chooses relevant information to print on a label.
   * 
   * @param data object to represent
   * @return short description suitable to print on a label.
   */
  Label createLabel(DescribedObject data);
}
