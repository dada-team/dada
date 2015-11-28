package main.java.controllers.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import main.java.controllers.interfaces.WpManager;
import main.java.controllers.interfaces.WpParser;
import main.java.model.interfaces.WebPage;

public class WpRaceManager extends WpManager {

	Logger logger = Logger.getLogger("main.java.controllers.data.WpRaceManager");
	private WpParser parser = new TurfoWpRaceParser(false);
	

	public WpRaceManager(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	@Override
	public void importWebPagesData(List<URL> urls) throws IOException {
		// TODO Auto-generated method stub
		logger.info("... importing " + urls.size() + " url");
		for (URL url : urls) {
			try {
				importWebPageData(url);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		logger.info("... import OVER");
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void importWebPageData(URL url) throws Exception {
		addWebPage(parser.parse(url));
	}

	private void addWebPage(WebPage wp) {
		this.webPages.add(wp);
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeToJSON() throws FileNotFoundException {
		logger.info("... write to JSON");
		
		for (WebPage webpage : webPages) {
			logger.error("... writing : " + webpage.getFileName());
			PrintWriter out = new PrintWriter(new File(outputDirectory, webpage.getFileName()));
			out.println(webpage.extractInformation());
			out.close();
		}
		logger.info("... writing OVER");
		// TODO Auto-generated method stub

	}

}
