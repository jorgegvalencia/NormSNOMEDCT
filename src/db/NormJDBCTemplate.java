package db;

import java.util.Map.Entry;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import model.ClinicalTrial;
import model.Concept;
import model.EligibilityCriteria;
import nlp.Match;
import nlp.ProcessingUnit;

public class NormJDBCTemplate implements NormSnomedDAO {
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;

	@Override
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void saveProcessingUnit(ProcessingUnit pu) {
		jdbcTemplateObject = new JdbcTemplate(dataSource);
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
		jdbcTemplateObject = new JdbcTemplate(dataSource);
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
		jdbcTemplateObject = new JdbcTemplate(dataSource);
		String sql = "INSERT INTO clinical_trial (id,title,studytype) VALUES (?,?,?) ON DUPLICATE KEY UPDATE"
				+ " title=VALUES(title), studytype=VALUES(studytype)";
		jdbcTemplateObject.update(sql, ct.getNctid(), ct.getTitle(), ct.getTopic());
		for (Entry<String, String> entry : ct.getAttributes().entrySet())
			saveAttribute(entry, ct.getNctid());
	}

	@Override
	public EligibilityCriteria getEligibilityCriteria(String trial, int number) {
		jdbcTemplateObject = new JdbcTemplate(dataSource);
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
		jdbcTemplateObject = new JdbcTemplate(dataSource);
		String sql = "INSERT INTO eligibility_criteria (id, clinical_trial_id, inc_exc, utterance) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE"
				+ " inc_exc=VALUES(inc_exc), utterance=VALUES(utterance)";
		jdbcTemplateObject.update(sql, ec.getNumber(), ec.getTrial(), ec.getCriteriaType(), ec.getUtterance());
	}

	@Override
	public Concept getConcept(String sctid) {
		jdbcTemplateObject = new JdbcTemplate(dataSource);
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
		jdbcTemplateObject = new JdbcTemplate(dataSource);
		String sql = "INSERT INTO concept (sctid,cui,name,semantic_type) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE"
				+ " sctid=VALUES(sctid), cui=VALUES(cui), name=VALUES(name), semantic_type=VALUES(semantic_type)";
		jdbcTemplateObject.update(sql, concept.getSctid(), concept.getCui(), concept.getFsn(), concept.getHierarchy());
	}

	private void saveMatch(Match m, String trial, int number) {
		jdbcTemplateObject = new JdbcTemplate(dataSource);
		String sql = "INSERT INTO match (number,trial,sctid,phrase,synonym,prefered_name,matched_words) VALUES (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE"
				+ " number=VALUES(number), trial=VALUES(trial), sctid=VALUES(sctid), phrase=VALUES(phrase),"
				+ " synonym=VALUES(synonym), prefered_name=VALUES(prefered_name), matched_words=VALUES(matched_words)";
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < m.getMatchedWords().size(); i++) {
			String mw = m.getMatchedWords().get(i);
			sb.append(" " + mw + " ");
			if (i < m.getMatchedWords().size() - 1)
				sb.append(",");
		}
		sb.append("]");
		jdbcTemplateObject.update(sql, number, trial, m.getConcept().getSctid(), m.getPhrase(), m.getTerm(),
				m.getPrefered(), sb.toString());
	}

	private void saveAttribute(Entry<String, String> pair, String trial) {
		jdbcTemplateObject = new JdbcTemplate(dataSource);
		String sql = "INSERT INTO attribute (trial, attribute, value) VALUES(?,?,?) ON DUPLICATE KEY UPDATE"
				+ " trial=VALUES(trial), attribute=VALUES(attribute), value=VALUES(value)";
		jdbcTemplateObject.update(sql, trial, pair.getKey(), pair.getValue());
	}
}
