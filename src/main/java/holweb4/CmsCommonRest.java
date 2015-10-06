package holweb4;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CmsCommonRest {

	@Autowired private JsonToFileService jsonToFileService;

	@RequestMapping(value = "/view/readContent", method = RequestMethod.GET)
	public  @ResponseBody Map<String, Object> readBugTinyWiki() {
		Map<String, Object> readJsonFromFile = jsonToFileService.readJsonFromFileName(AppConfig.commonContentFileName);
		return readJsonFromFile;
	}

	@RequestMapping(value = "/saveCommonContent", method = RequestMethod.POST)
	public  @ResponseBody Map<String, Object> saveBugTinyWiki(@RequestBody Map<String, Object> commonContentJavaObject) {
		jsonToFileService.saveJsonToFile(commonContentJavaObject,AppConfig.commonContentFileName);
		jsonToFileService.backup(AppConfig.commonContentFileName);
		return commonContentJavaObject;
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view() { return "redirect:/view/v.html?page1"; }

}
