package db.reports;

import java.util.Map;

public interface Record {

	public void printHeaders();

	public void printRecord();

	public Map<Integer, String> getRecordFields();
}
