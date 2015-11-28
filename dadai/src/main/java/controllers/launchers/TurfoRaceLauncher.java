package main.java.controllers.launchers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.joda.time.DateTime;


import main.java.controllers.data.WpRaceManager;
import main.java.controllers.interfaces.Sniffer;

import main.java.controllers.interfaces.WpManager;
import main.java.controllers.sniffers.TurfoRaceSniffer;
import main.java.model.impl.WpRaceTurfo;
import main.java.model.interfaces.WebPage;

public class TurfoRaceLauncher implements Callable<List<WebPage>> {

	private DateTime dtStart;
	private DateTime dtEnd;
	private WpManager webCol;
	private Sniffer<URL> sniffer = new TurfoRaceSniffer(false);
	
	public TurfoRaceLauncher(File outputDirectory, DateTime dtStart, DateTime dtEnd) {
		this.webCol = new WpRaceManager(outputDirectory);
		this.dtStart = dtStart;
		this.dtEnd = dtEnd;
	}
	
	public void loadAllData() throws Exception{
		List<URL> url;

		url = sniffer.sniff(dtStart, dtEnd);
		this.webCol.importWebPagesData(url);

	}
	
	public void writeAllData() throws IOException{
		this.webCol.writeToJSON();
	}

	@Override
	public List<WebPage> call() throws Exception {
		// TODO Auto-generated method stub
		loadAllData();
		return this.webCol.getWebPages();
	}
	
}
