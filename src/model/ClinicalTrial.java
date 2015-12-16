package model;

import java.net.URL;

public class ClinicalTrial {

	private URL url;
	private String nctid;
	private String title;
	private String briefSummary;
	private String startDate;
	private String studyType;
	// eligibility
	// private String studyPop;
	// private String samplingMethod;
	private String criteria;
	// private String minimumAge;
	// private String maximumAge;

	public void print() {
		System.out.format("%17s:\t%s\n" + "%17s:\t%s\n" + "%17s:\t%s\n" + "%17s:\t%s\n" + "%17s:\t%s\n" + "%17s:\t%s\n",
				"CT Title", title, "NCT ID", nctid, "URL", url, "Start date", startDate, "Study type", studyType,
				"Brief Summary", briefSummary);
	}

	private ClinicalTrial(ClinicalTrialBuilder builder) {
		nctid = builder.nctid;
		title = builder.title;
		briefSummary = builder.briefSummary;
		startDate = builder.startDate;
		studyType = builder.studyType;
		// studyPop = builder.studyPop;
		// samplingMethod = builder.samplingMethod;
		criteria = builder.criteria;
		// minimumAge = builder.minimumAge;
		// maximumAge = builder.maximumAge;
	}

	public URL getUrl() {
		return url;
	}

	public String getNctid() {
		return nctid;
	}

	public String getTitle() {
		return title;
	}

	public String getBriefSummary() {
		return briefSummary;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getStudyType() {
		return studyType;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public static class ClinicalTrialBuilder {
		private URL url;
		private String nctid;
		private String title;
		private String briefSummary;
		private String startDate;
		private String studyType;
		// eligibility
		private String studyPop;
		private String samplingMethod;
		private String criteria;
		private String minimumAge;
		private String maximumAge;

		public ClinicalTrialBuilder() {
		}

		public ClinicalTrial build() {
			return new ClinicalTrial(this);
		}

		public ClinicalTrialBuilder setUrl(URL url) {
			this.url = url;
			return this;
		}

		public ClinicalTrialBuilder setNctId(String nctid) {
			this.nctid = nctid;
			return this;
		}

		public ClinicalTrialBuilder setTitle(String title) {
			this.title = title;
			return this;
		}

		public ClinicalTrialBuilder setBriefSummary(String briefSummary) {
			this.briefSummary = briefSummary;
			return this;
		}

		public ClinicalTrialBuilder setStartDate(String startDate) {
			this.startDate = startDate;
			return this;
		}

		public ClinicalTrialBuilder setStudyType(String studyType) {
			this.studyType = studyType;
			return this;
		}

		public ClinicalTrialBuilder setStudyPop(String studyPop) {
			this.studyPop = studyPop;
			return this;
		}

		public ClinicalTrialBuilder setSamplingMethod(String samplingMethod) {
			this.samplingMethod = samplingMethod;
			return this;
		}

		public ClinicalTrialBuilder setCriteria(String criteria) {
			this.criteria = criteria;
			return this;
		}

		public ClinicalTrialBuilder setMinimumAge(String minimumAge) {
			this.minimumAge = minimumAge;
			return this;
		}

		public ClinicalTrialBuilder setMaximumAge(String maximumAge) {
			this.maximumAge = maximumAge;
			return this;
		}
	}
}
