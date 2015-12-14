package db;

import model.ClinicalTrial;

public interface ClinicalTrialDAO {
	public void create(String nctid);

	public void delete(String nctid);

	public ClinicalTrial get(String nctid);
}
