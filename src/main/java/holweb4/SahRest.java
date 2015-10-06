package holweb4;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SahRest {
	@Autowired private JsonToFileService jsonToFileService;
	@RequestMapping(value = "/view/readSahYearZwit", method = RequestMethod.GET)
	public  @ResponseBody Map<String, Object> readSahYearZwit() {
		return jsonToFileService.readJsonFromFileName(AppConfig.sahYearZwitJsonFileName);
	}
}
