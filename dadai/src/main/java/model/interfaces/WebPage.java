package main.java.model.interfaces;

import java.net.URL;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public abstract class WebPage implements Serializer {
	
	protected URL url;
	protected Integer id;
	private String name;
	protected DateTime creationTime;
	
	public WebPage(URL url, Integer id, String name){
		this.url = url;
		this.id = id;
		this.setName(name);
		this.creationTime = DateTime.now();
	}
	
	public String extractInformation() {
		// TODO Auto-generated method stub
		return this.serialize().toString();
	}

	public String getCreationTimeFormatted() {
		String pattern = "yyyy-MM-dd_hh.mm.ss";
		DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
		String formatted = formatter.print(this.creationTime);
		return formatted;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
