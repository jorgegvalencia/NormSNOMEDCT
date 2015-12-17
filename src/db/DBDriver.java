package db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import model.ClinicalTrial;
import model.ClinicalTrial.ClinicalTrialBuilder;
import model.Concept;
import model.ConceptFactory;
import model.EligibilityCriteria;
import nlp.Match;
import nlp.ProcessingUnit;

public class DBDriver implements NormSnomedDAO {
	@SuppressWarnings("unused")
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
	private ApplicationContext context;
	private static final String APPCONTEXT = "Beans.xml";
	// Index for status of concepts
	private static HashMap<String, Integer> index = new HashMap<>(); // sctid,status

	private DBDriver() {
		context = new ClassPathXmlApplicationContext(APPCONTEXT);
		jdbcTemplateObject = (JdbcTemplate) context.getBean("normJDBCTemplate");
	}

	private static class SingletonHelper {
		private static final DBDriver INSTANCE = new DBDriver();
	}

	public static DBDriver getInstance() {
		return SingletonHelper.INSTANCE;
	}

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
		String sql = "INSERT INTO clinical_trial (id,title,studytype) VALUES (?,?,?) ON DUPLICATE KEY UPDATE"
				+ " title=VALUES(title), studytype=VALUES(studytype)";
		jdbcTemplateObject.update(sql, ct.getNctid(), ct.getTitle(), ct.getTopic());
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
		String sql = "INSERT INTO eligibility_criteria (id, clinical_trial_id, inc_exc, utterance) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE"
				+ " inc_exc=VALUES(inc_exc), utterance=VALUES(utterance)";
		jdbcTemplateObject.update(sql, ec.getNumber(), ec.getTrial(), ec.getCriteriaType(), ec.getUtterance());
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
		String sql = "INSERT INTO concept (sctid,cui,name,semantic_type) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE"
				+ " sctid=VALUES(sctid), cui=VALUES(cui), name=VALUES(name), semantic_type=VALUES(semantic_type)";
		jdbcTemplateObject.update(sql, concept.getSctid(), concept.getCui(), concept.getFsn(), concept.getHierarchy());
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

	public static class ConceptMapper implements RowMapper<Concept> {
		@Override
		public Concept mapRow(ResultSet rs, int rowNum) throws SQLException {
			Concept c = ConceptFactory.getConcept(rs.getString("cui"));
			if (c == null) {
				c = new Concept(rs.getString("cui"), rs.getString("sctid"), rs.getString("name"),
						rs.getString("semantic_type"));
			}
			return c;
		}
	}

	public static class EligibilityCriteriaMapper implements RowMapper<EligibilityCriteria> {
		@Override
		public EligibilityCriteria mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new EligibilityCriteria(rs.getString("clinical_trial_id"), rs.getInt("id"),
					rs.getString("utterance"), rs.getInt("inc_exc"));
		}
	}

	public static class ClinicalTrialMapper implements RowMapper<ClinicalTrial> {
		@Override
		public ClinicalTrial mapRow(ResultSet rs, int rowNum) throws SQLException {
			ClinicalTrialBuilder ctb = new ClinicalTrial.ClinicalTrialBuilder(rs.getString("id"));
			ctb.setTitle(rs.getString("title"));
			ctb.setTopic(rs.getString("studytype"));
			return ctb.build();
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
