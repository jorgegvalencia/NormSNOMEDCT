package db;

import javax.sql.DataSource;

import model.ClinicalTrial;

public interface ClinicalTrialDAO {
	public void setDataSource(DataSource dataSource);

	public void create(ClinicalTrial ct);

	public void delete(String nctid);

	public ClinicalTrial get(String nctid);
}
