package holweb4.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import holweb4.AppConfig;

public class ExcelBasic {
	protected Cell setRCIntegerValue(Sheet sheet, Integer rowNum, int cellNum, Integer value) {
//		if(value != null){
		if(true){
			Row row = sheet.getRow(rowNum);
			if(row == null)
				row = sheet.createRow(rowNum);
			Cell cell = row.getCell(cellNum);
			
			if(cell == null)
				cell = row.createCell(cellNum);
			if(cell.getCellType() == Cell.CELL_TYPE_FORMULA){
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			}
			if(value == null){
				cell.setCellValue((String)null);
			}else{
				cell.setCellValue(value);
			}
			return cell;
		}
		return null;
	}
	protected void saveExcelFullName(Workbook workbook, String fileName) {
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
	protected HSSFWorkbook readExcelFullName(String fullFileName) {
		try {
			InputStream inputStream = new FileInputStream(fullFileName);
			return new HSSFWorkbook(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	protected HSSFWorkbook readExcel(String fileName) {
		return readExcelFullName(AppConfig.applicationDbFolderPfad+fileName);
	}
	public void copyToWeb(String shortFuleName) {
		System.out.println("cp "+AppConfig.applicationDbFolderPfad+shortFuleName
				+" "+AppConfig.innerExcelFolderPfad+shortFuleName);
		try {
			Files.copy(new File(AppConfig.applicationDbFolderPfad+shortFuleName).toPath()
			, new File(AppConfig.innerExcelFolderPfad+shortFuleName).toPath()
			, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("OK");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
