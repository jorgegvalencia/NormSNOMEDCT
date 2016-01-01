package db.reports;

import java.util.HashMap;
import java.util.Map;

public class ConceptFrecuencyRecord implements Record {
	private String cui;
	private String sctid;
	private int frecuency;
	private String type;
	private String concept;

	@Override
	public void printHeaders() {
		System.out.format("%10s | %10s | %10s | %30s | %-17s \n", "CUI", "SCTID", "FRECUENCY", "TYPE", "CONCEPT");
	}

	@Override
	public void printRecord() {
		System.out.format("%10s | %10s | %10d | %30s | %-17s \n", cui, sctid, frecuency, type, concept);
	}

	public static Map<Integer, String> getHeaderFields() {
		HashMap<Integer, String> record = new HashMap<>();
		record.put(0, "CUI");
		record.put(1, "SCTID");
		record.put(2, "FRECUENCY");
		record.put(3, "TYPE");
		record.put(4, "CONCEPT");
		return record;
	}

	@Override
	public Map<Integer, String> getRecordFields() {
		HashMap<Integer, String> record = new HashMap<>();
		record.put(0, cui);
		record.put(1, sctid);
		record.put(2, Integer.toString(frecuency));
		record.put(3, type);
		record.put(4, concept);
		return record;
	}

	public void setCui(String cui) {
		this.cui = cui;
	}

	public void setSctid(String sctid) {
		this.sctid = sctid;
	}

	public void setFrecuency(int frecuency) {
		this.frecuency = frecuency;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public void setType(String type) {
		this.type = type;
	}

}
