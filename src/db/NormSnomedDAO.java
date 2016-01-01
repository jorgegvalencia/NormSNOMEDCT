package db;

import javax.sql.DataSource;

import model.ClinicalTrial;
import model.Concept;
import model.EligibilityCriteria;
import nlp.ProcessingUnit;

public interface NormSnomedDAO {

	public void setDataSource(DataSource dataSource);

	public void saveProcessingUnit(ProcessingUnit pu);

	// ClinicalTrial

	public ClinicalTrial getClinicalTrial(String nctid);

	public void saveClinicalTrial(ClinicalTrial ct);

	// Concept

	public Concept getConcept(String sctid);

	public void saveConcept(Concept concept);

	// EligibilityCriteria

	public EligibilityCriteria getEligibilityCriteria(String trial, int number);

	public void saveEligibilityCriteria(EligibilityCriteria ec);

}
