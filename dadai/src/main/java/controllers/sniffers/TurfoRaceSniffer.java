package main.java.controllers.sniffers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import main.java.controllers.interfaces.Sniffer;
import main.java.controllers.interfaces.WpParser;
import main.java.model.interfaces.WebPage;

public class TurfoRaceSniffer extends WpParser implements Sniffer<URL> {
	
	Logger  logger = Logger.getLogger("main.java.controllers.sniffers.TurfoRaceSniffer");
	
	private DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
	private TurfoRaceParameters parameters;

	
	public TurfoRaceSniffer(boolean initProxies) {
		  super(initProxies);
		  this.parameters = new TurfoRaceParameters();
	}
	
	/**
	 * 
	 */
	@Override
	public List<URL> sniff(DateTime dDebut, DateTime dFin) throws Exception {
		
		List<URL> urlDateFound = new ArrayList<URL> ();
		URIBuilder ub = new URIBuilder(getMainUrl().toString());
		logger.info("_____________________________");
		logger.info("... sniff ");
		logger.info("... main site URL : " + ub.toString());
		DateTime currentDate = dDebut;	
		
		while(currentDate.isBefore(dFin) || currentDate.equals(dFin)) {
			ub.addParameter(parameters.getUrlGetDateArg(), currentDate.toString(dtf));
			
			logger.debug("... race sniffed : " + ub.build().toURL());
			//Document doc = Jsoup.connect(ub.build().toString()).get();
			Document doc = Jsoup.parse(this.download(ub.build().toURL()));
			urlDateFound.addAll(extractAllRaceURL(doc));
			
			currentDate = currentDate.plusDays(1);
			ub.clearParameters();
		}
		logger.info("_____________________________");
		return urlDateFound;
	}
	

	/**
	 * @throws Exception 
	 * 
	 */
	@Override
	public List<URL> sniff() throws Exception {
		DateTime date = new DateTime();
		return sniff(date, date);
	}
	
	/**
	 * 
	 * @param doc
	 * @return
	 * @throws MalformedURLException
	 */
	private List<URL> extractAllRaceURL(Document doc) throws MalformedURLException {
		List<URL> urlExtracted = new ArrayList<URL> ();
		Elements links = doc.select(parameters.getRaceUrlRegexp());
		
		for (Element link : links) {
			urlExtracted.add(new URL(getMainUrl(), link.attr("href").trim())); 
		}
		
		return urlExtracted;
	}

	/**
	 * 
	 */
	private URL getMainUrl() throws MalformedURLException {
		// TODO Auto-generated method stub
		return new URL(parameters.getMainRaceUrl());
	}

	@Override
	public WebPage parse(URL url) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}
}
