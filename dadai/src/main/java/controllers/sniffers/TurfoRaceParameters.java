package main.java.controllers.sniffers;


public class TurfoRaceParameters {

	private String raceUrlRegexp;
	private String mainRaceUrl;
	private String urlGetDateArg;

	/**
	 * 
	 */
	public TurfoRaceParameters() {
		this.raceUrlRegexp = "table tbody tr td a.btn[href]";
		this.mainRaceUrl = "http://www.turfomania.fr/arrivees-rapports/";
		this.urlGetDateArg = "choixdate";
	}
	
	/**
	 * 
	 * @return
	 */
	public String getRaceUrlRegexp(){
		return this.raceUrlRegexp;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getMainRaceUrl(){
		return this.mainRaceUrl;
	}

	/**
	 * 
	 * @return
	 */
	public String getUrlGetDateArg() {
		// TODO Auto-generated method stub
		return this.urlGetDateArg;
	}

}
