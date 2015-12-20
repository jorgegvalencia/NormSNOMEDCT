package db.reports;

import java.util.List;
import java.util.Map;

public abstract class Report {
	private List<? extends Record> records;

	public Report(List<? extends Record> records) {
		this.records = records;
	}

	public void buildReport() {
		records.get(0).printHeaders();
		for (Record record : getRecords()) {
			record.printRecord();
		}
	}

	public List<? extends Record> getRecords() {
		return records;
	}

	public Map<Integer, String> getHeaders() {
		return records.get(0).getHeaders();
	}

	public abstract void buildExcel();
}
