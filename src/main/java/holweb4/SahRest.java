package holweb4;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import holweb4.excel.SahExcelService;

@Controller
public class SahRest {
	@Autowired private JsonToFileService jsonToFileService;
	@Autowired private SahExcelService sahExcelService;

	@RequestMapping(value = "/view/read-excel-zait", method = RequestMethod.GET)
	public String createReadExcell(Principal userPrincipal) {
		sahExcelService.copyToWeb(AppConfig.leonSevoranShortFileName);
		return "redirect: excel/"+AppConfig.leonSevoranShortFileName;
	}

	
	@RequestMapping(value = "/view/readSahYearZwit", method = RequestMethod.GET)
	public  @ResponseBody Map<String, Object> readSahYearZwit() {
		return jsonToFileService.readJsonFromFileName(AppConfig.sahYearZwitJsonFileName);
	}

	@RequestMapping(value = "/saveRegion", method = RequestMethod.POST)
	public  @ResponseBody Map<String, Object> saveSah(@RequestBody Map<String, Object> qaJsonJavaObject, Principal userPrincipal) {
		String name = userPrincipal.getName();
		String key = name;
		System.out.println(name);
		Map<String, Object> rayonZwit_new = (Map<String, Object>) ((Map<String, Object>) qaJsonJavaObject.get("zwit")).get(key);
		Map<String, Object> sahYearZwitJsonFile = jsonToFileService.readJsonFromFileName(AppConfig.sahYearZwitJsonFileName);
		Map<String, Object> zwit = (Map<String, Object>) sahYearZwitJsonFile.get("zwit");
		zwit.put(key, rayonZwit_new);
		jsonToFileService.saveJsonToFile(sahYearZwitJsonFile,AppConfig.sahYearZwitJsonFileName);
		jsonToFileService.backup(AppConfig.sahYearZwitJsonFileName);
		sahExcelService.saveLeonSevoran(sahYearZwitJsonFile,key);
		return qaJsonJavaObject;
	}

}
