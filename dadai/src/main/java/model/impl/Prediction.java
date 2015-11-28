package main.java.model.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Prediction {

	protected String predicter;
	protected List<String> orderedHorseRankPrediction = new ArrayList<String>();
	
	public Prediction(String predicter, List<String> orderedHorseRankPrediction) {
		this.predicter = predicter;
		this.orderedHorseRankPrediction = orderedHorseRankPrediction;
	}
	
	public void setPredicter(String predicter){
		this.predicter = predicter;
	}
}
