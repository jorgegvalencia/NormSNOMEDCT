package model;

import java.util.ArrayList;
import java.util.List;

import nlp.Preprocessor;

public class CriteriaSet {
	private String criteriaTextBlock;
	private List<EligibilityCriteria> eclist;

	public CriteriaSet(String nctid, String criteria) {
		criteriaTextBlock = criteria;
		eclist = getEligibilityCriteriaFromText(nctid, criteria);
	}

	private List<EligibilityCriteria> getEligibilityCriteriaFromText(String trial, String criteria) {
		// Preprocess raw criteria
		String processed_criteria = Preprocessor.processEligibilityCriteria(criteria);
		// Get the utterances for each EC
		List<String> uttList = Preprocessor.getSentencesFromText(processed_criteria);
		List<EligibilityCriteria> ecList = new ArrayList<EligibilityCriteria>();
		int type = 0;
		for (int i = 0; i < uttList.size(); i++) { // for each utterance
			String utt = uttList.get(i);
			if (utt.toLowerCase().contains("Inclusion criteria"))
				type = 1;
			else if (utt.toLowerCase().contains("Exclusion criteria"))
				type = 2;
			EligibilityCriteria ec = new EligibilityCriteria(trial, i, utt, type);
			ecList.add(ec);
		}
		return ecList;
	}

	public String getCriteriaTextBlock() {
		return criteriaTextBlock;
	}

	public List<EligibilityCriteria> getEclist() {
		return eclist;
	}
}
