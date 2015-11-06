package holweb4;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Component("jsonToFileService")
public class JsonToFileService {

	ObjectMapper mapper = new ObjectMapper();

	public void saveJsonToFile(Map<String, Object> javaObjectToJson, String fileName) {
		File file = new File(AppConfig.applicationDbFolderPfad + fileName);
		System.out.println(file);
		ObjectWriter writerWithDefaultPrettyPrinter = mapper.writerWithDefaultPrettyPrinter();
		System.out.println(23);
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			writerWithDefaultPrettyPrinter.writeValue(fileOutputStream, javaObjectToJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Map<String, Object> readBackupFile(String fileName) {
		String fullFileName = AppConfig.applicationDbBackupFolderPfad+fileName;
//		String fullFileName = AppConfig.applicationDbFolderPfad+"backup/"+fileName;
		File file = new File(fullFileName);
		Map<String, Object> readJsonFromFullFileName = readJsonFromFullFileName(file);
		return readJsonFromFullFileName;
	}
	public List<Map<String, Object>> readBackupFiles(String fileName) {
		List<Map<String, Object>> backulFileList = new ArrayList<Map<String,Object>>();
		String backupDir = AppConfig.applicationDbBackupFolderPfad;
		File file = new File(backupDir);
		if(file.isDirectory())
			for (String string : file.list()) {
				Map<String, Object> fileAttribute = new HashMap<>();
				File file2 = new File(backupDir+string);
				DateTime dateTime = new DateTime(file2.lastModified());
				long length = file2.length();
				fileAttribute.put("name", file2.getName());
				fileAttribute.put("lastModified", dateTime.toDate());
				fileAttribute.put("length", length);
//				fileAttribute.put("file", file2);
				backulFileList.add(fileAttribute);
			}
		return backulFileList;
	}
	public Map<String, Object> readJsonFromFileName(String fileName) {
		File file = new File(AppConfig.applicationDbFolderPfad + fileName);
		return readJsonFromFullFileName(file);
	}
	private Map<String, Object> readJsonFromFullFileName(File file) {
		Map<String, Object> readJsonFileToJavaObject = null;
		try {
			readJsonFileToJavaObject = mapper.readValue(file, Map.class);
		} catch (JsonParseException e1) {
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return readJsonFileToJavaObject;
	}

	public void backup(String fileName) {
		DateTime today = new DateTime();
		String timestampStr = AppConfig.yyyyMMddHHmmssDateFormat.format(today.toDate());
		try {
			Files.copy(new File(AppConfig.applicationDbFolderPfad + fileName).toPath()
			, new File(AppConfig.applicationDbBackupFolderPfad + fileName +"."+ timestampStr).toPath()
			, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
