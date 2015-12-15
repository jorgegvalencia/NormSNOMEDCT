package nlp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public final class Preprocessor {
	private static final List<String> STOP_WORDS = new ArrayList<String>(
			Arrays.asList("and", "by", "for", "in", "of", "or", "the", "to", "with", "no"));
	private static final Pattern ASCII_PATTERN = Pattern.compile("[^\\p{ASCII}]+");

	public static String processEligibilityCriteria(String utterance) {
		String refinedText;
		refinedText = ASCII_PATTERN.matcher(utterance).replaceAll(" ");
		// Elimina los puntos de contenido numericos
		refinedText = refinedText.replaceAll("([0-9]+\\.(?=\\s))+\\s", " - ");
		// Elimina los guiones de puntos de contenido
		refinedText = refinedText.replaceAll("-\\s+(?=[A-Z])", "");
		// A las oraciones sin punto final y con salto de linea se le anade un
		// punto final
		refinedText = refinedText.replaceAll("(?<=.)\n\\s+(?=[A-Z]{1}[a-z])", ". ");
		refinedText = refinedText.replaceAll("\\.{2}", ". ");
		refinedText = refinedText.replaceAll("\\s+", " ");
		refinedText = refinedText.replaceAll("\\\"", "");
		return refinedText;
	}

	public static List<String> getSentencesFromText(String text) {
		NLPSentenceDetector sd = new NLPSentenceDetector("resources/en-sent.bin");
		return sd.detectSentences(text);
	}

	public static String removeStopWords(String nounp) {
		String refinedText = nounp;
		for (String sw : STOP_WORDS) {
			refinedText = refinedText.replaceAll("\\s" + sw + "\\s", " ");
		}
		return refinedText;
	}
}
