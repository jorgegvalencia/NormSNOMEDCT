package nlp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import db.DBDriver;
import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Result;
import gov.nih.nlm.nls.metamap.Utterance;
import model.Concept;
import model.ConceptFactory;
import model.EligibilityCriteria;

public class ConceptExtractor {
	// private static HashSet<String> excluded = new HashSet<String>(); // CUI
	private static MetaMapApi mmapi;
	private static String options = "-y -Q 2 -i -k cell,fish,ftcn,idcn,inpr,menp,mnob,podg,qlco,qnco,spco,tmco -R SNOMEDCT_US";

	public ConceptExtractor() {
		mmapi = new MetaMapApiImpl();
		mmapi.setOptions(options);
	}

	public ConceptExtractor(String host) {
		mmapi = new MetaMapApiImpl();
		mmapi.setHost(host);
		mmapi.setOptions(options);
	}

	public List<ProcessingUnit> process(List<ProcessingUnit> pulist, boolean save) {
		// List<ProcessingUnit> resultlist = new ArrayList<ProcessingUnit>();
		for (ProcessingUnit pu : pulist) {
			List<Match> matches;
			long startTime = System.nanoTime();
			for (EligibilityCriteria ec : pu.getBuilder().getCriteriaSet().getEclist()) {
				matches = getMatches(ec.getUtterance());
				ec.setMatches(matches);
			}
			long endTime = System.nanoTime();
			double time = Double.parseDouble(String.format("%.2f", (endTime - startTime) / Math.pow(10, 9)));
			if (pu.isProcessed()) {
				pu.setTimeAndBuild(time);
			}
			if (save) {
				DBDriver.getInstance().saveProcessingUnit(pu);
			}
		}
		return pulist;
	}

	private List<Match> getMatches(String utterance) {
		List<Match> matches = new ArrayList<Match>();
		try {
			List<Result> result = metamapQuery(utterance);
			for (Result res : result) {
				for (Utterance uttr : res.getUtteranceList()) {
					for (PCM pcm : uttr.getPCMList()) {
						for (Mapping map : pcm.getMappingList()) {
							for (Ev mapEv : map.getEvList()) {
								Concept c = ConceptFactory.getConcept(mapEv.getConceptId());
								if (c == null) {
									continue;
								}
								Match match = new Match(c, pcm.getPhrase().getPhraseText(), mapEv.getConceptName(),
										mapEv.getPreferredName(), mapEv.getMatchedWords());
								matches.add(match);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matches;
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
