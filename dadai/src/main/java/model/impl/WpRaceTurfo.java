package main.java.model.impl;

import java.net.URL;
import java.security.Timestamp;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import main.java.model.interfaces.WebPage;

public class WpRaceTurfo extends WebPage {

	private DateTime dtEvent;
	private FinishList finishList;
	private String raceDescription;
	private WpDetailsTurfo wpDetail;
	
	Logger  logger = Logger.getLogger("main.java.mode.impl.WpRaceTurfo");


	public WpRaceTurfo(URL url, Integer id, String name, DateTime dtEvent, String raceDescription, FinishList finishList) {
		super(url, id, name);
		this.dtEvent = dtEvent;
		this.finishList = finishList;
		this.raceDescription = raceDescription;
		// TODO Auto-generated constructor stub
	}

	@Override
	public JsonElement serialize() {
		logger.debug("... serializing race");
		// TODO Auto-generated method stub
		JsonObject result = new JsonObject();
		if(id!=null)
		result.add("id", new JsonPrimitive(this.id));
		if(this.getName()!=null)
		result.add("name", new JsonPrimitive(this.getName()));
		//result.add("url", new JsonPrimitive(this.url.toString()));
		result.add("description", new JsonPrimitive(this.raceDescription));
		
		String pattern = "yyyy-MM-dd";
		DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
		String formattedDate = formatter.print(this.dtEvent);
		
		result.add("date", new JsonPrimitive(formattedDate));
		result.add("results", this.finishList.serialize());
		
		if (this.wpDetail != null) {
			if(this.wpDetail.getPrediction() != null)
				result.add("predictions", this.wpDetail.serialize());
		}
		
		return result;
	}

	@Override
	public Object deserialize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileName() {
		// TODO Auto-generated method stub
		return new StringBuilder().append(getDtEventFormatted()).append("_").append(getName()).append("_")
				.append(getCreationTimeFormatted()).append(".json").toString();
	}

	public DateTime getDtEvent() {
		return dtEvent;
	}

	public void setFinishList(FinishList finishList) {
		this.finishList = finishList;
	}
	
	public FinishList  getFinishList() {
		return this.finishList;
	}
	
	public String getDtEventFormatted() {
		String pattern = "yyyy-MM-dd";
		DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
		String formatted = formatter.print(this.dtEvent);
		return formatted;
	}

	public void setRaceDescription(String raceDescription) {
		// TODO Auto-generated method stub
		this.raceDescription = raceDescription;
	}
	
	
	public void setWpDetail(WpDetailsTurfo wpDetail) {
		logger.info("... set wp detail");
		this.wpDetail = wpDetail;
	}

	public void enrichWithDetails() {
		logger.info("... enrich with details");
		// TODO Auto-generated method stub
		if (this.wpDetail != null) {
			logger.info("... enriching with details");
			Iterator<Finish> it = this.finishList.getFinishes().iterator();
			
			while(it.hasNext()) {
				Finish f = it.next();
				String horseName = f.horse.getName();
				logger.debug("... horse name searched : " + horseName);
				Iterator<Pronostic> itp = this.wpDetail.getPronostics().iterator();
				
				while(itp.hasNext()) {
					Pronostic p = itp.next();
					String horseNamePrediction = p.horseName;
					logger.debug("... horse prediction : " + horseNamePrediction);
					if (horseName.startsWith(horseNamePrediction)) {
						p.horseName = horseName;
						f.setPronostic(p);
						logger.debug("... pronostic RETROUVE");	
					}
				}
			}
		} else {
			logger.warn("... no details");
		}
	}
	
}
