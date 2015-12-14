package db.reports;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

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
}
