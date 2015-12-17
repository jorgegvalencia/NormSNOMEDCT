package db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import db.reports.CFReportJDBCTemplate;
import db.reports.ConceptFrecuencyReport;
import main.ProcessingUnit;
import model.ClinicalTrial;
import model.Concept;
import model.EligibilityCriteria;

public class DBManager {
	private ApplicationContext context;
	private static final String APPCONTEXT = "Beans.xml";
	// Index for status of concepts
	private static HashMap<String, Integer> index = new HashMap<>(); // sctid,status

	private DBManager() {
		context = new ClassPathXmlApplicationContext(APPCONTEXT);
	}

	public static DBManager getInstance() {
		return SingletonHelper.INSTANCE;
	}

	public ConceptFrecuencyReport getCFReport() {
		CFReportJDBCTemplate cfrReportJDBCTemplate = (CFReportJDBCTemplate) context.getBean("cfrReportJDBCTemplate");
		ConceptFrecuencyReport cfr = new ConceptFrecuencyReport(cfrReportJDBCTemplate.listConceptFrecuencies());
		return cfr;
	}

	public void saveProcessingUnit(ProcessingUnit pu) {
		// TODO Auto-generated method stub

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

	public List<String> getSCTID(String cui) {
		MetathesaurusJDBCTemplate metathesaurus = (MetathesaurusJDBCTemplate) context
				.getBean("metathesaurusJDBCTemplate");
		return metathesaurus.getSCTID(cui);
	}

	public String getFSN(String sctid) {
		SnomedJDBCTemplate snomed = (SnomedJDBCTemplate) context.getBean("snomedJDBCTemplate");
		return snomed.getFSN(sctid);
	}

	public int getStatusFromDB(String sctid) {
		SnomedJDBCTemplate snomed = (SnomedJDBCTemplate) context.getBean("snomedJDBCTemplate");
		return snomed.getStatusFromDB(sctid);
	}

	private static class SingletonHelper {
		private static final DBManager INSTANCE = new DBManager();
	}

	private static class MetathesaurusJDBCTemplate {
		@SuppressWarnings("unused")
		private DataSource dataSource;
		private JdbcTemplate jdbcTemplateObject;

		@SuppressWarnings("unused")
		public void setDataSource(DataSource dataSource) {
			this.dataSource = dataSource;
			jdbcTemplateObject = new JdbcTemplate(dataSource);
		}

		public List<String> getSCTID(String cui) {
			try {
				String sql = "SELECT SCUI FROM metathesaurus.mrconso WHERE CUI= ? AND ISPREF='Y' AND SAB='SNOMEDCT_US' GROUP BY SCUI";
				return jdbcTemplateObject.queryForList(sql, new Object[] { cui }, String.class);
			} catch (EmptyResultDataAccessException e) {
				return new ArrayList<String>();
			}
		}
	}

	private static class SnomedJDBCTemplate {
		@SuppressWarnings("unused")
		private DataSource dataSource;
		private JdbcTemplate jdbcTemplateObject;

		@SuppressWarnings("unused")
		public void setDataSource(DataSource dataSource) {
			this.dataSource = dataSource;
			jdbcTemplateObject = new JdbcTemplate(dataSource);
		}

		public int getStatusFromDB(String sctid) {
			if (index.containsKey(sctid))
				return index.get(sctid);
			try {
				String sql = "SELECT DISTINCT active FROM curr_concept_s WHERE id= ?";
				int status = jdbcTemplateObject.queryForObject(sql, new Object[] { sctid }, Integer.class);
				index.put(sctid, status);
				return status;
			} catch (EmptyResultDataAccessException e) {
				return 0;
			}
		}

		public String getFSN(String sctid) {
			try {
				String sql = "SELECT term FROM curr_concept_s, curr_description_s WHERE curr_concept_s.id = curr_description_s.conceptid "
						+ "AND curr_concept_s.id= ? AND curr_description_s.term LIKE '%(%)'"
						+ "AND curr_description_s.term NOT LIKE '%(qualifier value)'" + "AND curr_concept_s.active='1'"
						+ "AND curr_description_s.active='1';";
				return jdbcTemplateObject.queryForObject(sql, new Object[] { sctid }, String.class);
			} catch (EmptyResultDataAccessException e) {
				return null;
			}
		}
	}
}
