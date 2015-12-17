package model;

import java.util.HashMap;

public class ConceptFactory {
	private static final HashMap<String, Concept> concepts = new HashMap<String, Concept>();

	public static Concept getConcept(String cui) {
		Concept c = concepts.get(cui);
		if (c == null)
			try {
				c = new Concept(cui);
				concepts.put(cui, c);
			} catch (InstantiationException e) {
				c = null;
			}
		return c;
	}
}
