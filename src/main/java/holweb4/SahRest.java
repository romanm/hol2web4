package holweb4;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import holweb4.excel.SahExcelService;

@Controller
public class SahRest {
	private static final Logger logger = LoggerFactory.getLogger(SahRest.class);
	@Autowired private JsonToFileService jsonToFileService;
	@Autowired private SahExcelService sahExcelService;

	@RequestMapping(value = "/view/read-excel-zwit", method = RequestMethod.GET)
	public String createReadExcell(Principal userPrincipal) {
		logger.debug("/view/read-excel-zwit");
		logger.debug(AppConfig.leonSevoranShortFileName);
		sahExcelService.copyToWeb(AppConfig.leonSevoranShortFileName);
		return "redirect:/view/excel/"+AppConfig.leonSevoranShortFileName;
	}

	@RequestMapping(value = "/view/readSahYearZwit", method = RequestMethod.GET)
	public  @ResponseBody Map<String, Object> readSahYearZwit() {
		return jsonToFileService.readJsonFromFileName(AppConfig.sahYearZwitJsonFileName);
	}

	@RequestMapping(value = "/saveRegionCommon", method = RequestMethod.POST)
	public  @ResponseBody Map<String, Object> saveSahCommon(@RequestBody Map<String, Object> qaJsonJavaObject, Principal userPrincipal) {
		String name = userPrincipal.getName();
		String rayonKey = name;
		System.out.println(name);
		Map<String, Object> raionCommon_request = (Map<String, Object>) qaJsonJavaObject.get("raionCommon");
		Map<String, Object> raionCommon_requestZwit = (Map<String, Object>) raionCommon_request.get("zwit");
		Map<String, Object> raionCommonZwit_new1 = (Map<String, Object>) raionCommon_requestZwit.get(rayonKey);
		Map<String, Object> raionCommonZwit_new = raionCommonZwit_new1;
		System.out.println(name);
		Map<String, Object> sahYearZwitJsonFile = jsonToFileService.readJsonFromFileName(AppConfig.sahYearZwitJsonFileName);
		Map<String, Object> raionCommon_file = (Map<String, Object>) sahYearZwitJsonFile.get("raionCommon");
		Map<String, Object> raionCommon_fileZwit = (Map<String, Object>) raionCommon_file.get("zwit");
		if(raionCommon_fileZwit == null)
		{
			raionCommon_fileZwit = new HashMap<String, Object>();
			raionCommon_file.put("zwit", raionCommon_fileZwit);
		}
		raionCommon_fileZwit.put(rayonKey, raionCommonZwit_new);
		
		updateSignatures(qaJsonJavaObject, rayonKey, sahYearZwitJsonFile);

		jsonToFileService.saveJsonToFile(sahYearZwitJsonFile,AppConfig.sahYearZwitJsonFileName);
		jsonToFileService.backup(AppConfig.sahYearZwitJsonFileName);
		sahExcelService.saveAthEmployReport(sahYearZwitJsonFile, rayonKey);
		return sahYearZwitJsonFile;
	}

	@RequestMapping(value = "/saveRegion", method = RequestMethod.POST)
	public  @ResponseBody Map<String, Object> saveSah(@RequestBody Map<String, Object> qaJsonJavaObject, Principal userPrincipal) {
		String name = userPrincipal.getName();
		String rayonKey = name;
		System.out.println(name);

		Map<String, Object> rayonZwit_new = (Map<String, Object>) ((Map<String, Object>) qaJsonJavaObject.get("zwit")).get(rayonKey);
		Map<String, Object> sahYearZwitJsonFile = jsonToFileService.readJsonFromFileName(AppConfig.sahYearZwitJsonFileName);
		Map<String, Object> zwit = (Map<String, Object>) sahYearZwitJsonFile.get("zwit");
		zwit.put(rayonKey, rayonZwit_new);
		
		updateSignatures(qaJsonJavaObject, rayonKey, sahYearZwitJsonFile);
		
		jsonToFileService.saveJsonToFile(sahYearZwitJsonFile,AppConfig.sahYearZwitJsonFileName);
		jsonToFileService.backup(AppConfig.sahYearZwitJsonFileName);
		sahExcelService.saveLeonSevoran(sahYearZwitJsonFile,rayonKey);
		return sahYearZwitJsonFile;
	}

	private void updateSignatures(Map<String, Object> qaJsonJavaObject, String rayonKey,
			Map<String, Object> sahYearZwitJsonFile) {
		String regionsKey = "regions";
		String signaturesKey = "signatures";
		Map<String, Object> regionRaion_new = (Map<String, Object>) ((Map<String, Object>) qaJsonJavaObject.get(regionsKey)).get(rayonKey);
		List<Map<String, Object>> signatures = (List<Map<String, Object>>) regionRaion_new.get(signaturesKey);
		if(signatures != null)
		{
			Map<String, Object> regionRaion_file = (Map<String, Object>) ((Map<String, Object>) sahYearZwitJsonFile.get(regionsKey)).get(rayonKey);
			regionRaion_file.put(signaturesKey, signatures);
		}
	}

}
