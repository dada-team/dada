package main.java.model.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import main.java.model.interfaces.Serializer;

public class FinishList implements Serializer {
	private List<Finish> finishes = new ArrayList<Finish>();

	@Override
	public JsonArray serialize() {
		// TODO Auto-generated method stub
		JsonArray result = new JsonArray();
		for (Finish finish : getFinishes()) {
			result.add(finish.serialize());
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
		return null;
	}

	public List<Finish> getFinishes() {
		return finishes;
	}

	public void setFinishes(List<Finish> finishes) {
		this.finishes = finishes;
	}

}
