package db.reports;

import java.util.List;

public abstract class Report {
	private List<? extends Record> records;

	public Report(List<? extends Record> records) {
		this.records = records;
	}

	public void buildReport() {
		records.get(0).printHeaders();
		for (Record record : getRecords())
			record.printRecord();
	}

	public List<? extends Record> getRecords() {
		return records;
	}

	public abstract void buildExcel();
}
