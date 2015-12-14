package model;

import java.net.URL;

import db.ClinicalTrialDAO;

public class ClinicalTrial implements ClinicalTrialDAO {

	private URL url;
	private String nctId;
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

	@Override
	public void create(String nctid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String nctid) {
		// TODO Auto-generated method stub

	}

	@Override
	public ClinicalTrial get(String nctid) {
		// TODO Auto-generated method stub
		return null;
	}

	public void print() {
		System.out.format("%17s:\t%s\n" + "%17s:\t%s\n" + "%17s:\t%s\n" + "%17s:\t%s\n" + "%17s:\t%s\n" + "%17s:\t%s\n",
				"CT Title", title, "NCT ID", nctId, "URL", url, "Start date", startDate, "Study type", studyType,
				"Brief Summary", briefSummary);
	}

	private ClinicalTrial(ClinicalTrialBuilder builder) {
		nctId = builder.nctid;
		title = builder.title;
		briefSummary = builder.briefSummary;
		startDate = builder.startDate;
		studyType = builder.studyType;
		studyPop = builder.studyPop;
		samplingMethod = builder.samplingMethod;
		criteria = builder.criteria;
		minimumAge = builder.minimumAge;
		maximumAge = builder.maximumAge;
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

		public ClinicalTrialBuilder(String nctid, String title, String studyType, String criteria) {
			this.nctid = nctid;
			this.title = title;
			this.studyType = studyType;
			this.criteria = criteria;
		}

		public ClinicalTrial build() {
			return new ClinicalTrial(this);
		}

		public void setUrl(URL url) {
			this.url = url;
		}

		public void setNctId(String nctid) {
			this.nctid = nctid;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public void setBriefSummary(String briefSummary) {
			this.briefSummary = briefSummary;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public void setStudyType(String studyType) {
			this.studyType = studyType;
		}

		public void setStudyPop(String studyPop) {
			this.studyPop = studyPop;
		}

		public void setSamplingMethod(String samplingMethod) {
			this.samplingMethod = samplingMethod;
		}

		public void setCriteria(String criteria) {
			this.criteria = criteria;
		}

		public void setMinimumAge(String minimumAge) {
			this.minimumAge = minimumAge;
		}

		public void setMaximumAge(String maximumAge) {
			this.maximumAge = maximumAge;
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

		public String getStudyPop() {
			return studyPop;
		}

		public String getSamplingMethod() {
			return samplingMethod;
		}

		public String getCriteria() {
			return criteria;
		}

		public String getMinimumAge() {
			return minimumAge;
		}

		public String getMaximumAge() {
			return maximumAge;
		}
	}

}