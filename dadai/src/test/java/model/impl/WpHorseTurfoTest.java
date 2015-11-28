package test.java.model.impl;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.google.gson.JsonElement;

import main.java.model.impl.WpHorseTurfo;

public class WpHorseTurfoTest {
	Logger logger = Logger.getLogger("test.java.model.impl.WpHorseTurfoTest");

//	@Test
	public void testSerialize() throws MalformedURLException {
		URL url = new URL("http://TESTURL?id=666");
		Integer id = 666;
		String name = "ELEONOR";
		String age = "3";
		String sex = "Hongre";
		List<String> lastPerf = new ArrayList<String>();
		lastPerf.add("1p");
		lastPerf.add("2p");

		WpHorseTurfo wp = new WpHorseTurfo(url, id, name, age, sex, lastPerf);
		JsonElement jse = wp.serialize();

		logger.debug(jse.toString());
		assertTrue(jse.toString().length() > 0);
	}
}
