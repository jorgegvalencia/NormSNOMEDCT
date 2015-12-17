package test;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import main.CTManager;
import nlp.ProcessingUnit;

public class Test {

	// private static final String[] TRIALS = { "NCT02102490", "NCT01358877",
	// "NCT00148876", "NCT01633060","NCT01700257" };

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		// CTManager ctm = new CTManager();
		// ctm.buildClinicalTrial("NCT00378313").print();
		// for (String trial : TRIALS) {
		// metamapTest(trial);
		// System.out.println("\n");
		// }
		long endTime = System.nanoTime();
		NumberFormat format = NumberFormat.getInstance(Locale.US);
		Number number;
		try {
			number = format.parse(String.format("%.2f", (endTime - startTime) / Math.pow(10, 9)));
			double time = number.doubleValue();
			System.out.println(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private static ProcessingUnit init(String nctid) {
		return CTManager.buildProcessingUnit(nctid);
	}

	private static List<ProcessingUnit> init(List<String> trials) {
		List<ProcessingUnit> pulist = new ArrayList<>();
		for (String nctid : trials) {
			pulist.add(CTManager.buildProcessingUnit(nctid));
		}
		return pulist;
	}
}
