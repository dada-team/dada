package test.java.controllers.sniffers;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import main.java.controllers.sniffers.TurfoRaceSniffer;

public class TurfoRaceSnifferTest {
	Logger  logger = Logger.getLogger("test.java.controllers.sniffers.TurfoRaceSnifferTest");
	DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy");
	
	public TurfoRaceSnifferTest() {
		  
	}
	
//	@Test
    public void testNoResultsUnexistingDates() {
        TurfoRaceSniffer myUnit = new TurfoRaceSniffer(false);

        DateTime dtStart = formatter.parseDateTime("12/11/2066");
        DateTime dtEnd = formatter.parseDateTime("12/11/2066");
        
        List<URL> urls = null;
        
		try {
			urls = myUnit.sniff(dtStart, dtEnd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        assertEquals(0, urls.size());
    }
	
//	@Test
    public void testNoResultsIncoherentDates() {
        TurfoRaceSniffer myUnit = new TurfoRaceSniffer(false);

        DateTime dtStart = formatter.parseDateTime("12/11/2016");
        DateTime dtEnd = formatter.parseDateTime("11/11/2016");
        
        List<URL> urls = null;
        
		try {
			urls = myUnit.sniff(dtStart, dtEnd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        assertEquals(0, urls.size());
    }
	
//	@Test
    public void testURLAreFound() {
        TurfoRaceSniffer myUnit = new TurfoRaceSniffer(false);

        DateTime dtStart = formatter.parseDateTime("01/07/2015");
        DateTime dtEnd = formatter.parseDateTime("01/07/2015");
        
        List<URL> urls = null;
        
		try {
			urls = myUnit.sniff(dtStart, dtEnd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        assertFalse(urls.isEmpty());
    }
	
//	@Test
    public void testCorrectNbOfURLAreFound() {
        TurfoRaceSniffer myUnit = new TurfoRaceSniffer(false);

        DateTime dtStart = formatter.parseDateTime("01/07/2015");
        DateTime dtEnd = formatter.parseDateTime("01/07/2015");
        
        List<URL> urls = null;
        
		try {
			urls = myUnit.sniff(dtStart, dtEnd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        assertEquals(32, urls.size());
    }
	
//	@Test
    public void testCorrectURLAreFound() throws Exception {
        TurfoRaceSniffer myUnit = new TurfoRaceSniffer(false);

        DateTime dtStart = formatter.parseDateTime("01/07/2015");
        DateTime dtEnd = formatter.parseDateTime("01/07/2015");
        
        List<URL> urls = null;
        
		try {
			urls = myUnit.sniff(dtStart, dtEnd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<URL> myTestList = new ArrayList<URL> ();
		
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-vichy-prix-centre-france-la-montagne.html?idcourse=190254"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-vichy-prix-de-l-indre.html?idcourse=190255"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-vichy-prix-ordre-experts-comptables-d-auvergne.html?idcourse=190256"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-vichy-prix-des-ventes-du-marault.html?idcourse=190257"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-vichy-prix-de-vaumas.html?idcourse=190258"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-vichy-prix-vans-theault-prix-de-savoie.html?idcourse=190259"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-vichy-prix-de-la-societe-du-cheval-francais.html?idcourse=190260"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-deauville-prix-de-cayras.html?idcourse=190261"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-deauville-prix-de-bretoncelles.html?idcourse=190262"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-deauville-prix-caprice.html?idcourse=190263"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-deauville-prix-de-callenville.html?idcourse=190264"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-deauville-prix-de-coulonces.html?idcourse=190265"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-deauville-prix-de-villerville.html?idcourse=190266"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-deauville-prix-du-cap-d-antifer.html?idcourse=190267"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-deauville-prix-du-molay-littry.html?idcourse=190268"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-lisieux-prix-agrial.html?idcourse=190245"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-lisieux-prix-e-leclerc.html?idcourse=190246"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-lisieux-prix-d-agen.html?idcourse=190247"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-lisieux-prix-bricorama.html?idcourse=190248"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-lisieux-prix-equip-horse.html?idcourse=190249"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-lisieux-prix-les-bruyeres-carre.html?idcourse=190250"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-lisieux-prix-pinel-emmanuel-espace-vert-gr-a.html?idcourse=190251"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-lisieux-prix-pinel-emmanuel-espace-vert-gr-b.html?idcourse=190252"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-lisieux-prix-espace-emeraude-marolles.html?idcourse=190253"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-hamburg-prix-gerhard-kurtze-gedachtnisrennen.html?idcourse=190269"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-hamburg-prix-preis-vom-pferdesportverlag-ehlers.html?idcourse=190270"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-hamburg-prix-hamburg-dresden-pokal.html?idcourse=190271"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-hamburg-prix-preis-der-mitglieder-des-hamburger-renn-clubs.html?idcourse=190272"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-hamburg-prix-preis-der-grossen-woche-baden-baden-iffezheim.html?idcourse=190273"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-hamburg-prix-hamburger-flieger-preis.html?idcourse=190274"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-hamburg-prix-heinz-jentsch-rennen.html?idcourse=190275"));
		myTestList.add(new URL("http://www.turfomania.fr/pronostics/rapports-mercredi-01-juillet-2015-hamburg-prix-preis-der-kincsem-grafik.html?idcourse=190276"));
					
        assertEquals(urls, myTestList);
    }

	
//	@Test
    public void testNoBadURLAreFound() throws Exception {
        TurfoRaceSniffer myUnit = new TurfoRaceSniffer(false);

        DateTime dtStart = formatter.parseDateTime("01/05/2015");
        DateTime dtEnd = formatter.parseDateTime("01/06/2015");
        
        List<URL> urls = null;
        
		try {
			urls = myUnit.sniff(dtStart, dtEnd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        for (URL url : urls) {
        	 if (! Pattern.matches("http://.*idcourse=[0-9]+", url.toString())) {
        		 assertFalse(true);
        		 logger.debug(" bad url : " + url.toString());
        	 } 
        }
    }
	
//	@Test
    public void testURLAreFoundWeek() {
        TurfoRaceSniffer myUnit = new TurfoRaceSniffer(false);

        DateTime dtStart = formatter.parseDateTime("01/07/2015");
        DateTime dtEnd = formatter.parseDateTime("07/07/2015");
        
        List<URL> urls = null;
        
		try {
			urls = myUnit.sniff(dtStart, dtEnd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        assertFalse(urls.isEmpty());
    }
	
}
