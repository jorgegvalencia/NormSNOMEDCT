package db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import model.ClinicalTrial;
import model.ClinicalTrial.ClinicalTrialBuilder;

public class ClinicalTrialMapper implements RowMapper<ClinicalTrial> {
	@Override
	public ClinicalTrial mapRow(ResultSet rs, int rowNum) throws SQLException {
		ClinicalTrialBuilder ctb = new ClinicalTrial.ClinicalTrialBuilder(rs.getString("id"));
		ctb.setTitle(rs.getString("title"));
		ctb.setTopic(rs.getString("studytype"));
		return ctb.build();
	}
}