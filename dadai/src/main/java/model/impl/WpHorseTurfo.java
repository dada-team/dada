package main.java.model.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import main.java.model.interfaces.Serializer;
import main.java.model.interfaces.WebPage;

public class WpHorseTurfo extends WebPage {

	private String age;
	private String sex;
	private List<String> lastPerformances = new ArrayList<String>();

	public WpHorseTurfo(URL url, Integer id, String name, String age, String sex, List<String> lastPerformances) {
		super(url, id, name);
		this.age = age;
		this.sex = sex;
		this.lastPerformances = lastPerformances;
		// TODO Auto-generated constructor stub
	}
	
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public List<String> getLastPerformances() {
		return lastPerformances;
	}

	public void setLastPerformances(List<String> lastPerformances) {
		this.lastPerformances = lastPerformances;
	}

	@Override
	public JsonElement serialize() {
		JsonObject result = new JsonObject();

		result.add("name", new JsonPrimitive(this.getName()));
		result.add("age", new JsonPrimitive(this.age.toString()));
		result.add("sex", new JsonPrimitive(this.sex));
		result.add("lastResults", new JsonPrimitive(this.lastPerformances.toString()));

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
		return new StringBuilder().append(getName()).append("_").append(getCreationTimeFormatted()).append(".json").toString();
	}

}
