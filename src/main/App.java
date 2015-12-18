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
	private static final int MEAN = 10;
	private static final String DEFAULTPATH = "resources/trials/";
	private static final String NAME = "App";

	public static void main(String[] args) {
		if (args.length == 4 && args[0].equals("-f")) {
			String filepath = args[1];
			if (new File(filepath).getAbsoluteFile().exists()) {
				int offset = Integer.parseInt(args[2]);
				int limit = Integer.parseInt(args[3]);
				processBatch(filepath, offset, limit);
			} else {
				System.err.println("The filepath: " + filepath + " doesn't exists.");
				usage();
			}
		} else if (args.length == 1 && args[0].equals("-d"))
			processBatch(DEFAULTPATH, 0, 10000);
		else if (args.length == 2 && args[0].equals("-t")) {
			String trial = args[1];
			processTrial(trial);
		} else
			usage();
	}

	private static void usage() {
		System.out.println("Usage: " + NAME
				+ " [-f <filepath> <offset> <limit>] || [-d] || [-t <nctid>] || [-r <nctid>] "
				+ "\n -f: Process a set of trials located in the directory <filepath>. Files must be in XML format."
				+ "\n\t <offset>: The start file from the directory." + "\n\t <limit>: The maximum files to process. "
				+ "\n -d: Default: Process all trial xml files located in default directory: \"/resources/trials\""
				+ "\n -t: Process a trial." + "\n\t <nctid>: Identifier of the trial file." + "\n -r: Build a report."
				+ "\n\t <nctid>: Identifier of the trial file.");
	}

	private static List<ProcessingUnit> processBatch(String filepath, int offset, int limit) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String path = filepath;
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
