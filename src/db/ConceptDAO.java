package db;

import javax.sql.DataSource;

import model.Concept;

public interface ConceptDAO {
	public void setDataSource(DataSource dataSource);

	public void create(Concept concept);

	public void delete(String sctid);

	public Concept get(String sctid);
}
