package rules;

import java.util.ArrayList;

import correlator.HealthCorrelatorStateI;
import interfaces.CorrelatorStateI;
import interfaces.EventI;

public class S04 extends S03 {

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		HealthCorrelatorStateI samuState = (HealthCorrelatorStateI)c;
		return !samuState.isMedicAvailable();
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		c.traceRuleTrigger("S04");
		// propagate
	}
	
}
