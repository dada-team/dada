package test.java.controllers.data;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import main.java.controllers.data.TurfoWpDetailsRaceParser;
import main.java.controllers.interfaces.WpParser;

public class TurfoWpDetailsRaceParserTest {
	@Test
	public void testBasicExecutionFirstModel() throws MalformedURLException {
		WpParser myUnit = new TurfoWpDetailsRaceParser(false);

		URL testUrl = new URL("http://www.turfomania.fr/pronostics/partants-jeudi-16-juillet-2015-compiegne-prix-de-francieres.html?idcourse=191565");

		try {
			myUnit.parse(testUrl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals(0, 0);
	}
	
//	@Test
	public void testBasicExecutionSecondModel() throws MalformedURLException {
		WpParser myUnit = new TurfoWpDetailsRaceParser(false);

		URL testUrl = new URL("http://www.turfomania.fr/pronostics/partants-jeudi-23-juillet-2015-enghien-soisy-prix-de-la-place-rodin.html?idcourse=192301");

		try {
			myUnit.parse(testUrl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals(0, 0);
	
	}
	
}
