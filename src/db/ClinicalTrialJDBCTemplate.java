package db;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import model.ClinicalTrial;
import model.ClinicalTrial.ClinicalTrialBuilder;

public class ClinicalTrialJDBCTemplate implements ClinicalTrialDAO {
	@SuppressWarnings("unused")
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;

	@Override
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	@Override
	public void create(ClinicalTrial ct) {
		String sql = "INSERT INTO clinical_trial (id,title,studytype) VALUES (?,?,?) ON DUPLICATE KEY UPDATE"
				+ " title=VALUES(title), studytype=VALUES(studytype)";
		jdbcTemplateObject.update(sql, ct.getNctid(), ct.getTitle(), ct.getTopic());
	}

	@Override
	public void delete(String nctid) {
		System.out.println("Unsupported method");
	}

	@Override
	public ClinicalTrial get(String nctid) {
		try {
			String SQL = "SELECT * FROM clinical_trial WHERE id = ?";
			ClinicalTrial ct = jdbcTemplateObject.queryForObject(SQL, new Object[] { nctid },
					new ClinicalTrialMapper());
			return ct;
		} catch (EmptyResultDataAccessException e) {
			return null;
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

}
