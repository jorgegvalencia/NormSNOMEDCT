package nlp;

import java.util.List;

import model.Concept;

public class Match {
	private Concept concept;
	private String phrase;
	private String term;
	private String prefered;
	private List<String> matchedWords;

	public Match(Concept c, String phrase, String t, String pref, List<String> mw) {
		concept = c;
		this.phrase = phrase;
		term = t;
		prefered = pref;
		matchedWords = mw;
	}

	public Concept getConcept() {
		return concept;
	}

	public String getPhrase() {
		return phrase;
	}

	public String getTerm() {
		return term;
	}

	public String getPrefered() {
		return prefered;
	}

	public List<String> getMatchedWords() {
		return matchedWords;
	}

}
