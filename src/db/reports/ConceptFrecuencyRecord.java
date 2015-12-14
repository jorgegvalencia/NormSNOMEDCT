package db.reports;

public class ConceptFrecuencyRecord implements Record {
	private String cui;
	private String sctid;
	private int frecuency;
	private String type;
	private String concept;

	@Override
	public void printHeaders() {
		System.out.format("%10s | %10s | %10s | %30s | %17s \n", "CUI", "SCTID", "FRECUENCY", "TYPE", "CONCEPT");
	}

	@Override
	public void printRecord() {
		System.out.format("%10s | %10s | %10d | %30s | %17s \n", cui, sctid, frecuency, type, concept);
	}

	@Override
	public String getHeaders() {
		return String.format("%10s | %10s | %10d | %17s | %17s \n", "CUI", "SCTID", "FRECUENCY", "TYPE", "CONCEPT");
	}

	@Override
	public String getRecord() {
		return String.format("%10s | %10s | %10d | %17s | %17s \n", cui, sctid, frecuency, type, concept);
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
