package de.tautenhahn.collection.generic.renderer;

import de.tautenhahn.collection.generic.data.DescribedObject;

public interface LabelCreator
{
    Label createLabel(DescribedObject data);
}
