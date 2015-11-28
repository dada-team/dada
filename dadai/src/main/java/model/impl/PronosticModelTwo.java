package main.java.model.impl;

import org.joda.time.Period;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import main.java.model.interfaces.Serializer;

public class PronosticModelTwo extends Pronostic implements Serializer {
	protected Period period;
	protected Integer distance;
	
	public PronosticModelTwo(String horseName, Period period, Integer distance) {
		super(horseName);
		this.period = period;
		this.distance = distance;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public JsonElement addToSerialization(JsonObject result) {
		if (this.period != null)
			result.add("record", new JsonPrimitive(this.period.toStandardDuration().getMillis()));
		if (this.distance != null)
			result.add("distance", new JsonPrimitive(this.distance));
		
		return result;
	}

	@Override
	public JsonElement serialize() {
		// TODO Auto-generated method stub
		JsonObject result = new JsonObject();
		if (this.horseName != null)
			result.add("horseName", new JsonPrimitive(this.horseName));
		if (this.period != null)
			result.add("poids", new JsonPrimitive(this.period.toStandardDuration().getMillis()));
		if (this.distance != null)
			result.add("corde", new JsonPrimitive(this.distance));

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
