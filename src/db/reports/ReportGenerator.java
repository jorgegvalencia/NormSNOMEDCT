package db.reports;

import db.DBManager;

public class ReportGenerator {

	public static ConceptFrecuencyReport getCFReport() {
		return DBManager.getInstance().getCFReport();
	}

	public static TrialConceptsReport getTCReport(String nctid) {
		return DBManager.getInstance().getTCReport(nctid);
	}
}
