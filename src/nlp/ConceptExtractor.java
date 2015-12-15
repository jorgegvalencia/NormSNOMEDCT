package nlp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import db.DBManager;
import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Result;
import gov.nih.nlm.nls.metamap.Utterance;
import model.Concept;
import model.Concept.ConceptBuilder;
import model.EligibilityCriteria;

public class ConceptExtractor {
	// Index for status of concepts
	private static HashMap<String, Integer> index = new HashMap<>(); // sctid,status
	private static HashSet<String> excluded = new HashSet<String>(); // CUI
	private static MetaMapApi mmapi;
	private static String options = "-y -Q 2 -i -k cell,fish,ftcn,idcn,inpr,menp,mnob,podg,qlco,qnco,spco,tmco -R SNOMEDCT_US";
	private DBManager db;

	public ConceptExtractor() {
		try {
			db = new DBManager();
			mmapi = new MetaMapApiImpl();
			mmapi.setOptions(options);
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}

	public ConceptExtractor(String host) {
		try {
			db = new DBManager();
			mmapi = new MetaMapApiImpl();
			mmapi.setHost(host);
			mmapi.setOptions(options);
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}

	public List<EligibilityCriteria> getEligibilityCriteriaFromText(String trial, String criteria) {
		List<EligibilityCriteria> ecList = new ArrayList<EligibilityCriteria>();
		int type = 0;
		// Process raw criteria
		String processed_criteria = Preprocessor.processEligibilityCriteria(criteria);
		// Get the utterances for each EC
		List<String> uttList = Preprocessor.getSentencesFromText(processed_criteria);
		// for each utterance
		for (int i = 0; i < uttList.size(); i++) {
			String utt = uttList.get(i);
			if (utt.contains("Inclusion") || utt.contains("inclusion")) {
				type = 1;
			} else if (utt.contains("Exclusion") || utt.contains("exclusion")) {
				type = 2;
			}
			// get the concepts from the utterance
			List<Concept> concepts = getConceptsFromText(utt);
			// !! REVIEW CONCEPTS
			List<Concept> rs = removeRedundancies(concepts);
			// create EligibilityCriteria object
			EligibilityCriteria ec = new EligibilityCriteria.EligibilityCriteriaBuilder(trial, i, utt, type)
					.setConcepts(rs).build();
			ecList.add(ec);
		}
		ecList = filterConcepts(ecList);
		return ecList;
	}

	private List<Concept> getConceptsFromText(String text) {
		List<Concept> concepts = new ArrayList<Concept>();
		try {
			List<String> np = getNounPhrasesFromText(text);
			for (String nounp : np) {
				// !!! FILTER: search for patterns to manually map frequent
				// concepts
				// !!! PROCESS NOUN PHRASE BEFORE CALLING METAMAP
				List<Result> result = metamapQuery(Preprocessor.removeStopWords(nounp).toLowerCase());
				for (Result res : result) {
					for (Utterance uttr : res.getUtteranceList()) {
						for (PCM pcm : uttr.getPCMList()) {
							for (Mapping map : pcm.getMappingList()) {
								for (Ev mapEv : map.getEvList()) {
									String sctid = "-";
									String fullySpecifiedName;
									List<String> sctidList = db.getSCTID(mapEv.getConceptId()); // getProperSCUI
									if (!sctidList.isEmpty()) {
										sctid = sctidList.get(0);
									} else {
										continue;
									}
									if ((fullySpecifiedName = db.getFSN(sctid)) == null) {
										continue;
									}
									ConceptBuilder cb = new Concept.ConceptBuilder(mapEv.getConceptId(), sctid,
											fullySpecifiedName);
									concepts.add(cb.build());
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return concepts;
	}

	private List<String> getNounPhrasesFromText(String text) {
		List<String> np = new ArrayList<String>();
		try {
			List<Result> result = metamapQuery(text);
			for (Result res : result) {
				for (Utterance uttr : res.getUtteranceList()) {
					for (PCM pcm : uttr.getPCMList()) {
						String nounphrase = pcm.getPhrase().getPhraseText();
						np.add(nounphrase);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return np;
	}

	private List<Concept> removeRedundancies(List<Concept> concepts) {
		List<Concept> result = new ArrayList<>();
		Map<String, Concept> index = new HashMap<String, Concept>();
		for (Concept c : concepts) {
			if (!index.containsKey(c.getCui())) {
				index.put(c.getCui(), c);
			} else {
				String ph1 = c.getPhrase();
				String ph2 = index.get(c.getCui()).getPhrase();
				if (ph1.equals(ph2)) {
					continue;
				} else {
					index.get(c.getCui()).setPhrase(ph1 + " + " + ph2);
				}
			}
		}
		result.addAll(index.values());
		return result;
	}

	private List<EligibilityCriteria> filterConcepts(List<EligibilityCriteria> list) {
		List<EligibilityCriteria> filteredList = new ArrayList<>(list);
		for (EligibilityCriteria ec : filteredList) {
			Iterator<Concept> it = ec.getConcepts().iterator();
			while (it.hasNext()) {
				Concept c = it.next();
				if (excluded.contains(c.getCui())) {
					it.remove();
				}
			}
		}
		return filteredList;
	}

	private List<Result> metamapQuery(String text) throws IOException {
		List<Result> resultList = new ArrayList<>();
		try {
			resultList = mmapi.processCitationsFromString(text);
		} catch (Exception e) {
			System.err.println("Restarting servers...");
		}
		return resultList;
	}
}
