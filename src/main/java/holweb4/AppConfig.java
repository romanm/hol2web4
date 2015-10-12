package holweb4;

import java.text.SimpleDateFormat;

public class AppConfig {
	public final static SimpleDateFormat yyyyMMddDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public final static SimpleDateFormat ddMMyyyDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	public final static SimpleDateFormat yyyyMMddHHmmssDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	final static String commonContentFileName = "commonContent.json";
	final static String qaJsonFileName = "qa.json";
	final static String jsonDbFiles	= "src/main/webapp/db/";
	//product
//	final static String applicationFolderPfad	= "/home/holweb/server4/hol2web4/";
//	public static String applicationDbFolderPfad	= "/home/holweb/server4/db/h2hol2web4/";
//	static String applicationDbBackupFolderPfad = "/home/holweb/server4/db/h2hol2web4-backup/";
//	final static String urlDb = "jdbc:h2:"+ applicationDbFolderPfad + "holweb4";
	//development
	final static String applicationFolderPfad = "/home/roman/algoritmed.com/development/hol2web4/";
	public final static String applicationDbFolderPfad	= "/home/roman/algoritmed.com/development/db-hol2web4/";
	static String applicationDbBackupFolderPfad = "/home/roman/algoritmed.com/development/hol2web4-backup/";
	final static String urlDb = "jdbc:h2:tcp://localhost/holweb4/holweb4";
//	public final static String applicationExcelFolderPfad	= applicationFolderPfad+"src/main/webapp/excel/";

//	final static String urlDb = "jdbc:h2:/home/roman/algoritmed.com/h2-files/holweb4/holweb4";

	//all
	final static String applicationResourcesFolderPfad	= applicationFolderPfad+"src/main/resources/";
	public final static String innerExcelFolderPfad	= applicationFolderPfad + "src/main/webapp/view/excel/";
//	public final static String innerExcelFolderPfad	= applicationFolderPfad + "src/main/webapp/excel/";
	final static String sahYearZwitJsonFileName = "sah_year_zwit.json";
	final static String sahUsersJsonFileName = "sah_users.json";
	public static String leonSevoranShortFileName = "leon-sevoran.xls";
	public static String leonSevoranFileName = AppConfig.applicationDbFolderPfad+leonSevoranShortFileName;
}
