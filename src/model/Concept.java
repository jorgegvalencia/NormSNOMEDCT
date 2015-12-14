package model;

import java.util.List;

import db.ConceptDAO;

public class Concept implements ConceptDAO {
	private String cui;
	private String sctid;
	private String name;
	private String preferedName;
	private String phrase;
	private List<String> semtypes;
	private String hierarchy;
	private String normalForm;

	@Override
	public void create() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	@Override
	public Concept get(String sctid) {
		// TODO Auto-generated method stub
		return null;
	}

	private Concept(ConceptBuilder builder) {

	}

	public static class ConceptBuilder {
		private String cui;
		private String sctid;
		private String name;
		private String preferedName;
		private String phrase;
		private List<String> semtypes;
		private String hierarchy;
		private String normalForm;
	}
}
