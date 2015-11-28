package main.java.controllers.data;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import main.java.controllers.interfaces.WpParser;
import main.java.model.impl.Prediction;
import main.java.model.impl.Pronostic;
import main.java.model.impl.WpDetailsTurfoModelOne;
import main.java.model.impl.WpDetailsTurfo;
import main.java.model.impl.WpDetailsTurfoModelTwo;
import main.java.model.interfaces.WebPage;
import main.java.model.impl.Finish;
import main.java.model.impl.FinishList;

public class TurfoWpDetailsRaceParser extends WpParser {

	Logger logger = Logger.getLogger("main.java.controllers.sniffers.TurfoWpDetailsParser");
	TurfoWpHorseParser horseParser;
	protected FinishList  fl;
public FinishList getFinishList(){
return fl;
}
	public TurfoWpDetailsRaceParser(boolean initProxies) {
		super(initProxies);
		this.horseParser = new TurfoWpHorseParser(false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public WebPage parse(URL url) throws IOException, InterruptedException, Exception {
		// TODO Auto-generated method stub
		logger.info("... parse : " + url.toString());
		Document doc = Jsoup.parse(this.download(url));
		Elements headers = doc.select("table.tablesorter.tableauLine thead th");
		return parse(url, headers, doc);
	}

	private WebPage parse(URL url, Elements headers, Document doc) throws Exception {
		// 1 : caracteristiques chevaux
		WebPage wp = null;
		if (isFirstModel(headers)) {
			logger.debug("... page is first model");
			wp = parseDocFirstModel(url, doc);
		} else if (isSecondModel(headers)) {
			logger.debug("... page is second model");
			wp = parseDocSecondModel(url, doc);
		}
	
		return wp;
	}

	private boolean isSecondModel(Elements headers) {
		// TODO Auto-generated method stub
		String distance = headers.get(2).text();
		String record = headers.get(7).text();

		if (distance.equals("Dist.") && record.equals("Record"))
			return true;
		else
			return false;
	}

	private boolean isFirstModel(Elements headers) {
		// TODO Auto-generated method stub
		String poids = headers.get(2).text();
		String cordes = headers.get(3).text();
		String oeilleres = headers.get(8).text();

		if (poids.equals("Poids") && cordes.equals("Corde") && oeilleres.equals("Oeill."))
			return true;
		else
			return false;
	}

	private WebPage parseDocFirstModel(URL url, Document doc) throws Exception {
		//2 : horses
		Elements horsesUrl = doc.select("table.tablesorter.tableauLine tbody tr td:eq(1) a[href]");
		Elements horsesCotes = doc.select("table.tablesorter.tableauLine tbody tr td:eq(11)");
		Elements horsesJockey = doc.select("table.tablesorter.tableauLine tbody tr td:eq(7) a[href]");
		Elements horsesTrainers = doc.select("table.tablesorter.tableauLine tbody tr td:eq(8) a[href]");
		 fl = new FinishList();
		Elements horsesPredictions = doc.select("table.tablesorter.tableauLine tbody tr");
		Elements jockeyWeights = doc.select("table.tablesorter.tableauLine tbody tr");
		Elements horsesCordes = doc.select("table.tablesorter.tableauLine tbody tr");
		Elements horsesOeilleres = doc.select("table.tablesorter.tableauLine tbody tr");

		WpDetailsTurfoModelOne wp = new WpDetailsTurfoModelOne(url, null, null);

		for (int i = 0; i < horsesPredictions.size(); i++) {
			String name = horsesPredictions.get(i).select("td:eq(1) a").text().trim().toString();
			Float weight = null;

			try {
				weight = Float.parseFloat(
						jockeyWeights.get(i).select("td:eq(4)").text().trim().toString().replaceAll(",", "."));
			} catch (Exception e) {
				logger.error(e);
			}


			Integer corde = null;
			try {
				corde = Integer.parseInt(horsesCordes.get(i).select("td:eq(5)").text().trim().toString());
			} catch (Exception e) {
				logger.error(e);
			}
			String oeilleres = horsesOeilleres.get(i).select("td:eq(10)").text().trim().toString();

			logger.debug("... name : " + name);
			logger.debug("... weight : " + weight);
			logger.debug("... corde : " + corde);
			logger.debug("... oeilleres : " + oeilleres);
			if(withHorseDetail){
	try {
				int rang = (i + 1);
				Float cote = null;

				try {
					cote = Float.parseFloat(horsesCotes.get(i).text());
					logger.debug(cote);
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

				WebPage horseWebPage = this.horseParser.parse(new URL(horseURL));
				// look in detailed statistics to get values corresponding to
				// the horseName

				Finish finish = new Finish(-1, cote, horseJockey, horseTrainer,horseWebPage);
				finish.setPoids(weight);
				finish.setCorde(corde);
				finish.setOeilleres(oeilleres);
				fl.getFinishes().add(finish);
			} catch (Exception e) {
				logger.error(e);
			}
	((WpDetailsTurfo)wp).setFinishList(fl);
			}
			wp.addPronostic(name, weight, corde, oeilleres);
		}

		logger.debug("... set prediction");
		try {
			wp.setPrediction(getPrediction(doc));
		} catch (Exception e) {
			logger.error("... no prediction found");
			logger.error(e);
		}

		logger.debug("... return");
		//logger.debug(wp.serialize());
		return wp;
	}
boolean withHorseDetail = false;
public void setWithHorseDetail(boolean b){
this.withHorseDetail = b;
}
	private WebPage parseDocSecondModel(URL url, Document doc) throws Exception {
		// TODO Auto-generated metho	//2 : horses
		Elements horsesUrl = doc.select("table.tablesorter.tableauLine tbody tr td:eq(1) a[href]");
		Elements horsesCotes = doc.select("table.tablesorter.tableauLine tbody tr td:eq(11)");
		Elements horsesJockey = doc.select("table.tablesorter.tableauLine tbody tr td:eq(7) a[href]");
		Elements horsesTrainers = doc.select("table.tablesorter.tableauLine tbody tr td:eq(8) a[href]");
		 fl = new FinishList();
		Elements horsesDistance = doc.select("table.tablesorter.tableauLine tbody tr");
		Elements horsesRecord = doc.select("table.tablesorter.tableauLine tbody tr");
		WpDetailsTurfoModelTwo wp = new WpDetailsTurfoModelTwo(url, null, null);

		for (int i = 0; i < horsesDistance.size(); i++) {
			String name = horsesDistance.get(i).select("td:eq(1) a").text().trim().toString();
			Integer distance = Integer.parseInt(horsesDistance.get(i).select("td:eq(4)").text().trim().toString());

			String time = horsesRecord.get(i).select("td:eq(9)").text().trim().toString();
			String timePatternParsing = "([0-9]+)'([0-9]+)\"([0-9]+)";
			Pattern patternTime = Pattern.compile(timePatternParsing);
			java.util.regex.Matcher matcherTime = patternTime.matcher(time);

			Period period = null;

			if (matcherTime.find()) {
				period = new Period(0, Integer.parseInt(matcherTime.group(1)), Integer.parseInt(matcherTime.group(2)),
						(Integer.parseInt(matcherTime.group(3)) * 100));
			} else {
				logger.warn("... time is at a weird format");
			}

			logger.debug("... name :" + name);
			logger.debug("... period :" + period.toStandardDuration().getMillis());
			logger.debug("... distance :" + distance);
			if(withHorseDetail){
	try {
				int rang = (i + 1);
				Float cote = null;

				try {
					cote = Float.parseFloat(horsesCotes.get(i).text());
					logger.debug(cote);
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

				WebPage horseWebPage = this.horseParser.parse(new URL(horseURL));
				// look in detailed statistics to get values corresponding to
				// the horseName

				Finish finish = new Finish(-1, cote, horseJockey, horseTrainer,horseWebPage);
				finish.setPeriod(period);
				finish.setDistance(distance);
				fl.getFinishes().add(finish);
			} catch (Exception e) {
				logger.error(e);
			}
	((WpDetailsTurfo)wp).setFinishList(fl);
		}
			wp.addPronostic(name, period, distance);
		}


		logger.debug("... set prediction");
		try {
			wp.setPrediction(getPrediction(doc));
		} catch (Exception e) {
			logger.error("... no prediction found");
			logger.error(e);
		}

		return wp;
	}

	private Prediction getPrediction(Document doc) throws Exception {
		String patternStringRankHorse = "([0-9]+)(.+)";
		Pattern patternH = Pattern.compile(patternStringRankHorse);

		Elements expertPredictions = doc.select("div.colTreeDetailBloc2").get(0).select("div.choixJeux");
		List<String> horseRankPrediction = new ArrayList<String> ();

		for (int i = 0; i < expertPredictions.size(); i++) {
			int rank = (i + 1);
			String horseName = expertPredictions.get(i).text();
			java.util.regex.Matcher matcherH = patternH.matcher(horseName);

			if (matcherH.find()) {
				horseName = matcherH.group(2);
				horseRankPrediction.add(horseName);
			} else {
				logger.warn("... no prediction for the horse");
			}

			logger.debug("... rank " + rank);
			logger.debug("... horse : " + horseName);
		}

		// 3 : predicteur
		String expertPredicter = doc.select("div.choixPronotiqueur").get(0).text().toString();

		String patternStringPredicter = "(Pronostiqué par)(.+)";
		Pattern patternP = Pattern.compile(patternStringPredicter);// .html?idcourse=([0-9]+)
		java.util.regex.Matcher matcherP = patternP.matcher(expertPredicter);

		if (matcherP.find()) {
			expertPredicter = matcherP.group(2);
		} else {
			logger.warn("... impossible to catch predicter name or no predictor");
			throw new Exception("... impossible to catch predicter name");
		}

		logger.debug("... predicteur : " + expertPredicter);
		Prediction p = new Prediction(expertPredicter, horseRankPrediction);

		return p;
	}

}
