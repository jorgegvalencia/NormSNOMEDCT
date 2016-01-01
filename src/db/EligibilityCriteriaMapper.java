package db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import model.EligibilityCriteria;

public class EligibilityCriteriaMapper implements RowMapper<EligibilityCriteria> {
	@Override
	public EligibilityCriteria mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new EligibilityCriteria(rs.getString("clinical_trial_id"), rs.getInt("id"), rs.getString("utterance"),
				rs.getInt("inc_exc"));
	}
}
