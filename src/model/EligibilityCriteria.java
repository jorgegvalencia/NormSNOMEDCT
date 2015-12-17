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

	/*
	 * private List<Concept> removeRedundancies(List<Concept> concepts) {
	 * List<Concept> result = new ArrayList<>(); Map<String, Concept> index =
	 * new HashMap<String, Concept>(); for (Concept c : concepts) if
	 * (!index.containsKey(c.getCui())) index.put(c.getCui(), c); else { String
	 * ph1 = c.getPhrase(); String ph2 = index.get(c.getCui()).getPhrase(); if
	 * (ph1.equals(ph2)) continue; else index.get(c.getCui()).setPhrase(ph1 +
	 * " + " + ph2); } result.addAll(index.values()); return result; }
	 */

}
