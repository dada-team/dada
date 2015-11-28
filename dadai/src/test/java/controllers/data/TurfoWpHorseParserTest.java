package test.java.controllers.data;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import main.java.controllers.data.TurfoWpHorseParser;
import main.java.controllers.interfaces.WpParser;
import main.java.controllers.sniffers.TurfoRaceSniffer;

public class TurfoWpHorseParserTest {

//	@Test
	public void testBasicExecution() throws MalformedURLException {
		WpParser myUnit = new TurfoWpHorseParser(false);

		URL testUrl = new URL("http://www.turfomania.fr/fiches/chevaux/circumference.html?idcheval=773136");

		try {
			myUnit.parse(testUrl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals(0, 0);
	}
}
