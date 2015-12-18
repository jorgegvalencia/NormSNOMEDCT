package main;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nlp.ConceptExtractor;
import nlp.ProcessingUnit;

public class App {
	private static final String HOST = "luria.dia.fi.upm.es";
	private static final boolean STORE = true;

	public static void main(String[] args) {
		processBatch(0, 5);
	}

	private static List<ProcessingUnit> processBatch(int offset, int limit) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String path = "resources/trials/";
		File[] files = new File(path).listFiles();
		ConceptExtractor ce = new ConceptExtractor(HOST);
		List<ProcessingUnit> pulist = new ArrayList<>();

		// Time estimation
		System.out.print("Processing trials... estimated time: ");
		if (((files.length - offset < limit) ? files.length - offset : limit) * 18 / 60 > 60)
			System.out.print(((files.length - offset < limit) ? files.length - offset : limit) * 18 / 3600 + " h\n");
		else
			System.out.print(((files.length - offset < limit) ? files.length - offset : limit) * 18 / 60 + " min\n");

		// Processing
		int j = 0;
		for (int i = offset; i < files.length && i < limit + offset; i++) {
			File f = files[i];
			if (j >= limit + offset)
				break;
			if (f.getName().contains("NCT")) {
				j++;
				System.out.print(dateFormat.format(new Date()));
				System.out.print(" [" + j + "]" + "[" + f.getName() + "] ");
				ProcessingUnit pu = CTManager.buildProcessingUnit(f.getName().replace(".xml", ""));
				ce.process(pu, STORE);
				System.out.println(pu.getTime() + " s");
				pulist.add(pu);
			}
		}
		return pulist;
	}
}
