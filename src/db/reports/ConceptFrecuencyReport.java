package db.reports;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

	public ConceptFrecuencyReport(List<ConceptFrecuencyRecord> records) {
		super(records);
	}

	@Override
	public void buildExcel() {
		DateFormat dateFormat = new SimpleDateFormat("YY_MMM_dd-HH_mm_ss");
		FileOutputStream out = null;
		try {
			out = new FileOutputStream("results_" + dateFormat.format(new Date()) + ".xlsx");

			XSSFWorkbook wb = new XSSFWorkbook(); // create a new workbook
			XSSFSheet sheet = wb.createSheet(); // create a new sheet
			XSSFRow row = null; // declare a row object reference
			XSSFCell cell = null; // declare a cell object reference

			CreationHelper createHelper = wb.getCreationHelper();
			Font hlink_font = wb.createFont();

			// Styles
			Font f = wb.createFont();
			f.setBold(true);

			XSSFCellStyle head = wb.createCellStyle();
			head.setFillForegroundColor(new XSSFColor(new java.awt.Color(150, 150, 150)));
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
			Map<Integer, String> headers = ConceptFrecuencyRecord.getHeaderFields();
			row = sheet.createRow(0);
			for (int colnum = 0; colnum < headers.size(); colnum++) {
				cell = row.createCell(colnum);
				cell.setCellStyle(head);
				cell.setCellValue(headers.get(colnum));
			}

			// Data
			List<XSSFCell> hyper = new ArrayList<>();
			for (int rownum = 0; rownum < getRecords().size(); rownum++) {
				// Take each record
				Record record = getRecords().get(rownum);
				// Create a row
				row = sheet.createRow(rownum + 1);
				// For each record, get the record fields
				Map<Integer, String> recordFields = record.getRecordFields();
				for (int colnum = 0; colnum < recordFields.size(); colnum++) {
					// Insert all fields in the row
					cell = row.createCell(colnum);
					cell.setCellValue(recordFields.get(colnum));
				}
				// Add the last cell of the row corresponding
				// to the concept to the hyperlink list
				hyper.add(cell);

				////////////////////////////////////////////////////////////////////////

				// Matches sheets
				XSSFSheet matchSheet = wb.createSheet();
				wb.setSheetName((rownum + 1), "" + (rownum + 1));

				// Get the matches report
				MatchReport matchReport = DBManager.getInstance().getMatchReport(record.getRecordFields().get(1)); // SCTID

				// Headers
				row = matchSheet.createRow(0);
				Map<Integer, String> mrheaders = MatchRecord.getHeaderFields();

				// First row of headers: Concept, Return
				cell = row.createCell(0);
				cell.setCellStyle(head);
				cell.setCellValue(record.getRecordFields().get(4)); // FSN

				cell = row.createCell(1);
				cell.setCellValue("Return");
				cell.setCellStyle(hlink_style);
				Hyperlink linkreturn = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
				linkreturn.setAddress("'Main'!A" + (rownum + 1));
				cell.setHyperlink(linkreturn);

				// Second row of headers: Trial, Utterance, Phrase, Synonym,
				// Matched Words
				row = matchSheet.createRow(1);
				for (int colnum = 0; colnum < mrheaders.size(); colnum++) {
					cell = row.createCell(colnum);
					cell.setCellStyle(head);
					cell.setCellValue(mrheaders.get(colnum));
				}

				// Data

				// For each match
				for (int j = 0; j < matchReport.getRecords().size(); j++) {
					// Get the record of the match
					Record matchRecord = matchReport.getRecords().get(j);
					// Create a new row
					row = matchSheet.createRow(j + 2);
					Map<Integer, String> matchRecordFields = matchRecord.getRecordFields();
					for (int field = 0; field < matchRecordFields.size(); field++) {
						cell = row.createCell(field);
						cell.setCellValue(matchRecordFields.get(field + 1));
					}
				}
				Hyperlink link2 = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
				link2.setAddress("'" + (rownum + 1) + "'!A1");
				hyper.get(hyper.size() - 1).setCellStyle(hlink_style);
				hyper.get(hyper.size() - 1).setHyperlink(link2);

				// phraseSheet.autoSizeColumn(0);
				// phraseSheet.autoSizeColumn(1);
				// matchSheet.autoSizeColumn(2);
				matchSheet.setColumnWidth(0, (short) ((250) / ((double) 1 / 20)));
				matchSheet.setColumnWidth(1, (short) ((1000) / ((double) 1 / 20)));
				matchSheet.setColumnWidth(2, (short) ((750) / ((double) 1 / 20)));
				matchSheet.autoSizeColumn(3);
				matchSheet.autoSizeColumn(4);

				////////////////////////////////////////////////////////////////////////
			}
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
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
