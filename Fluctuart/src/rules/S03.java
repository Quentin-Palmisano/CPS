package rules;

import java.util.ArrayList;

import correlator.HealthCorrelatorStateI;
import events.HealthEvent;
import interfaces.CorrelatorStateI;
import interfaces.EventBaseI;
import interfaces.EventI;
import interfaces.RuleI;

public class S03 implements RuleI {

	public S03() {
	}
	

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		
		return null;
	}

	@Override
	public boolean correlate(ArrayList<EventI> matchedEvents) {
		
		return 	true;
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		
		return true;
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		
	}

}
