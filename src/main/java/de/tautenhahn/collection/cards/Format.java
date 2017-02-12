package de.tautenhahn.collection.cards;

import java.util.StringTokenizer;

import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.Question;

public class Format extends AttributeInterpreter {

	protected Format() {
		super("format");
	}

	@Override
	public boolean isLegalValue(String value) {
		return value.matches("[1-9][0-9]*x[1-9][0-9]*( \\(.*\\))?");
	}

	@Override
	protected int correllateValue(String thisValue, String otherValue) {
		return new Rectangle(thisValue).similar(new Rectangle(otherValue))? (thisValue.equals(otherValue)? 50: 30) :-1;
	}

	@Override
	public Question getQuestion() {
		Question result = new Question("format", "Breite und Höhe der Karten in mm:", "Messen und zählen");
		result.setHelptext("Angabe '<Breite>x<Höhe>', ggf.  Freitext in Klammern nach Leerzeichen anfügen");
		return result;
	}

	static class Rectangle
	  {

	    int width, height;

	    Rectangle(String value)
	    {
	      StringTokenizer tokens = new StringTokenizer(value, "x (");
	      try
	      {
	        if (tokens.hasMoreTokens())
	        {
	          width = Integer.parseInt(tokens.nextToken());
	        }
	        if (tokens.hasMoreTokens())
	        {
	          height = Integer.parseInt(tokens.nextToken());
	        }
	      }
	      catch (NumberFormatException e)
	      {
	        // cannot handle
	      }
	    }

	    boolean similar(Rectangle other)
	    {
	      return Math.abs(width - other.width) < 2 && Math.abs(height - other.height) < 2;
	    }
	  }
}
