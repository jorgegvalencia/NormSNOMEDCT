package model;

import java.util.HashMap;
import java.util.Map;

public class ClinicalTrial {

	private String nctid;
	private String title;
	private String topic;
	private CriteriaSet criteriaset;
	private Map<String, String> attributes;

	private ClinicalTrial(ClinicalTrialBuilder builder) {
		nctid = builder.nctid;
		title = builder.title;
		topic = builder.topic;
		criteriaset = builder.criteriaset;
		attributes = builder.attributes;
	}

	public String getNctid() {
		return nctid;
	}

	public String getTitle() {
		return title;
	}

	public String getTopic() {
		return topic;
	}

	public CriteriaSet getCriteria() {
		return criteriaset;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public static class ClinicalTrialBuilder {
		private String nctid;
		private String title;
		private String topic;
		private CriteriaSet criteriaset;
		private Map<String, String> attributes;

		public ClinicalTrialBuilder(String nctid) {
			this.nctid = nctid;
		}

		public ClinicalTrial build() {
			return new ClinicalTrial(this);
		}

		public ClinicalTrialBuilder setTitle(String title) {
			this.title = title;
			return this;
		}

		public ClinicalTrialBuilder setTopic(String topic) {
			if (this.topic == null)
				this.topic = topic;
			else
				this.topic = this.topic + ", " + topic;
			return this;
		}

		public ClinicalTrialBuilder setCriteria(String criteria) {
			CriteriaSet criteriaset = new CriteriaSet(nctid, criteria);
			this.criteriaset = criteriaset;
			return this;
		}

		public ClinicalTrialBuilder setAttribute(String attr, String value) {
			if (attributes == null)
				attributes = new HashMap<String, String>();
			attributes.put(attr, value);
			return this;
		}

		public CriteriaSet getCriteriaSet() {
			return criteriaset;
		}
	}
}
