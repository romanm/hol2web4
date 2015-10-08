package holweb4;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import holweb4.excel.ExcelService;

@Controller
public class AppRest {
	private static final Logger logger = LoggerFactory.getLogger(AppRest.class);
	@Autowired private AppService appService;
	// 1.2   Запис надходження/виписки хворих на сьогодні – saveMoveTodayPatients
	@RequestMapping(value = "/saveMoveTodayPatients", method = RequestMethod.POST)
	public  @ResponseBody Map<String, Object> saveMoveTodayPatients(@RequestBody Map<String, Object> moveTodayPatients) {
		logger.debug("------------saveMoveTodayPatients-------------------");
		logger.info("\n Start /saveMoveTodayPatients");
		appService.saveMoveTodayPatients(moveTodayPatients, new DateTime());
		return moveTodayPatients;
	}
	@RequestMapping(value = "/save-{yyyy}-{mm}-{dd}-Patients", method = RequestMethod.POST)
	public  @ResponseBody Map<String, Object> save_yyyymmdd_Patients(
			@PathVariable Integer yyyy , @PathVariable Integer mm, @PathVariable Integer dd
			,@RequestBody Map<String, Object> moveTodayPatients, Principal userPrincipal) {
		DateTime dateTime = new DateTime(yyyy,mm,dd,0,0);
		logger.info("\n Start /save_yyyymmdd_Patients"+dateTime);
		moveTodayPatients.put("today",dateTime.getMillis());
		appService.saveMoveTodayPatients(moveTodayPatients,dateTime);
		return moveTodayPatients;
	}
	@RequestMapping(value = "/testGet", method = RequestMethod.GET)
	public  @ResponseBody Map<String, Object> testGet(@RequestBody Map<String, Object> moveTodayPatients) {
		logger.debug("------------testGet-------------------");
		logger.debug(""+moveTodayPatients);
		Map<String, Object> map = new HashMap<>();
		map.put("Hello", "World");
		return map;
	}
	
	@RequestMapping(value = "/readMoveTodayPatients", method = RequestMethod.GET)
	public  @ResponseBody Map<String, Object> readMoveTodayPatients(Principal userPrincipal) {
		logger.info("\n Start /readMoveTodayPatients");
		Map<String, Object> moveTodayPatients = new HashMap<String, Object>();
		DateTime today = new DateTime();
		List<Map<String, Object>> moveTodayPatientsList = appService.readMoveTodayPatients(today);
		moveTodayPatients.put("moveTodayPatientsList", moveTodayPatientsList);
		moveTodayPatients.put("today", new DateTime().toDate());
		return moveTodayPatients;
	}
	@RequestMapping(value = "/readMove-{yyyy}-{mm}-{dd}-Patients", method = RequestMethod.GET)
	public  @ResponseBody Map<String, Object> readMoveyyyymmddPatients(
			@PathVariable Integer yyyy , @PathVariable Integer mm, @PathVariable Integer dd
			,Principal userPrincipal) {
		Map<String, Object> moveTodayPatients = new HashMap<String, Object>();
		DateTime dateTime = new DateTime(yyyy,mm,dd,0,0);
		logger.info("\n Start /readMove-"
				+ AppConfig.yyyyMMddDateFormat.format(dateTime.toDate())
				+ "-Patients ("+dateTime+") ");
		List<Map<String, Object>> moveTodayPatientsList = appService.readMoveTodayPatients(dateTime);
		moveTodayPatients.put("moveTodayPatientsList", moveTodayPatientsList);
		moveTodayPatients.put("today", dateTime.toDate());
		return moveTodayPatients;
	}
	// 2  Показ кількості надходжень/виписки хворих за останні 7 днів – movePatients.html.
		// 2.1  Зчитування руху хворих за останні 7 днів – readMovePatients
		@RequestMapping(value = "/readMovePatients", method = RequestMethod.GET)
		public  @ResponseBody Map<String, Object> readMovePatients( Principal userPrincipal) {
			logger.info("\n Start /readMovePatients");
			Map<String, Object> readMovePatients = appService.readMovePatients();
			return readMovePatients;
		}
		//excell
		@Autowired private ExcelService excelService;
		@RequestMapping(value = "/create-read-{yyyy}-{mm}-{dd}-excel", method = RequestMethod.GET)
		public String createReadExcell(
				@PathVariable Integer yyyy , @PathVariable Integer mm, @PathVariable Integer dd,
				Principal userPrincipal) {
			logger.debug("/create-read-{yyyy}-{mm}-{dd}-excel");
			DateTime dateTime = new DateTime(yyyy,mm,dd,0,0);
			List<Map<String, Object>> moveTodayPatientsList = appService.readMoveTodayPatients(dateTime);
			excelService.createExcel(moveTodayPatientsList, dateTime);
			return "redirect: excel/pyx2015.xls"; 
		}
}
