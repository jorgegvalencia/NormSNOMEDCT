package test;

import java.util.List;

import main.CTManager;
import model.ClinicalTrial;
import model.EligibilityCriteria;
import nlp.ConceptExtractor;

public class Test {

	private static final String[] TRIALS = { "NCT02102490", "NCT01358877", "NCT00148876", "NCT01633060",
			"NCT01700257" };

	public static void main(String[] args) {
		CTManager ctm = new CTManager();
		ctm.buildClinicalTrial("NCT00378313").print();
		for (String trial : TRIALS) {
			metamapTest(trial);
			System.out.println("\n");
		}

	}

	public static void metamapTest(String nctid) {
		CTManager ctm = new CTManager();
		ConceptExtractor ce = new ConceptExtractor("luria.dia.fi.upm.es");
		ClinicalTrial ct = ctm.buildClinicalTrial(nctid);
		ct.print();
		System.out.println(ct.getCriteria());
		List<EligibilityCriteria> ecList = ce.getEligibilityCriteriaFromText(ct.getNctid(), ct.getCriteria());
		for (EligibilityCriteria ec : ecList)
			if (!ec.getConcepts().isEmpty())
				ec.print();
	}
}
