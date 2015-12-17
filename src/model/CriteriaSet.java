package model;

import java.util.List;

public class CriteriaSet {
	private String criteria;
	private List<EligibilityCriteria> eclist;

	public CriteriaSet(String criteria) {
		this.criteria = criteria;
		// Preprocessor (criteria)
	}
}
