package test;

import java.util.ArrayList;
import java.util.List;

import db.DBManager;
import db.reports.Report;
import main.CTManager;
import nlp.ProcessingUnit;

public class Test {

	// private static final String[] TRIALS = { "NCT02102490", "NCT01358877",
	// "NCT00148876", "NCT01633060","NCT01700257" };

	public static void main(String[] args) {
		Report rp = DBManager.getInstance().getCFReport();
		rp.buildReport();
	}

	private static ProcessingUnit init(String nctid) {
		return CTManager.buildProcessingUnit(nctid);
	}

	private static List<ProcessingUnit> init(List<String> trials) {
		List<ProcessingUnit> pulist = new ArrayList<>();
		for (String nctid : trials)
			pulist.add(CTManager.buildProcessingUnit(nctid));
		return pulist;
	}
}
