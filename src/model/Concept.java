package model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Concept {
	private static final Pattern pattern = Pattern.compile("\\([a-z\\s/]+\\)\\z");
	private String cui;
	private String sctid;
	private String fsn;
	private String hierarchy;

	public Concept(String cui) {
		this.cui = cui;
		getSCTid();
		getFullySpecifiedName();
		getSnomedHierarchy();
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

	private void getSCTid() {

	}

	private void getFullySpecifiedName() {

	}

	private void getSnomedHierarchy() {
		Matcher m = pattern.matcher(fsn);
		if (m.find())
			hierarchy = m.group(0).replaceAll("\\p{Punct}", "");
		else
			hierarchy = "N/A";
	}
}
