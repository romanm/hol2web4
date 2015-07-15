package holweb4;

import java.text.SimpleDateFormat;

public class AppConfig {
	public final static SimpleDateFormat yyyyMMddDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public final static SimpleDateFormat ddMMyyyDateFormat = new SimpleDateFormat("dd-MM-yyyy");
	final static String jsonDbFiles	= "src/main/webapp/db/";
	//product
	final static String applicationFolderPfad	= "/home/holweb/server4/hol2web4/";
	final static String urlDb = "jdbc:h2:/home/holweb/server4/h2hol2web4/holweb4";
	//development
//	final static String applicationFolderPfad = "/home/roman/algoritmed.com/development/holweb4/";
//	final static String urlDb = "jdbc:h2:/home/roman/algoritmed.com/h2-files/holweb4";
//	final static String urlDb = "jdbc:h2:tcp://localhost/holweb4/holweb4";

	//all
	final static String applicationResourcesFolderPfad	= applicationFolderPfad+"src/main/resources/";
}
