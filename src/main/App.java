package main;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import db.reports.ReportGenerator;
import nlp.ConceptExtractor;
import nlp.ProcessingUnit;

public class App {
	private static final String HOST = "luria.dia.fi.upm.es";
	private static final boolean STORE = true;
	private static final int MEAN = 10;

	public static void main(String[] args) {
		ReportGenerator.getTCReport("NCT00148876").buildReport();
		// processTrial("NCT00148876");
		// processBatch(0, 5);
	}

	private static List<ProcessingUnit> processBatch(int offset, int limit) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String path = "resources/trials/";
		File[] files = new File(path).listFiles();
		ConceptExtractor ce = new ConceptExtractor(HOST);
		List<ProcessingUnit> pulist = new ArrayList<>();

		// Time estimation
		System.out.print("Processing trials... estimated time: ");
		if (((files.length - offset < limit) ? files.length - offset : limit) * MEAN / 60 > 60)
			System.out.print(((files.length - offset < limit) ? files.length - offset : limit) * MEAN / 3600 + " h\n");
		else if (((files.length - offset < limit) ? files.length - offset : limit) * MEAN / 60 > 1)
			System.out.print(((files.length - offset < limit) ? files.length - offset : limit) * MEAN / 60 + " min\n");
		else
			System.out.print(((files.length - offset < limit) ? files.length - offset : limit) * MEAN + " s\n");

		// Processing
		int j = 0;
		for (int i = offset; i < files.length && i < limit + offset; i++) {
			File f = files[i];
			if (j >= limit + offset)
				break;
			if (f.getName().contains("NCT")) {
				j++;
				System.out.print(dateFormat.format(new Date()));
				System.out.print(" [" + j + "]" + "[" + f.getName().replace(".xml", "") + "] ");
				ProcessingUnit pu = CTManager.buildProcessingUnit(f.getName().replace(".xml", ""));
				ce.process(pu, STORE);
				System.out.println(pu.getTime() + " s");
				pulist.add(pu);
			}
		}
		System.out.println("...done");
		return pulist;
	}

	private static ProcessingUnit processTrial(String nctid) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		ConceptExtractor ce = new ConceptExtractor(HOST);
		System.out.print(dateFormat.format(new Date()));
		System.out.print(" [*]" + "[" + nctid + "] ");
		ProcessingUnit pu = CTManager.buildProcessingUnit(nctid);
		ce.process(pu, STORE);
		System.out.println(pu.getTime() + " s");
		return pu;
	}
}
