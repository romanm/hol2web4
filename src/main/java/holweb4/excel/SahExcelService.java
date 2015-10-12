package holweb4.excel;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import holweb4.AppConfig;

@Component("sahExcelService")
public class SahExcelService extends ExcelBasic{
	private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);

	
	public void saveLeonSevoran(Map<String, Object> sahYearZwitJsonFile, String rayonKey) {
		HSSFWorkbook leonSevoranExcel = readLeonSevoran();
		logger.debug("Hello world! "+leonSevoranExcel);
		HSSFSheet leonSevoranSheet = leonSevoranExcel.getSheet("ЛЕОН.севоран");
		
		Map<String, Object> rayonZwit = (Map<String, Object>) ((Map<String, Object>) sahYearZwitJsonFile.get("zwit")).get(rayonKey);
		Map<String, Object> regions = (Map<String, Object>) sahYearZwitJsonFile.get("regions");
		Map<String, Object> rayonInfo = (Map<String, Object>) regions.get(rayonKey);
		List<String> regionOrder = (List<String>) regions.get("order");
		int rayonPosition = regionOrder.indexOf(rayonKey);
		Integer row1Number = (Integer) regions.get("row1Number");
		Integer rayonRowIndex = row1Number - 1 + rayonPosition;
		
		Map<String, Object> parameters = (Map<String, Object>) sahYearZwitJsonFile.get("parameters");
		Integer col1Number = (Integer) parameters.get("col1Number");
		logger.debug("/"+col1Number);
		List<String> parameterOrder = (List<String>) parameters.get("order");
		for (String paramKey : parameterOrder) {
			int regionPosition = parameterOrder.indexOf(paramKey);
			Integer param = (Integer) rayonZwit.get(paramKey);
			if(param != null){
				Integer paramColIndex = col1Number - 1 + regionPosition;
				logger.debug(paramKey+"/"+regionPosition+"/"+param+"/"+paramColIndex);
				setRCIntegerValue(leonSevoranSheet,rayonRowIndex,paramColIndex,param);
			}
		}
		logger.debug(AppConfig.leonSevoranFileName);
		saveExcelFullName(leonSevoranExcel,AppConfig.leonSevoranFileName);
	}

	private HSSFWorkbook readLeonSevoran() {
		HSSFWorkbook leonSevoranExcel = readExcelFullName(AppConfig.leonSevoranFileName);
		return leonSevoranExcel;
	}

}
