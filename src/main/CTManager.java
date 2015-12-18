package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import nlp.ProcessingUnit;

public class CTManager {

	public CTManager() {

	}

	/**
	 * Checks if the clinical trial with the specified nctid is already in the
	 * local files.
	 *
	 * @param nctid
	 * @return true if the XML file does exists, false otherwise.
	 */
	private static boolean checkLocalFile(String nctid) {
		String filePath = "resources/trials/" + nctid + ".xml";
		File f = new File(filePath);
		return f.exists();
	}

	/**
	 * Downloads the clinical trial specified as an XML file.
	 *
	 * @param nctid
	 */
	private static void downloadClinicalTrial(String nctid) {
		String path = "https://clinicaltrials.gov/show/" + nctid + "?displayxml=true";
		String filePath = "resources/trials/" + nctid + ".xml";
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "text/xml");
			if (conn.getResponseCode() != 200)
				throw new RuntimeException("HTTP Error: " + conn.getResponseCode());
			else {
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream()), "utf-8"));
				File trial = new File(filePath);
				BufferedWriter bw = new BufferedWriter(new FileWriter(trial));
				String line;
				while ((line = br.readLine()) != null) {
					bw.write(line);
					bw.newLine();
				}
				bw.close();
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Builds a ClinicalTrial object from the XML file of the trial specified by
	 * the ntcid.
	 *
	 * @param nctid
	 * @return
	 */
	public static ProcessingUnit buildProcessingUnit(String nctid) {
		ProcessingUnit pu = new ProcessingUnit(nctid);
		String filePath = "resources/trials/" + nctid + ".xml";
		// System.out.println("Checking local files...");
		if (!checkLocalFile(nctid)) {
			System.out.println("Sending request to clinicaltrials.gov...");
			downloadClinicalTrial(nctid);
		}
		try {
			File file = new File(filePath);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLStreamReader streamReader = factory.createXMLStreamReader(br);
			String currentElement = null;
			while (streamReader.hasNext()) {
				int event = streamReader.next();
				switch (event) {
				case XMLStreamConstants.START_ELEMENT:
					currentElement = streamReader.getLocalName();
					if (currentElement.equals("brief_title"))
						pu.getBuilder().setTitle(streamReader.getElementText());
					else if (currentElement.equals("condition"))
						pu.getBuilder().setTopic(streamReader.getElementText());
					break;
				case XMLStreamConstants.CHARACTERS:
					if (currentElement.equals("criteria")) {
						streamReader.next();
						if (streamReader.getEventType() == XMLStreamReader.START_ELEMENT) {
							currentElement = streamReader.getLocalName();
							pu.getBuilder().setCriteria((streamReader.getElementText()));
						}
					} else if (currentElement.equals("brief_summary")) {
						streamReader.next();
						if (streamReader.getEventType() == XMLStreamReader.START_ELEMENT) {
							currentElement = streamReader.getLocalName();
							pu.getBuilder().setAttribute("brief_summary", streamReader.getElementText());
						}
					} else
						break;
				}
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return pu;
	}
}
