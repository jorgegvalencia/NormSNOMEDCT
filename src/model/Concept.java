package model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import db.DBManager;

public class Concept {
	private static final Pattern pattern = Pattern.compile("\\([a-z\\s/]+\\)\\z");
	private String cui;
	private String sctid;
	private String fsn;
	private String hierarchy;

	private DBManager db;

	public Concept(String cui) throws InstantiationException {
		this.cui = cui;
		try {
			getSCTid();
			getFullySpecifiedName();
			getSnomedHierarchy();
		} catch (InstantiationException e) {
			throw new InstantiationException();
		}
	}

	public Concept(String cui, String sctid, String fsn, String hierarchy) {
		this.cui = cui;
		this.sctid = sctid;
		this.fsn = fsn;
		this.hierarchy = hierarchy;
	}

	public String getCui() {
		return cui;
	}

	public String getSctid() {
		return sctid;
	}

	public String getFsn() {
		return fsn;
	}

	public String getHierarchy() {
		return hierarchy;
	}

	private void getSCTid() throws InstantiationException {
		if (db == null)
			db = DBManager.getInstance();
		List<String> candidates = db.getSCTID(cui);
		if (candidates.size() < 1)
			throw new InstantiationException("Could not resolve the SnomedCT identifier for the concept " + cui);
		else {
			int status = 0;
			for (String candidate : candidates) {
				status = db.getStatusFromDB(candidate);
				if (status == 1)
					sctid = candidate;
			}
		}

	}

	private void getFullySpecifiedName() throws InstantiationException {
		if (db == null)
			db = DBManager.getInstance();
		List<String> fsn = db.getFSN(sctid);
		if (fsn == null || fsn.size() < 1)
			throw new InstantiationException("Could not resolve the Fully Specified Name for the concept " + cui);
		else
			this.fsn = fsn.get(0);
	}

	private void getSnomedHierarchy() {
		Matcher m = pattern.matcher(fsn);
		if (m.find())
			hierarchy = m.group(0).replaceAll("\\p{Punct}", "");
		else
			hierarchy = "N/A";
	}
}
