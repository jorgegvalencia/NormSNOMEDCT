package db.reports;

public class TrialConceptRecord implements Record {
	private String trial;
	private String sctid;
	private String cui;
	private String concept;
	private String phrase;

	@Override
	public void printHeaders() {
		System.out.format("%10s\n %10s | %10s | %-100s | %s\n", trial, "CUI", "SCTID", "CONCEPT", "PHRASE");
	}

	@Override
	public void printRecord() {
		System.out.format(" %10s | %10s | %-100s | %s \n", cui, sctid, concept, phrase);
	}

	@Override
	public String getHeaders() {
		return String.format("%10s\n %10s | %10d | %-100s | %s\n", trial, "CUI", "SCTID", "CONCEPT", "PHRASE");
	}

	@Override
	public String getRecord() {
		return String.format(" %10s | %10s | %-100s | %s \n", cui, sctid, concept, phrase);
	}

	public void setTrial(String trial) {
		this.trial = trial;
	}

	public void setSctid(String sctid) {
		this.sctid = sctid;
	}

	public void setCui(String cui) {
		this.cui = cui;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

}
