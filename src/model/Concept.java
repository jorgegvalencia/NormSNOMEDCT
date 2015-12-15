package model;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Concept {
	private String cui;
	private String sctid;
	private String name;
	private String preferedName;
	private String phrase;
	private List<String> semtypes;
	private String hierarchy;
	private String normalForm;

	private Concept(ConceptBuilder builder) {
		cui = builder.cui;
		sctid = builder.sctid;
		preferedName = builder.preferedName;
		phrase = builder.phrase;
		hierarchy = builder.hierarchy;
	}

	public void print() {
		System.out.format("%8s|%9s|%-60s\t%s\n", cui, sctid, preferedName, phrase);
	}

	public String getCui() {
		return cui;
	}

	public String getSctid() {
		return sctid;
	}

	public String getName() {
		return name;
	}

	public String getPreferedName() {
		return preferedName;
	}

	public String getPhrase() {
		return phrase;
	}

	public List<String> getSemtypes() {
		return semtypes;
	}

	public String getHierarchy() {
		return hierarchy;
	}

	public String getNormalForm() {
		return normalForm;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public void setNormalForm(String normalForm) {
		this.normalForm = normalForm;
	}

	public static class ConceptBuilder {
		private static final Pattern pattern = Pattern.compile("\\([a-z\\s/]+\\)\\z");
		private String cui;
		private String sctid;
		private String name;
		private String preferedName;
		private String phrase;
		private List<String> semtypes;
		private String hierarchy;
		private String normalForm;

		public ConceptBuilder(String cui, String sctid, String preferedName) {
			this.cui = cui;
			this.sctid = sctid;
			this.preferedName = preferedName;
			Matcher m = pattern.matcher(preferedName);
			if (m.find()) {
				hierarchy = m.group(0).replaceAll("\\p{Punct}", "");
			} else {
				hierarchy = "N/A";
			}
		}

		public ConceptBuilder setName(String name) {
			this.name = name;
			return this;
		}

		public ConceptBuilder setPhrase(String phrase) {
			this.phrase = phrase;
			return this;
		}

		public ConceptBuilder setSemtypes(List<String> semtypes) {
			this.semtypes = semtypes;
			return this;
		}

		public ConceptBuilder setNormalForm(String normalForm) {
			this.normalForm = normalForm;
			return this;
		}

		public ConceptBuilder setHierarchy(String hierarchy) {
			this.hierarchy = hierarchy;
			return this;
		}

		public Concept build() {
			return new Concept(this);
		}
	}
}
