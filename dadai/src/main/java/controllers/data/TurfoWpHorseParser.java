package main.java.controllers.data;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.select.Elements;

import main.java.controllers.interfaces.WpParser;
import main.java.model.impl.WpHorseTurfo;
import main.java.model.interfaces.WebPage;

public class TurfoWpHorseParser extends WpParser {

	Logger logger = Logger.getLogger("main.java.controllers.sniffers.TurfoWpHorseParser");

	public TurfoWpHorseParser(boolean useProxy) {
		super(useProxy);
	}

	@Override
	public WebPage parse(URL url) throws Exception {
		return parse(url, null);
	}

	public String tranformAge(String age, DateTime dtEvent) {
		long yearsInBetween = 0;
		logger.debug("age : " + age);
		String patternString = "([0-9]+) ans";
		Pattern pattern = Pattern.compile(patternString);// .html?idcourse=([0-9]+)
		java.util.regex.Matcher matcher = pattern.matcher(age.toString());

		if (matcher.find()) {
			Integer currentAge = Integer.parseInt(matcher.group(1));
			logger.debug("current age " + currentAge);
			Duration duration = new Duration(dtEvent, DateTime.now());
			logger.debug("duration in days : " + duration.toStandardDays().getDays());
			yearsInBetween = duration.getStandardDays() / 365;
			logger.debug("years in between : " + yearsInBetween);
			
			Integer trueAge = (int) (currentAge - yearsInBetween);
			logger.debug("true age " + trueAge);
			
			age = trueAge + " ans";
		}

		return age;
	}

	public WebPage parse(URL url, DateTime dtEvent) throws Exception {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		logger.info("... parse " + url);
		Document doc = Jsoup.parse(this.download(url));
		String path = url.getPath();
		String horseName = doc.select(WpHorseParameters.HORSE_NAME_SELECT).get(0).text().trim();

		String sex = doc.select(WpHorseParameters.HORSE_SEX_SELECT).get(0).select("td:eq(4)").get(0).text();
		String age = doc.select(WpHorseParameters.HORSE_AGE_SELECT).get(1).select("td:eq(4)").get(0).text();

		if (dtEvent != null)
			age = tranformAge(age, dtEvent);

		Elements horsesRanks = doc.select(WpHorseParameters.HORSE_MUSIC_SELECT);

		Integer horseId = null;

		Pattern pattern = Pattern.compile(WpHorseParameters.URL_HORSE_ID_EXTRACT);// .html?idcourse=([0-9]+)
		java.util.regex.Matcher matcher = pattern.matcher(url.toString());

		if (matcher.find()) {
			horseId = Integer.parseInt(matcher.group(1));
		}
		logger.debug("_______________________");
		logger.debug("... info horse found : ");
		logger.debug(" ... sex : " + sex);
		logger.debug(" ... age : " + age);
		logger.debug(" ... name : " + horseName);
		logger.debug(" ... id : " + horseId);
		List<String> rs = new ArrayList<String>();

		logger.debug("....... past perf.");
		boolean includeHorseRank;
		
		for (int i = 0; i < horsesRanks.size(); i++) {
			includeHorseRank = true;
			
			if (dtEvent != null) {
				includeHorseRank = testRaceDate(dtEvent, horsesRanks.get(i).attr("href").trim().toString());
			}

			if (includeHorseRank) {
				URL horseRace = new URL(url, horsesRanks.get(i).attr("href").trim().toString());
				logger.debug("...... race : " + horseRace.toString());
				String horseRes = horsesRanks.get(i).text().trim();
				logger.debug("...... result : " + horseRes);
				rs.add(horseRes);
			}
		}
		logger.debug("_______________________");
		WebPage wp = new WpHorseTurfo(url, null, horseName, age, sex, rs);
		// Elements horsesUrl = doc.select("table.tableauLine:eq(0) tbody tr
		// td:eq(3) a[href]");
		return wp;
	}

	private boolean testRaceDate(DateTime dtEvent, String horseRankHref) {
		// TODO Auto-generated method stub
		boolean includeHorseRank = true;
		
		Pattern patternRace = Pattern.compile(WpRaceParameters.URL_RACE_PATTERN_REGEXP);// .html?idcourse=([0-9]+)
		java.util.regex.Matcher matcherRace = patternRace
				.matcher(horseRankHref);

		DateTime dt = null;

		if (matcherRace.find()) {
			dt = transformToDate(matcherRace.group(1));
			logger.debug("... date course : " + dt.toString());
		}

		if (dt.isAfter(dtEvent) || dt.isEqual(dtEvent)) {
			logger.debug("... date course " + dt.toString() + " is after/equal " + dtEvent.toString());
			includeHorseRank = false;
		}
		
		return includeHorseRank;
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
