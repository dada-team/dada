package test.java.controllers.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.joda.time.DateTime;
import org.jsoup.select.Elements;
import org.junit.Test;

import main.java.controllers.data.TurfoWpRaceParser;
import main.java.controllers.data.WpRaceParameters;
import main.java.controllers.interfaces.WpParser;
import main.java.controllers.sniffers.TurfoRaceSniffer;
import main.java.model.impl.WpRaceTurfo;

public class TurfoWpRaceParserTest {

	@Test
	public void testBasicExecution() throws Exception {
		WpParser myUnit = new TurfoWpRaceParser(false);

		URL testUrl = new URL(
				"http://www.turfomania.fr/pronostics/rapports-vendredi-20-novembre-2015-lyon-la-soie-prix-de-tire-gerbe.html?idcourse=203530");
		myUnit.parse(testUrl);

		assertEquals(0, 0);
	}

//	@Test
	public void testUrlParsing() throws IOException {
		TurfoWpRaceParser myUnit = new TurfoWpRaceParser(false);

		URL testUrl = new URL(
				"http://www.turfomania.fr/pronostics/rapports-mercredi-16-avril-2014-auteuil-prix-champoreau.html?idcourse=151128");

		WpRaceTurfo wp = myUnit.initWebPage(testUrl);
		assertTrue(wp.getFileName() != null);
		assertTrue(wp.getDtEvent() != null);
	}
	
}
