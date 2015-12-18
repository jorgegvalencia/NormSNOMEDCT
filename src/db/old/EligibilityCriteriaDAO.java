package db.old;

import javax.sql.DataSource;

import model.EligibilityCriteria;

public interface EligibilityCriteriaDAO {
	public void setDataSource(DataSource dataSource);

	public void create(EligibilityCriteria ec);

	public void delete(String trial, int number);

	public EligibilityCriteria get(String trial, int number);
}
