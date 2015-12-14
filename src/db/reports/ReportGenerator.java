package db.reports;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ReportGenerator {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");

		CFReportJDBCTemplate cfrReportJDBCTemplate = (CFReportJDBCTemplate) context.getBean("cfrReportJDBCTemplate");
		ConceptFrecuencyReport cfr = new ConceptFrecuencyReport(cfrReportJDBCTemplate.listConceptFrecuencies());
		cfr.buildReport();

	}

}
