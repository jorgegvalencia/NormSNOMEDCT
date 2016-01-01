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

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public class XMLDocumentParser {

	public XMLDocumentParser() {

	}

	public static void main(String[] args) {
		downloadClinicalTrial("NCT00987766");
		builderClinicalTrial2("NCT00987766");
	}

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

	public static void builderClinicalTrial2(String nctid) {
		try {
			String filePath = "resources/trials/" + nctid + ".xml";
			File file = new File(filePath);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLEventReader eventReader = factory.createXMLEventReader(br);
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.getEventType() == XMLStreamConstants.START_ELEMENT)
					System.out.println(resolveEventType(event.getEventType()) + "::"
							+ event.asStartElement().getName().getLocalPart());
				else if (event.getEventType() == XMLStreamConstants.END_ELEMENT)
					System.out.println(resolveEventType(event.getEventType()) + "::"
							+ event.asEndElement().getName().getLocalPart());
				else if (event.getEventType() == XMLStreamConstants.CHARACTERS)
					System.out.println(resolveEventType(event.getEventType()) + "::" + "\""
							+ event.asCharacters().getData().trim() + "\"");
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String resolveEventType(int element) {
		switch (element) {
		case 1:
			return "START_ELEMENT";
		case 2:
			return "END_ELEMENT";
		case 3:
			return "PROCESSING_INSTRUCTION";
		case 4:
			return "CHARACTERS";
		case 5:
			return "COMMENT";
		case 6:
			return "SPACE";
		case 7:
			return "START_DOCUMENT";
		case 8:
			return "END_DOCUMENT";
		case 9:
			return "ENTITY_REFERENCE";
		case 10:
			return "ATTRIBUTE";
		case 11:
			return "DTD";
		case 12:
			return "CDATA";
		case 13:
			return "NAMESPACE";
		case 14:
			return "NOTATION_DECLARATION";
		case 15:
			return "ENTITY_DECLARATION";
		default:
		}
		return null;
	}
}
