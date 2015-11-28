package main.java.model.impl;

import java.net.URL;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import main.java.model.interfaces.Serializer;

public class PronosticModelOne extends Pronostic implements Serializer {
	protected Float poids;
	protected Integer corde;
	protected String oeilleres;
	
	public PronosticModelOne(String horseName, Float poids, Integer corde, String oeilleres) {
		super(horseName);
		this.poids = poids;
		this.corde = corde;
		this.oeilleres = oeilleres;
	}

	@Override
	public JsonElement addToSerialization(JsonObject result) {
		if (this.poids != null)
			result.add("poids", new JsonPrimitive(this.poids));
		if (this.corde != null)
			result.add("corde", new JsonPrimitive(this.corde));
		//result.add("url", new JsonPrimitive(this.url.toString()));
		if (this.oeilleres != null)
			result.add("oeilleres", new JsonPrimitive(this.oeilleres));
		
		return result;
	}
	

	@Override
	public JsonElement serialize() {
		// TODO Auto-generated method stub
		JsonObject result = new JsonObject();
		if (this.horseName != null)
			result.add("horseName", new JsonPrimitive(this.horseName));
		if (this.poids != null)
			result.add("poids", new JsonPrimitive(this.poids));
		if (this.corde != null)
			result.add("corde", new JsonPrimitive(this.corde));
		//result.add("url", new JsonPrimitive(this.url.toString()));
		if (this.oeilleres != null)
			result.add("oeilleres", new JsonPrimitive(this.oeilleres));

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
		return null;
	}

}
