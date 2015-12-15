package model;

import java.util.List;

public class EligibilityCriteria {
	private String trial;
	private int number;
	private String utterance;
	private List<Concept> concepts;
	private int criteriaType;

	private EligibilityCriteria(EligibilityCriteriaBuilder builder) {
		utterance = builder.utterance;
		concepts = builder.concepts;
		criteriaType = builder.criteriaType;
	}

	public void print() {
		System.out.println("EC: " + utterance);
		switch (criteriaType) {
		case 1:
			System.out.println("Type: Inclusion");
			break;
		case 2:
			System.out.println("Type: Exclusion");
			break;
		default:
			System.out.println("Type: N/A");
		}
		for (Concept concept : concepts)
			concept.print();
		System.out.println(
				"+----------------------------------------------------------------------------------------------------------------------------------+");
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

	public List<Concept> getConcepts() {
		return concepts;
	}

	public int getCriteriaType() {
		return criteriaType;
	}

	public static class EligibilityCriteriaBuilder {
		private String trial;
		private int number;
		private String utterance;
		private List<Concept> concepts;
		private int criteriaType;

		public EligibilityCriteriaBuilder(String trial, int number, String utterance, int criteriaType) {
			this.trial = trial;
			this.number = number;
			this.utterance = utterance;
			this.criteriaType = criteriaType;
		}

		public void setConcepts(List<Concept> concepts) {
			this.concepts = concepts;
		}

		public EligibilityCriteria build() {
			return new EligibilityCriteria(this);
		}
	}
}
