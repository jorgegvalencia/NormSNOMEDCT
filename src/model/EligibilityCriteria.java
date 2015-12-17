package model;

import java.util.List;

import nlp.Match;

public class EligibilityCriteria {
	private String trial;
	private int number;
	private String utterance;
	private List<Match> matches;
	private int criteriaType;

	public EligibilityCriteria(String trial, int number, String utterance, int type) {
		this.trial = trial;
		this.number = number;
		this.utterance = utterance;
		criteriaType = type;
	}

	public int getNumber() {
		return number;
	}

	public String getTrial() {
		return trial;
	}

	public String getUtterance() {
		return utterance;
	}

	public int getCriteriaType() {
		return criteriaType;
	}

	public List<Match> getMatches() {
		return matches;
	}

	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}

}
