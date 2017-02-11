package de.tautenhahn.collection.cards;

import java.awt.Image;
import java.io.IOException;

import javax.swing.Icon;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.DescribedObjectInterpreter;
import de.tautenhahn.collection.generic.persistence.Persistence;
import de.tautenhahn.collection.generic.persistence.WorkspacePersistence;

public class CardApplicationContext extends ApplicationContext{

	private Persistence persistence;
	
	@Override
	public DescribedObjectInterpreter getInterpreter(String type) {
		switch(type)
		{
		case "deck":
			return new Deck();
			default: throw new IllegalArgumentException("unsupported type "+type);
		}		
	}

	@Override
	protected String getSpecificText(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Icon getApplicationIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image getNoImage() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void init() {
		new CardApplicationContext();
		
	}
	
	private CardApplicationContext()
	{
		persistence= new WorkspacePersistence();
		try {
			persistence.init("cards");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Persistence getPersistence() {
		
		return persistence;
	}
	

}
