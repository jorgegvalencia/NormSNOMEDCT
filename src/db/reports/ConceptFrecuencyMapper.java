package db.reports;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ConceptFrecuencyMapper implements RowMapper<ConceptFrecuencyRecord> {

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
