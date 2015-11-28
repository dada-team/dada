package main.java.model.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import main.java.model.interfaces.Serializer;
import main.java.model.interfaces.WebPage;
import main.java.model.impl.Finish;
import main.java.model.impl.FinishList;

public abstract class WpDetailsTurfo extends WebPage implements Serializer {

	Logger  logger = Logger.getLogger("main.java.mode.impl.WpDetailsTurfo");
	
	private Prediction prediction;
	private List<Pronostic> pronostics;
	private FinishList finishList;
	

	public WpDetailsTurfo(URL url, Integer id, String name) {
		super(url, id, name);
		this.pronostics = new ArrayList<Pronostic> ();
		// TODO Auto-generated constructor stub
	}

	public List<Pronostic> getPronostics() {
		return this.pronostics;
	}

	public void setPronostics(List<Pronostic> pronostics) {
		this.pronostics = pronostics;
	}

	public Prediction getPrediction() {
		return this.prediction;
	}

	public void setPrediction(Prediction prediction) {
		this.prediction = prediction;
	}

	public void setFinishList(FinishList finishList) {
		this.finishList = finishList;
	}

	public FinishList  getFinishList() {
		return this.finishList;
	}
	public JsonElement serialize(){
		logger.debug("serialize");
		JsonObject result = new JsonObject();
		
		if(this.prediction.predicter != null)
			result.add("pronostiqueur", new JsonPrimitive(this.prediction.predicter));
		
		JsonArray pronostics = new JsonArray();
		
		int rank = 1;

		for (String horseName : this.prediction.orderedHorseRankPrediction ) {
				JsonObject pronostic = new JsonObject();
			    pronostic.add("place", new JsonPrimitive(rank));
			    pronostic.add("horse", new JsonPrimitive(horseName));
			    pronostics.add(pronostic);
			    rank++;
		}
		
			
		result.add("pronostiques", pronostics);
		//result.add("url", new JsonPrimitive(this.url.toString()));

		JsonArray pronostics2 = new JsonArray();
		for (Pronostic prono : this.pronostics ) {
				JsonObject pronostic2 = new JsonObject();
			    if(prono.getClass().getName().equals("main.java.model.impl.PronosticModelOne")){
			     pronostics2.add(((PronosticModelOne)prono).serialize());
			    }
			    else{
			    pronostics2.add(((PronosticModelTwo)prono).serialize());
			    }
		}
		//result.add("partant", finishList.serialize());
		return result;
		
	}
}
