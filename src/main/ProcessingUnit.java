package main;

import model.ClinicalTrial;
import model.ClinicalTrial.ClinicalTrialBuilder;

public class ProcessingUnit {
	private ClinicalTrialBuilder builder;
	private ClinicalTrial clinicalTrial;
	private double time;

	public ProcessingUnit(String nctid) {
		builder = new ClinicalTrialBuilder(nctid);
		time = 0.0;
	}

	public ClinicalTrial getClinicalTrial() {
		return clinicalTrial;
	}

	public double getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
		clinicalTrial = builder.build();
	}

	public ClinicalTrialBuilder getBuilder() {
		return builder;
	}

}
