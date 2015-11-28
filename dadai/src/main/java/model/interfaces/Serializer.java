package main.java.model.interfaces;

import com.google.gson.JsonElement;

public interface Serializer {
	
	public JsonElement serialize();
	
	public Object deserialize();
	
	public String getFileName();
}
