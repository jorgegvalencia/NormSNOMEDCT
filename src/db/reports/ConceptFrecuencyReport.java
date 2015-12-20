package db.reports;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import db.DBManager;

public class ConceptFrecuencyReport extends Report {
	private MatchReport mr;

	public ConceptFrecuencyReport(List<ConceptFrecuencyRecord> records) {
		super(records);
	}

	@Override
	public void buildExcel() {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream("results.xlsx");

			XSSFWorkbook wb = new XSSFWorkbook(); // create a new workbook
			XSSFSheet sheet = wb.createSheet(); // create a new sheet
			XSSFRow row = null; // declare a row object reference
			XSSFCell cell = null; // declare a cell object reference

			CreationHelper createHelper = wb.getCreationHelper();
			Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
			Font hlink_font = wb.createFont();

			// Styles
			Font f = wb.createFont();
			f.setBold(true);

			XSSFCellStyle head = wb.createCellStyle();
			head.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 192, 0)));
			head.setFillPattern(CellStyle.SOLID_FOREGROUND);
			head.setFont(f);

			XSSFCellStyle hlink_style = wb.createCellStyle();
			hlink_font.setUnderline(Font.U_SINGLE);
			hlink_font.setColor(IndexedColors.BLUE.getIndex());
			hlink_style.setFont(hlink_font);

			////////////////////////////////////////////////////////////////////////////

			// Main sheet
			wb.setSheetName(0, "Main");

			// Headers
			Map<Integer, String> headers = getHeaders();
			row = sheet.createRow(0);
			for (int i = 1; i < headers.size(); i++) {
				cell.setCellStyle(head);
				cell = row.createCell(i - 1);
				cell.setCellValue(headers.get(i));
			}

			// Data
			for (int i = 0; i < getRecords().size(); i++) {
				Record cfr = getRecords().get(i);
				row = sheet.createRow(i);

				Map<Integer, String> record = cfr.getRecord();
				for (int j = 1; j < record.size(); j++) {
					row = sheet.createRow(j);
					cell = row.createCell(j);
					cell.setCellValue(record.get(j));
				}

				XSSFSheet phraseSheet = wb.createSheet();

				////////////////////////////////////////////////////////////////////////

				// Matches sheets

				mr = DBManager.getInstance().getMatchReport(record.get(3));

				// Headers
				row = phraseSheet.createRow(0);
				Map<Integer, String> mrheaders = mr.getHeaders();

				cell = row.createCell(0);
				cell.setCellStyle(head);
				cell.setCellValue(cfr.getRecord().get("CONCEPT"));

				cell = row.createCell(1);
				cell.setCellValue("Return");
				cell.setCellStyle(hlink_style);
				cell.setHyperlink(link);
				link.setAddress("'Main'!A1");

				row = phraseSheet.createRow(1);

				for (int j = 1; j < mrheaders.size(); j++) {
					cell.setCellStyle(head);
					cell = row.createCell(j - 1);
					cell.setCellValue(mrheaders.get(i));
				}

				// Data
				row = phraseSheet.createRow(2);
				for (int j = 0; j < mr.getRecords().size(); j++) {
					Record mrr = mr.getRecords().get(j);
					cell = row.createCell(j);

					Map<Integer, String> matchRecord = mrr.getRecord();
					cell.setCellValue(matchRecord.get(j));
				}
				////////////////////////////////////////////////////////////////////////
			}
			////////////////////////////////////////////////////////////////////////////

			// Free resources
			wb.write(out);
			wb.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
