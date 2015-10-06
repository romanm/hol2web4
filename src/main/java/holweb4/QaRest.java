package holweb4;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class QaRest {
	@Autowired private JsonToFileService jsonToFileService;
	
	@RequestMapping(value = "/read_user", method = RequestMethod.GET)
	public  @ResponseBody Principal getRoleTypes(Principal userPrincipal) {
		return userPrincipal;
	}
	@RequestMapping(value = "/saveQa", method = RequestMethod.POST)
	public  @ResponseBody Map<String, Object> saveQa(@RequestBody Map<String, Object> qaJsonJavaObject) {
		System.out.println(26);
		System.out.println(AppConfig.qaJsonFileName);
		jsonToFileService.saveJsonToFile(qaJsonJavaObject,AppConfig.qaJsonFileName);
		jsonToFileService.backup(AppConfig.qaJsonFileName);
		return qaJsonJavaObject;
	}
	@RequestMapping(value = "/view/backupfile/{fileName}.{d}", method = RequestMethod.GET)
	public  @ResponseBody Map<String, Object> readBackupFile(@PathVariable String fileName, @PathVariable String d) {
		System.out.println(fileName);
		System.out.println(d);
		String fileName2 = fileName+"."+d;
		Map<String, Object> readBackupFile = jsonToFileService.readBackupFile(fileName2);
		return readBackupFile;
	}
	@RequestMapping(value = "/view/readBackupFiles", method = RequestMethod.GET)
	public  @ResponseBody List<Map<String, Object>> readBackupFiles() {
		List<Map<String, Object>> readBackupFiles = jsonToFileService.readBackupFiles("");
		return readBackupFiles;
	}
	@RequestMapping(value = "/view/readQa", method = RequestMethod.GET)
	public  @ResponseBody Map<String, Object> readBugTinyWiki() {
		Map<String, Object> readJsonFromFile = jsonToFileService.readJsonFromFileName(AppConfig.qaJsonFileName);
		return readJsonFromFile;
	}
	@RequestMapping(value = "/view/random_qa", method = RequestMethod.POST)
	public  @ResponseBody Set<Integer> randomQa(@RequestBody Map<String, Object> randomQa) {
		System.out.println(randomQa);
		int qForTest = (int) randomQa.get("qForTest");
//		int maxAsk = 5;
		int qMax = (int) randomQa.get("qMax");
		Set s = new HashSet<Integer>();
		int cnt = 0;
		System.out.println(qForTest+"/"+qMax+"/"+cnt);
		while (s.size()<qForTest) {
			cnt++;
			double random = Math.random();
			int n = (int) (qMax*random);
			System.out.println(s.size()+"/"+cnt+"/"+random+"//"+n);
			if(n==0)
				continue;
			s.add(n);
			if(cnt > 100)
				break;
		}
		System.out.println(s+"/"+qMax+"/"+cnt);
		return s;
	}
}
