package holweb4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("excelService")
public class ExcelService {
	private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);

	HSSFCellStyle dateCellStyle = null;
	CreationHelper createHelper = null;
	String monthTemplateName = "month_template";
	HSSFSheet monthTemplateSheet;
	
	public void createExcel(List<Map<String, Object>> moveTodayPatientsList, DateTime dateTime) {
//		testExcel(moveTodayPatientsList);
		HSSFWorkbook pyx2015 = readExcel();
		createHelper = pyx2015.getCreationHelper();
		dateCellStyle = pyx2015.createCellStyle();
		dateCellStyle.setDataFormat(
				createHelper.createDataFormat().getFormat("dd.mm.yyyy"));

		logger.debug(""+pyx2015.getNumberOfSheets());
		logger.debug(""+pyx2015.getSheet(monthTemplateName).getSheetName());
		buildMonthSheet(pyx2015, dateTime, moveTodayPatientsList);
		monthTemplateSheet = pyx2015.getSheet(monthTemplateName);
		Integer rowNr = findFirstDepartmentRow(monthTemplateSheet);
//		rowNr = setPatientMovesDate(moveTodayPatientsList, monthTemplateSheet, rowNr);
		saveExcel(pyx2015, AppConfig.applicationExcelFolderPfad+"pyx2015.xls");

		try {
			Files.copy(new File(AppConfig.applicationExcelFolderPfad+"pyx2015.xls").toPath()
			, new File(AppConfig.innerExcelFolderPfad+"pyx2015.xls").toPath()
			, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}

		findDate(monthTemplateSheet);
	}
	private void findDate(HSSFSheet sheet1) {
		for (Row row : sheet1) {
			Cell cell = row.getCell(0);
			if(cell != null)
			{
				if(cell.getCellType() != Cell.CELL_TYPE_STRING){
					if(HSSFDateUtil.isCellDateFormatted(cell)){
						logger.debug(row.getRowNum()+"/"+HSSFDateUtil.isCellDateFormatted(cell));
					}
				}
			}
		}
	}
	private void saveExcel(Workbook workbook, String fileName) {
		// Create a FileOutputStream by passing the excel file name.
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(fileName);
			// Write the FileOutputStream to workbook object.
			workbook.write(outputStream);
			// Finally close the FileOutputStream.
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private Integer findFirstDepartmentRow(HSSFSheet sheet1) {
		for (Row row : sheet1) {
			Cell cell = row.getCell(0);
			if(cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING){
				String stringCellValue = cell.getStringCellValue();
				if("Хірургія".equals(stringCellValue)){
					int rowNum = row.getRowNum();
					return rowNum;
				}
			}
		}
		return null;
	}
	private HSSFSheet buildMonthSheet(HSSFWorkbook pyx2015, DateTime dateTime, List<Map<String, Object>> moveTodayPatientsList) {
		int monthOfYear = dateTime.getMonthOfYear();
		HSSFSheet monthSheet = null;
		int monthSheetNr = 0;
		int indexOfScheet = 0;
		//find monthScheet
		for (; indexOfScheet < pyx2015.getNumberOfSheets(); indexOfScheet++) {
			if(pyx2015.getSheetName(indexOfScheet).matches("\\d+"))
			{
				monthSheetNr = Integer.parseInt(pyx2015.getSheetName(indexOfScheet));
				if(monthSheetNr == monthOfYear)
				{
					monthSheet = pyx2015.getSheetAt(indexOfScheet);
					pyx2015.setActiveSheet(indexOfScheet);
					break;
				}
			}
		}
		//create new monthSheet
		if(monthSheetNr != monthOfYear)
		{
			monthSheet = pyx2015.createSheet((monthOfYear<10?"0":"")+monthOfYear);
			int numberOfSheets = pyx2015.getNumberOfSheets();
			pyx2015.setSheetOrder(monthTemplateName, numberOfSheets - 1);
			pyx2015.setActiveSheet(numberOfSheets - 2);
		}
		//init month in new sheet
		int lastRowNum = monthSheet.getLastRowNum();
		if(lastRowNum == 0)
		{
			DateTime minDate = dateTime.dayOfMonth().withMinimumValue();
			DateTime maxDate = dateTime.dayOfMonth().withMaximumValue();

			int row = 1;

			while (minDate.monthOfYear().get() == maxDate.monthOfYear().get()) {
				HSSFCell createCell = monthSheet.createRow(row++).createCell(0);
				createCell.setCellStyle(dateCellStyle);
				createCell.setCellFormula("DATE("
						+ minDate.getYear()
						+ ","
						+ minDate.getMonthOfYear()
						+ ","
						+ minDate.getDayOfMonth()
						+ ")");
				minDate = minDate.plusDays(1);
				row++;
			}
		}
		//active day

		logger.debug(""+dateTime);
		HSSFCell dateCell = getDateCell(dateTime.dayOfMonth().get(), monthSheet);
		logger.debug(""+dateCell.getRowIndex());
		dateCell.setAsActiveCell();
		HSSFCell nextDateCell = getDateCell(dateTime.dayOfMonth().get()+1, monthSheet);
		if(nextDateCell.getRowIndex()-dateCell.getRowIndex() < 31)
		{
			monthSheet.shiftRows(dateCell.getRowIndex() + 1, dateCell.getSheet().getLastRowNum(), 31);
		}
		HSSFCell monthTemplateDateCell = pyx2015.getSheet(monthTemplateName).getRow(1).getCell(0);
		copyWithStyle(getCell(monthTemplateDateCell, 1, 0), initCell(dateCell, 1, 0));
		int rowShift1Department = 3;
		int departmentCount = 22;
		for (int i = 0; i < departmentCount+1; i++) {
			//department names
			copyWithStyle(getCell(monthTemplateDateCell, rowShift1Department + i, 0), initCell(dateCell, rowShift1Department + i, 0));
		}
		int addColShift = 1;
		int rowSkipSumme = departmentCount+3;
		for (int i = 0; i < 18; i++) {
//			copyValue(getCell(monthTemplateDateCell, 1, addColShift + i), initCell(dateCell, 1, addColShift + i));
//			//head
			copyWithStyle(getCell(monthTemplateDateCell, 1, addColShift + i), initCell(dateCell, 1, addColShift + i));
			//summe formula
			copyWithStyle(getCell(monthTemplateDateCell, rowSkipSumme, addColShift + i), initCell(dateCell, rowSkipSumme, addColShift + i));
			addDaySum(dateCell, departmentCount, rowSkipSumme, addColShift + i);
		}
		HSSFCell day1SumCell = initCell(dateCell, rowSkipSumme, addColShift);
		String cellFormula = day1SumCell.getCellFormula();
		logger.debug(cellFormula);
		CellRangeAddress cellRangeAddress = new CellRangeAddress(day1SumCell.getRowIndex()-4, day1SumCell.getRowIndex()-3
				, day1SumCell.getColumnIndex(), day1SumCell.getColumnIndex());
		logger.debug(cellRangeAddress.formatAsString());
		day1SumCell.setCellFormula(cellFormula+" - SUM("
				+ cellRangeAddress.formatAsString()
				+ ")");

		dateCell.getSheet().autoSizeColumn(dateCell.getColumnIndex());
		dateCell.getSheet().getRow(dateCell.getRowIndex()+1).setHeight((short)1400);
		setPatientMovesDate(moveTodayPatientsList, dateCell.getSheet(), dateCell.getRowIndex() + rowShift1Department);
		return monthSheet;
	}
	private void addDaySum(HSSFCell dateCell, int departmentCount, int rowSkipSumme, int columnSkip) {
		HSSFCell day1SumCell = initCell(dateCell, rowSkipSumme, columnSkip);
		CellRangeAddress cellRangeAddress = new CellRangeAddress(day1SumCell.getRowIndex()-departmentCount, day1SumCell.getRowIndex()-1
				, day1SumCell.getColumnIndex(), day1SumCell.getColumnIndex());
		logger.debug(cellRangeAddress.formatAsString());
		day1SumCell.setCellFormula("SUM("
				+ cellRangeAddress.formatAsString()
				+ ")");
	}
	private void copyValue(HSSFCell sourceCell, HSSFCell destinationCell) {
		switch(sourceCell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			destinationCell.setCellValue(sourceCell.getStringCellValue());
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			destinationCell.setCellFormula(sourceCell.getCellFormula());
			break;
		}
	}
	private void copyWithStyle(HSSFCell sourceCell, HSSFCell destinationCell) {
		copyValue(sourceCell, destinationCell);
		destinationCell.setCellStyle(sourceCell.getCellStyle());
	}
	private HSSFCell initCell(HSSFCell startCell, int rowSkip, int columnSkip) {
		HSSFRow row = startCell.getSheet().getRow(startCell.getRowIndex() + rowSkip);
		if(row == null)
			row = startCell.getSheet().createRow(startCell.getRowIndex() + rowSkip);
		HSSFCell destinationCell = row.getCell(startCell.getColumnIndex() + columnSkip);
		if(destinationCell == null)
			destinationCell = row.createCell(startCell.getColumnIndex() + columnSkip);
		return destinationCell;
	}
	private HSSFCell getCell(HSSFCell startCell, int rowSkip, int columnSkip) {
		HSSFRow row = startCell.getSheet().getRow(startCell.getRowIndex() + rowSkip);
		HSSFCell destinationCell = row.getCell(startCell.getColumnIndex() + columnSkip);
		return destinationCell;
	}
	private Integer setPatientMovesDate(List<Map<String, Object>> moveTodayPatientsList, HSSFSheet sheet, Integer rowNr) {
		for (Map<String, Object> map : moveTodayPatientsList) {
			setRCIntegerValue(sheet,rowNr,1,parseInt(map, "DEPARTMENT_BED"));
			setRCIntegerValue(sheet,rowNr,2,parseInt(map, "MOVEDEPARTMENTPATIENT_PATIENT1DAY"));
			setRCIntegerValue(sheet,rowNr,3,parseInt(map, "MOVEDEPARTMENTPATIENT_IN"));
			setRCIntegerValue(sheet,rowNr,4,parseInt(map, "MOVEDEPARTMENTPATIENT_INDEPARTMENT"));
			setRCIntegerValue(sheet,rowNr,6,parseInt(map, "MOVEDEPARTMENTPATIENT_OUTDEPARTMENT"));
			setRCIntegerValue(sheet,rowNr,8,parseInt(map, "MOVEDEPARTMENTPATIENT_OUT"));
			setRCIntegerValue(sheet,rowNr,9,parseInt(map, "MOVEDEPARTMENTPATIENT_DEAD"));
			setRCIntegerValue(sheet,rowNr,10,parseInt(map, "MOVEDEPARTMENTPATIENT_PATIENT2DAY"));
			setRCIntegerValue(sheet,rowNr,14,parseInt(map, "MOVEDEPARTMENTPATIENT_SITY"));
			setRCIntegerValue(sheet,rowNr,15,parseInt(map, "MOVEDEPARTMENTPATIENT_LYING"));
			setRCIntegerValue(sheet,rowNr,16,parseInt(map, "MOVEDEPARTMENTPATIENT_CHILD"));
			setRCIntegerValue(sheet,rowNr,17,parseInt(map, "MOVEDEPARTMENTPATIENT_INSURED"));
			setRCIntegerValue(sheet,rowNr,18,parseInt(map, "MOVEDEPARTMENTPATIENT_CAES"));
			rowNr++;
		}
		return rowNr;
	}
	private void setRCIntegerValue(HSSFSheet sheet1, Integer rownum, int cellnum, Integer value) {
		if(value != null){
			HSSFRow row = sheet1.getRow(rownum);
			if(row == null)
				row = sheet1.createRow(rownum);
			HSSFCell cell = row.getCell(cellnum);
			if(cell == null)
				cell = row.createCell(cellnum);
			cell.setCellValue(value);
		}
	}
	private Integer parseInt(Map<String, Object> map, String key) {
		Integer value = null;
		Object valueOnject = map.get(key);
		if(valueOnject instanceof Integer)
			value = (Integer) valueOnject;
		else
			if(valueOnject != null)
				value = Integer.parseInt(""+valueOnject);
		return value;
	}
	private HSSFCell getDateCell(int dayToSeek, HSSFSheet monthSheet) {
		for (int i = monthSheet.getFirstRowNum(); i < monthSheet.getLastRowNum(); i++) {
			HSSFRow row = monthSheet.getRow(i);
			if(row == null)
				continue;
			HSSFCell cell = row.getCell(0);
			if(cell == null) continue;
			if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING) continue;
			if(HSSFDateUtil.isCellDateFormatted(cell)){
				String cellFormula = cell.getCellFormula();
				String dayStr = cellFormula.split(",")[2].replace(")", "");
				int day = Integer.parseInt(dayStr);
				if(day == dayToSeek){
					return cell;
				}
			}
		}
		return null;
	}
	private HSSFWorkbook readExcel() {
		try {
			InputStream inputStream = new FileInputStream(
					AppConfig.applicationExcelFolderPfad+"pyx2015.xls");
			return new HSSFWorkbook(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
