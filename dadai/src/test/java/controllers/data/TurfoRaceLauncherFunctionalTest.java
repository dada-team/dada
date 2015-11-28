package test.java.controllers.data;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import main.java.controllers.launchers.TurfoRaceLauncher;

public class TurfoRaceLauncherFunctionalTest {
	Logger  logger = Logger.getLogger("test.java.controllers.launchers.TurfoRaceLauncherFunctionalTest");
	DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

//	@Test
	public void basicExecution(){
		long nanoTimeStart = System.currentTimeMillis();
		File f = new File("src/test/resources");
        DateTime dtStart = formatter.parseDateTime("2015-04-25");
        DateTime dtEnd = formatter.parseDateTime("2015-04-25");
		TurfoRaceLauncher tfl = new TurfoRaceLauncher(f, dtStart, dtEnd);

		try {
			tfl.loadAllData();
			tfl.writeAllData();
			assertTrue(true);
		} catch (Exception e) {
			logger.error(e);
			assertTrue(false);
		}
		
		long nanoTimeEnd = System.currentTimeMillis();
		long milliseconds = nanoTimeStart - nanoTimeEnd;
		
		int seconds = (int) (milliseconds / 1000) % 60 ;
		int minutes = (int) ((milliseconds / (1000*60)) % 60);
		int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
		
		StringBuilder sb = new StringBuilder();
		sb.append(hours);
		sb.append(" Hours ");
        sb.append(minutes);
        sb.append(" Minutes ");
        sb.append(seconds);
        sb.append(" Seconds");
        
        logger.info("... execution time (1 day test) : " + sb.toString());
	}
}
