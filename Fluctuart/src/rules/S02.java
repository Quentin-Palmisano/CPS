package rules;

import java.util.ArrayList;

import correlator.*;
import interfaces.*;

public class S02 extends S01 {

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		Health2CorrelatorStateI samuState = (Health2CorrelatorStateI)c;
		return samuState.isAmbulanceAvailable();
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		Health2CorrelatorStateI samuState = (Health2CorrelatorStateI)c;
		samuState.spreadEvent();
	}
	
}
