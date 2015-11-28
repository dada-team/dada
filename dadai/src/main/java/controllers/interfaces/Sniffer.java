package main.java.controllers.interfaces;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.joda.time.DateTime;


public interface Sniffer<T> {
	public List<T> sniff() throws Exception;	
	public List<T> sniff(DateTime dDebut, DateTime dFin) throws Exception;
}
