package db.reports;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TrialConceptsMapper implements RowMapper<TrialConceptRecord> {

	@Override
	public TrialConceptRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
		TrialConceptRecord tcr = new TrialConceptRecord();
		tcr.setConcept(rs.getString("CONCEPT"));
		tcr.setCui(rs.getString("CUI"));
		tcr.setSctid(rs.getString("SCTID"));
		tcr.setTrial(rs.getString("TRIAL"));
		tcr.setPhrase(rs.getString("PHRASE"));
		return tcr;
	}

}
