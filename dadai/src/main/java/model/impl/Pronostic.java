package main.java.model.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class Pronostic {
	protected String horseName;
	
	public Pronostic(String horseName) {
		this.horseName = horseName;
	}

	public abstract JsonElement addToSerialization(JsonObject result);
}
