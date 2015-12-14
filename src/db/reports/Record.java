package db.reports;

public interface Record {

	public void printHeaders();

	public void printRecord();

	public String getHeaders();

	public String getRecord();
}
