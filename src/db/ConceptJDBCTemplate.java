package db;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import model.Concept;
import model.ConceptFactory;

public class ConceptJDBCTemplate implements ConceptDAO {
	@SuppressWarnings("unused")
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;

	@Override
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	@Override
	public void create(Concept concept) {
		String sql = "INSERT INTO concept (sctid,cui,name,semantic_type) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE"
				+ " sctid=VALUES(sctid), cui=VALUES(cui), name=VALUES(name), semantic_type=VALUES(semantic_type)";
		jdbcTemplateObject.update(sql, concept.getSctid(), concept.getCui(), concept.getFsn(), concept.getHierarchy());
	}

	@Override
	public void delete(String sctid) {
		System.out.println("Unsupported method");
	}

	@Override
	public Concept get(String sctid) {
		try {
			String SQL = "SELECT * FROM concept WHERE sctid = ?";
			Concept c = jdbcTemplateObject.queryForObject(SQL, new Object[] { sctid }, new ConceptMapper());
			return c;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public static class ConceptMapper implements RowMapper<Concept> {
		@Override
		public Concept mapRow(ResultSet rs, int rowNum) throws SQLException {
			Concept c = ConceptFactory.getConcept(rs.getString("cui"));
			if (c == null)
				c = new Concept(rs.getString("cui"), rs.getString("sctid"), rs.getString("name"),
						rs.getString("semantic_type"));
			return c;
		}
	}

}
