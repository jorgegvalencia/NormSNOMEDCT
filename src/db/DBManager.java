package db;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import db.reports.CFReportJDBCTemplate;
import db.reports.ConceptFrecuencyReport;
import db.reports.MatchReport;
import db.reports.TConceptsJDBCTemplate;
import db.reports.TrialConceptsReport;
import model.ClinicalTrial;
import model.Concept;
import model.EligibilityCriteria;
import nlp.Match;
import nlp.ProcessingUnit;

public class DBManager {
	public ApplicationContext context;
	private static final String APPCONTEXT = "Beans.xml";
	// Index for status of concepts
	private static HashMap<String, Integer> index = new HashMap<>(); // sctid,status

	private DBManager() {
		PrintStream stderr = System.err;
		try {
			System.setErr(new PrintStream(new FileOutputStream("spring-norm.log")));
			context = new ClassPathXmlApplicationContext(APPCONTEXT);
			System.setErr(stderr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (context == null) {
				System.exit(-1);
			}
		}
	}

	private static class SingletonHelper {
		private static final DBManager INSTANCE = new DBManager();
	}

	public static DBManager getInstance() {
		return SingletonHelper.INSTANCE;
	}

	public ConceptFrecuencyReport getCFReport() {
		CFReportJDBCTemplate cfrReportJDBCTemplate = (CFReportJDBCTemplate) context.getBean("cfrReportJDBCTemplate");
		ConceptFrecuencyReport cfr = new ConceptFrecuencyReport(cfrReportJDBCTemplate.listConceptFrecuencies());
		return cfr;
	}

	public MatchReport getMatchReport(String sctid) {
		CFReportJDBCTemplate cfrReportJDBCTemplate = (CFReportJDBCTemplate) context.getBean("cfrReportJDBCTemplate");
		MatchReport mr = new MatchReport(cfrReportJDBCTemplate.listMatchRecord(sctid));
		return mr;
	}

	public TrialConceptsReport getTCReport(String nctid) {
		TConceptsJDBCTemplate tcrReportJDBCTemplate = (TConceptsJDBCTemplate) context.getBean("tcrReportJDBCTemplate");
		TrialConceptsReport tcr = new TrialConceptsReport(tcrReportJDBCTemplate.listTrialConcepts(nctid));
		return tcr;
	}

	public void saveProcessingUnit(ProcessingUnit pu) {
		NormJDBCTemplate norm = (NormJDBCTemplate) context.getBean("normJDBCTemplate");
		if (pu.isProcessed()) {
			saveClinicalTrial(pu.getClinicalTrial());
			for (EligibilityCriteria ec : pu.getClinicalTrial().getCriteria().getEclist()) {
				saveEligibilityCriteria(ec);
				for (Match m : ec.getMatches()) {
					saveConcept(m.getConcept());
					norm.saveMatch(m, ec.getTrial(), ec.getNumber());
				}
			}
		}
	}

	public ClinicalTrial getClinicalTrial(String nctid) {
		NormJDBCTemplate norm = (NormJDBCTemplate) context.getBean("normJDBCTemplate");
		return norm.getClinicalTrial(nctid);
	}

	public void saveClinicalTrial(ClinicalTrial ct) {
		NormJDBCTemplate norm = (NormJDBCTemplate) context.getBean("normJDBCTemplate");
		norm.saveClinicalTrial(ct);
	}

	public Concept getConcept(String sctid) {
		NormJDBCTemplate norm = (NormJDBCTemplate) context.getBean("normJDBCTemplate");
		return norm.getConcept(sctid);
	}

	public void saveConcept(Concept concept) {
		NormJDBCTemplate norm = (NormJDBCTemplate) context.getBean("normJDBCTemplate");
		norm.saveConcept(concept);
	}

	public EligibilityCriteria getEligibilityCriteria(String trial, int number) {
		NormJDBCTemplate norm = (NormJDBCTemplate) context.getBean("normJDBCTemplate");
		return norm.getEligibilityCriteria(trial, number);
	}

	public void saveEligibilityCriteria(EligibilityCriteria ec) {
		NormJDBCTemplate norm = (NormJDBCTemplate) context.getBean("normJDBCTemplate");
		norm.saveEligibilityCriteria(ec);
	}

	public List<String> getSCTID(String cui) {
		MetathesaurusJDBCTemplate metathesaurus = (MetathesaurusJDBCTemplate) context
				.getBean("metathesaurusJDBCTemplate");
		return metathesaurus.getSCTID(cui);
	}

	public List<String> getFSN(String sctid) {
		SnomedJDBCTemplate snomed = (SnomedJDBCTemplate) context.getBean("snomedJDBCTemplate");
		return snomed.getFSN(sctid);
	}

	public int getStatusFromDB(String sctid) {
		SnomedJDBCTemplate snomed = (SnomedJDBCTemplate) context.getBean("snomedJDBCTemplate");
		return snomed.getStatusFromDB(sctid);
	}

	private static class NormJDBCTemplate implements NormSnomedDAO {
		@SuppressWarnings("unused")
		private DataSource dataSource;
		private JdbcTemplate jdbcTemplateObject;

		@Override
		public void setDataSource(DataSource dataSource) {
			this.dataSource = dataSource;
			jdbcTemplateObject = new JdbcTemplate(dataSource);
		}

		@Override
		public void saveProcessingUnit(ProcessingUnit pu) {
			if (pu.isProcessed()) {
				saveClinicalTrial(pu.getClinicalTrial());
				for (EligibilityCriteria ec : pu.getClinicalTrial().getCriteria().getEclist()) {
					saveEligibilityCriteria(ec);
					for (Match m : ec.getMatches()) {
						saveConcept(m.getConcept());
						saveMatch(m, ec.getTrial(), ec.getNumber());
					}
				}
			}
		}

		@Override
		public ClinicalTrial getClinicalTrial(String nctid) {
			try {
				String SQL = "SELECT * FROM clinical_trial WHERE id = ?";
				ClinicalTrial ct = jdbcTemplateObject.queryForObject(SQL, new Object[] { nctid },
						new ClinicalTrialMapper());
				return ct;
			} catch (EmptyResultDataAccessException e) {
				return null;
			}
		}

		@Override
		public void saveClinicalTrial(ClinicalTrial ct) {
			String sql = "INSERT INTO clinical_trial (nctid,title,topic) VALUES (?,?,?) ON DUPLICATE KEY UPDATE"
					+ " nctid=VALUES(nctid), title=VALUES(title), topic=VALUES(topic)";
			jdbcTemplateObject.update(sql, ct.getNctid(), ct.getTitle(), ct.getTopic());
			for (Entry<String, String> entry : ct.getAttributes().entrySet()) {
				saveAttribute(entry, ct.getNctid());
			}
		}

		@Override
		public EligibilityCriteria getEligibilityCriteria(String trial, int number) {
			try {
				String SQL = "SELECT * FROM eligibility_criteria WHERE clinical_trial = ? AND id = ?";
				EligibilityCriteria ec = jdbcTemplateObject.queryForObject(SQL, new Object[] { trial, number },
						new EligibilityCriteriaMapper());
				return ec;
			} catch (EmptyResultDataAccessException e) {
				return null;
			}
		}

		@Override
		public void saveEligibilityCriteria(EligibilityCriteria ec) {
			String sql = "INSERT INTO eligibility_criteria (number, trial, utterance, inc_exc) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE"
					+ " number=VALUES(number), trial=VALUES(trial), utterance=VALUES(utterance), inc_exc=VALUES(inc_exc)";
			jdbcTemplateObject.update(sql, ec.getNumber(), ec.getTrial(), ec.getUtterance(), ec.getCriteriaType());
		}

		@Override
		public Concept getConcept(String sctid) {
			try {
				String SQL = "SELECT * FROM concept WHERE sctid = ?";
				Concept c = jdbcTemplateObject.queryForObject(SQL, new Object[] { sctid }, new ConceptMapper());
				return c;
			} catch (EmptyResultDataAccessException e) {
				return null;
			}
		}

		@Override
		public void saveConcept(Concept concept) {
			String sql = "INSERT INTO concept (sctid,cui,fsn,hierarchy) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE"
					+ " sctid=VALUES(sctid), cui=VALUES(cui), fsn=VALUES(fsn), hierarchy=VALUES(hierarchy)";
			jdbcTemplateObject.update(sql, concept.getSctid(), concept.getCui(), concept.getFsn(),
					concept.getHierarchy());
		}

		private void saveMatch(Match m, String trial, int number) {
			String sql = "INSERT INTO cmatch (number, trial, sctid, phrase, synonym, prefered_name, matched_words) VALUES(?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE"
					+ " number=VALUES(number), trial=VALUES(trial), sctid=VALUES(sctid), phrase=VALUES(phrase),"
					+ " synonym=VALUES(synonym), prefered_name=VALUES(prefered_name), matched_words=VALUES(matched_words)";
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			for (int i = 0; i < m.getMatchedWords().size(); i++) {
				String mw = m.getMatchedWords().get(i);
				sb.append(" " + mw + " ");
				if (i < m.getMatchedWords().size() - 1) {
					sb.append(",");
				}
			}
			sb.append("]");
			jdbcTemplateObject.update(sql, number, trial, m.getConcept().getSctid(), m.getPhrase(), m.getTerm(),
					m.getPrefered(), sb.toString());
		}

		private void saveAttribute(Entry<String, String> pair, String trial) {
			String sql = "INSERT INTO attribute (trial, attribute, value) VALUES(?,?,?) ON DUPLICATE KEY UPDATE"
					+ " trial=VALUES(trial), attribute=VALUES(attribute), value=VALUES(value)";
			jdbcTemplateObject.update(sql, trial, pair.getKey(), pair.getValue());
		}
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

		public List<String> getFSN(String sctid) {
			try {
				String sql = "SELECT term FROM curr_concept_s, curr_description_s WHERE curr_concept_s.id = curr_description_s.conceptid "
						+ "AND curr_concept_s.id= ? AND curr_description_s.term LIKE '%(%)'"
						+ "AND curr_description_s.term NOT LIKE '%(qualifier value)'" + "AND curr_concept_s.active='1'"
						+ "AND curr_description_s.active='1';";
				return jdbcTemplateObject.queryForList(sql, new Object[] { sctid }, String.class);
			} catch (EmptyResultDataAccessException e) {
				return null;
			}
		}
	}
}