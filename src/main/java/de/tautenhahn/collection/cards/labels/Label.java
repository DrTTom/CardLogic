package de.tautenhahn.collection.cards.labels;

import java.util.List;

import lombok.Data;


/**
 * Just a data record wrapping label texts.
 * 
 * @author t.tautenhahn
 */
@Data
public class Label
{

  String header;

  List<String> lines;
}
