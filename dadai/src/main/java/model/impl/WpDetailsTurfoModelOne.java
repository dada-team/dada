package main.java.model.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;

import main.java.model.interfaces.Serializer;

public class WpDetailsTurfoModelOne extends WpDetailsTurfo {
	
	public WpDetailsTurfoModelOne(URL url, Integer id, String name) {
		super(url, id, name);
		// TODO Auto-generated constructor stub
	}
	
	public void addPronostic(String horseName, Float poids, Integer corde, String oeilleres) {
		this.getPronostics().add(new PronosticModelOne(horseName, poids, corde, oeilleres));
	}

	@Override
	public JsonElement serialize() {
		// TODO Auto-generated method stub
		return super.serialize();
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
