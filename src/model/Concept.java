package model;

import java.util.List;

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

	public static class ConceptBuilder {
		private String cui;
		private String sctid;
		private String name;
		private String preferedName;
		private String phrase;
		private List<String> semtypes;
		private String hierarchy;
		private String normalForm;

		public ConceptBuilder(String cui, String sctid, String preferedName, String hierarchy) {
			this.cui = cui;
			this.sctid = sctid;
			this.preferedName = preferedName;
			this.hierarchy = hierarchy;
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

		public void setName(String name) {
			this.name = name;
		}

		public void setPhrase(String phrase) {
			this.phrase = phrase;
		}

		public void setSemtypes(List<String> semtypes) {
			this.semtypes = semtypes;
		}

		public void setNormalForm(String normalForm) {
			this.normalForm = normalForm;
		}

		public Concept build() {
			return new Concept(this);
		}
	}
}
