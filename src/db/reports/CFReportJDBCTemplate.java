package db.reports;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class CFReportJDBCTemplate {
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	public List<ConceptFrecuencyRecord> listConceptFrecuencies() {
		String sql = "SELECT concept.name AS CONCEPT, concept.cui AS CUI, "
				+ "ec_concepts.concept_sctid AS SCTID, COUNT(ec_concepts.eligibility_criteria_id) AS FRECUENCY, "
				+ "concept.semantic_type AS TYPE FROM ec_concepts, concept WHERE "
				+ "concept.sctid = ec_concepts.concept_sctid GROUP BY concept_sctid ORDER BY FRECUENCY DESC "
				+ "LIMIT 0,100";
		List<ConceptFrecuencyRecord> records = jdbcTemplateObject.query(sql, new ConceptFrecuencyMapper());
		return records;
	}

	public static class ConceptFrecuencyMapper implements RowMapper<ConceptFrecuencyRecord> {

		@Override
		public ConceptFrecuencyRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
			ConceptFrecuencyRecord cfr = new ConceptFrecuencyRecord();
			cfr.setCui(rs.getString("CUI"));
			cfr.setSctid(rs.getString("SCTID"));
			cfr.setFrecuency(rs.getInt("FRECUENCY"));
			cfr.setConcept(rs.getString("CONCEPT"));
			cfr.setType(rs.getString("TYPE"));
			return cfr;
		}

	}
}
