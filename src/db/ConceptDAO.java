package db;

import model.Concept;

public interface ConceptDAO {
	public void create();

	public void delete();

	public Concept get(String sctid);
}
