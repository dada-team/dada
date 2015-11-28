package main.java.controllers.data;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hamcrest.Matcher;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import main.java.controllers.interfaces.WpParser;
import main.java.model.impl.Finish;
import main.java.model.impl.FinishList;
import main.java.model.impl.WpDetailsTurfo;
import main.java.model.impl.WpHorseTurfo;
import main.java.model.impl.WpRaceTurfo;
import main.java.model.interfaces.WebPage;

public class TurfoWpRaceParser extends WpParser {

	Logger logger = Logger.getLogger("main.java.controllers.sniffers.TurfoWpRaceParser");
	TurfoWpHorseParser horseParser;
	TurfoWpDetailsRaceParser detailsParser;

	public TurfoWpRaceParser(boolean useProxy) {
		super(useProxy);
		this.horseParser = new TurfoWpHorseParser(useProxy);
		this.detailsParser = new TurfoWpDetailsRaceParser(useProxy);
	}

	@Override
	public WebPage parse(URL url) throws Exception {
		// TODO Auto-generated method stub
		logger.info("... parse : " + url);
		Document doc = Jsoup.parse(this.download(url));
		Elements horsesUrl = doc.select(WpRaceParameters.HORSE_URL_SELECT);
		Elements horsesCotes = doc.select(WpRaceParameters.HORSE_COTES_SELECT);
		Elements horsesJockey = doc.select(WpRaceParameters.HORSE_JOCKEY_SELECT);
		Elements horsesTrainers = doc.select(WpRaceParameters.HORSE_TRAINER_SELECT);

		// Elements horsesUrl = doc.select("table.tableauLine:eq(0) tbody tr
		// td:eq(3) a[href]");
		WpRaceTurfo wp = initWebPage(url);

		String urlDetailRelativeUrl = doc.select(WpRaceParameters.DETAIL_URL_SELECT).get(0).attr("href").trim().toString();
		URL urlDetail = new URL(url, urlDetailRelativeUrl);
		// call and search WebPage detailed statistics

		FinishList fl = new FinishList();

		for (int i = 0; i < horsesUrl.size(); i++) {
			try {
				int rang = (i + 1);
				Float cote = null;

				try {
					cote = Float.parseFloat(horsesCotes.get(i).text());
				} catch (Exception e) {
					cote = null;
				}

				// logger.debug("horse text name : " + horsesUrl.get(i).text());
				String horseURL = new URL(url, horsesUrl.get(i).attr("href").trim().toString()).toString();
				String horseJockey = horsesJockey.get(i).text();
				String horseTrainer = horsesTrainers.get(i).text();

				logger.debug("___________________________");
				logger.debug("... new finish");
				logger.debug("... rank : " + rang);
				logger.debug("... cote : " + cote);
				logger.debug("... horse found : " + horseURL);
				logger.debug("... jockey found : " + horseJockey);

				WebPage horseWebPage = this.horseParser.parse(new URL(horseURL), wp.getDtEvent());
				// look in detailed statistics to get values corresponding to
				// the horseName

				Finish finish = new Finish(i, cote, horseJockey, horseTrainer,horseWebPage);

				fl.getFinishes().add(finish);
			} catch (Exception e) {
				logger.error(e);
			}
		}

		String raceDescription = doc.select(WpRaceParameters.RACE_DESCRIPTION_SELECT).get(0).text();
		logger.debug("... race description : " + raceDescription);

		logger.debug("___________________________");

		// http://www.turfomania.fr/pronostics/rapports-dimanche-12-juillet-2015-chantilly-prix-de-l-hermitage.html?idcourse=191143
		wp.setRaceDescription(raceDescription);
		WpDetailsTurfo wpDetail ;
		if(horsesUrl.size()==0){//course pas débutée
		this.detailsParser.setWithHorseDetail(true);
	wpDetail	= (WpDetailsTurfo) this.detailsParser.parse(urlDetail);
		wp.setFinishList(((WpDetailsTurfo)wpDetail).getFinishList());
			logger.info("pas débutée");
		}else{
		 wpDetail = (WpDetailsTurfo) this.detailsParser.parse(urlDetail);
		wp.setFinishList(fl);
			logger.info("débutée");
		}
		logger.debug("... set details");
		wp.setWpDetail(wpDetail);
		
		logger.debug("... enrich details");
		wp.enrichWithDetails();
		// set description

		logger.info("_________________________");
		logger.debug(wp.serialize());
		return wp;
	}

	public WpRaceTurfo initWebPage(URL url) throws IOException {
		Pattern pattern = Pattern.compile(WpRaceParameters.URL_RACE_PATTERN_REGEXP);// .html?idcourse=([0-9]+)
		java.util.regex.Matcher matcher = pattern.matcher(url.toString());

		Integer id = null;
		DateTime dt = null;
		String nameCourse = null;

		try {
			if (matcher.find()) {
				dt = transformToDate(matcher.group(1));
				nameCourse = matcher.group(2);
				id = Integer.parseInt(matcher.group(3));
			}
		} catch (Exception e) {
			logger.error("error parsing url race");
			logger.error(e);
			throw new IOException("error parsing url race");
		}
		logger.debug("___________________________");
		logger.debug("... new webpage");
		logger.debug("... url : " + url);
		logger.debug("... id : " + id);
		logger.debug("... nameCourse : " + nameCourse);
		logger.debug("... date : " + dt);
		logger.debug("___________________________");

		WpRaceTurfo wp = new WpRaceTurfo(url, id, nameCourse, dt, null, null);

		return wp;
	}

	public WpRaceTurfo initWebPage(URL url, String raceDescription, FinishList fl) throws Exception {
		WpRaceTurfo wp = initWebPage(url);
		wp.setFinishList(fl);
		wp.setRaceDescription(raceDescription);
		return wp;
		// http://www.turfomania.fr/pronostics/rapports-dimanche-12-juillet-2015-chantilly-prix-de-l-hermitage.html?idcourse=191143
	}

	private DateTime transformToDate(String date) {
		// TODO Auto-generated method stub
		String[] parts = date.split("-");
		String day = getDay(parts[0]); // 004
		String month = getMonth(parts[1]); // 034556
		Integer year = Integer.parseInt(parts[2]);

		String strDateTime = day + "/" + month + "/" + year;
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/YYYY");
		DateTime dateTime = DateTime.parse(strDateTime, fmt);

		return dateTime;
	}

	private String getDay(String day) {
		if (day.length() == 1)
			day = "0" + day;

		return day;
	}

	private String getMonth(String month) {
		// TODO Auto-generated method stub
		String[] monthNames = { "janvier", "fevrier", "mars", "avril", "mai", "juin", "juillet", "aout", "septembre",
				"octobre", "novembre", "decembre" };
		Integer indexFound = null;

		for (int i = 1; i <= monthNames.length; i++) {
			if (month.equalsIgnoreCase(monthNames[i - 1])) {
				indexFound = i;
				break;
			}
		}

		String indexReturned = Integer.toString(indexFound);

		if (indexReturned.length() == 1)
			indexReturned = "0" + indexReturned;

		return indexReturned;
	}

}
