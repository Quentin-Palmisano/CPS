package rules;

import java.util.ArrayList;

import correlator.HealthCorrelatorStateI;
import interfaces.CorrelatorStateI;
import interfaces.EventI;

public class S2 extends S1 {

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		HealthCorrelatorStateI samuState = (HealthCorrelatorStateI)c;
		return samuState.isAmbulanceAvailable();
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		HealthCorrelatorStateI samuState = (HealthCorrelatorStateI)c;
		samuState.callAmbulance();
	}
	
}
