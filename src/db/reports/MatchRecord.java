package db.reports;

import java.util.HashMap;
import java.util.Map;

public class MatchRecord implements Record {
	private String fsn;
	private String trial;
	private String utterance;
	private String phrase;
	private String synonym;
	private String matched_words;

	@Override
	public void printHeaders() {
		// System.out.format("%10s | %10s | %10s | %30s | %-17s \n", "CUI",
		// "SCTID", "FRECUENCY", "TYPE", "CONCEPT");

	}

	@Override
	public void printRecord() {
		// System.out.format("%10s | %10s | %10d | %30s | %-17s \n", cui, sctid,
		// frecuency, type, concept);
	}

	@Override
	public Map<Integer, String> getHeaders() {
		HashMap<Integer, String> record = new HashMap<>();
		record.put(0, fsn);
		record.put(1, "TRIAL");
		record.put(2, "UTTERANCE");
		record.put(3, "PHRASE");
		record.put(4, "SYNONYM");
		record.put(5, "MATCHED_WORDS");
		return record;
	}

	@Override
	public Map<Integer, String> getRecord() {
		HashMap<Integer, String> record = new HashMap<>();
		record.put(1, trial);
		record.put(2, utterance);
		record.put(3, phrase);
		record.put(4, synonym);
		record.put(5, matched_words);
		return record;
	}

	public String getFsn() {
		return fsn;
	}

	public String getTrial() {
		return trial;
	}

	public String getUtterance() {
		return utterance;
	}

	public String getPhrase() {
		return phrase;
	}

	public String getSynonym() {
		return synonym;
	}

	public String getMatchedwords() {
		return matched_words;
	}

	public void setFsn(String fsn) {
		this.fsn = fsn;
	}

	public void setTrial(String trial) {
		this.trial = trial;
	}

	public void setUtterance(String utterance) {
		this.utterance = utterance;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public void setSynonym(String synonym) {
		this.synonym = synonym;
	}

	public void setMatchedWords(String matched_words) {
		this.matched_words = matched_words;
	}

}
