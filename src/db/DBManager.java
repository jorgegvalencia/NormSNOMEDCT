package db;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import db.reports.CFReportJDBCTemplate;
import db.reports.ConceptFrecuencyReport;
import model.ClinicalTrial;
import model.Concept;
import model.EligibilityCriteria;

public class DBManager {
	public ApplicationContext context;
	private static final String APPCONTEXT = "Beans.xml";

	public DBManager() throws InstantiationException {
		PrintStream stderr = System.err;
		try {
			System.setErr(new PrintStream(new FileOutputStream("norm.log")));
			context = new ClassPathXmlApplicationContext(APPCONTEXT);
			System.setErr(stderr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (context == null)
				throw new InstantiationException("ERROR in starting DBManager: Can't write in file log \"norm.log\"");
		}
	}

	public ConceptFrecuencyReport getCFReport() {
		CFReportJDBCTemplate cfrReportJDBCTemplate = (CFReportJDBCTemplate) context.getBean("cfrReportJDBCTemplate");
		ConceptFrecuencyReport cfr = new ConceptFrecuencyReport(cfrReportJDBCTemplate.listConceptFrecuencies());
		return cfr;
	}

	public ClinicalTrial getClinicalTrial(String nctid) {
		ClinicalTrialJDBCTemplate ctJDBCTemplate = (ClinicalTrialJDBCTemplate) context.getBean("ctJDBCTemplate");
		return ctJDBCTemplate.get(nctid);
	}

	public void saveClinicalTrial(ClinicalTrial ct) {
		ClinicalTrialJDBCTemplate ctJDBCTemplate = (ClinicalTrialJDBCTemplate) context.getBean("ctJDBCTemplate");
		ctJDBCTemplate.create(ct);
	}

	public Concept getConcept(String sctid) {
		ConceptJDBCTemplate conceptJDBCTemplate = (ConceptJDBCTemplate) context.getBean("conceptJDBCTemplate");
		return conceptJDBCTemplate.get(sctid);
	}

	public void saveConcept(Concept concept) {
		ConceptJDBCTemplate conceptJDBCTemplate = (ConceptJDBCTemplate) context.getBean("conceptJDBCTemplate");
		conceptJDBCTemplate.create(concept);
	}

	public EligibilityCriteria getEligibilityCriteria(String trial, int number) {
		EligibilityCriteriaJDBCTemplate ecJDBCTemplate = (EligibilityCriteriaJDBCTemplate) context
				.getBean("ecJDBCTemplate");
		return ecJDBCTemplate.get(trial, number);
	}

	public void saveEligibilityCriteria(EligibilityCriteria ec) {
		EligibilityCriteriaJDBCTemplate ecJDBCTemplate = (EligibilityCriteriaJDBCTemplate) context
				.getBean("ecJDBCTemplate");
		ecJDBCTemplate.create(ec);
	}
}
