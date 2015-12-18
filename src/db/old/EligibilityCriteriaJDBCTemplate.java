package db.old;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import model.EligibilityCriteria;

public class EligibilityCriteriaJDBCTemplate implements EligibilityCriteriaDAO {
	@SuppressWarnings("unused")
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;

	@Override
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	@Override
	public void create(EligibilityCriteria ec) {
		String sql = "INSERT INTO eligibility_criteria (id, clinical_trial_id, inc_exc, utterance) VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE"
				+ " inc_exc=VALUES(inc_exc), utterance=VALUES(utterance)";
		jdbcTemplateObject.update(sql, ec.getNumber(), ec.getTrial(), ec.getCriteriaType(), ec.getUtterance());
	}

	@Override
	public void delete(String trial, int number) {
		System.out.println("Unsupported method");
	}

	@Override
	public EligibilityCriteria get(String trial, int number) {
		try {
			String SQL = "SELECT * FROM eligibility_criteria WHERE clinical_trial = ? AND id = ?";
			EligibilityCriteria ec = jdbcTemplateObject.queryForObject(SQL, new Object[] { trial, number },
					new EligibilityCriteriaMapper());
			return ec;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public static class EligibilityCriteriaMapper implements RowMapper<EligibilityCriteria> {
		@Override
		public EligibilityCriteria mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new EligibilityCriteria(rs.getString("clinical_trial_id"), rs.getInt("id"),
					rs.getString("utterance"), rs.getInt("inc_exc"));
		}
	}

}