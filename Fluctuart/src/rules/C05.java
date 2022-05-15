package rules;

import java.util.ArrayList;

import correlator.TrafficCorrelatorStateI;
import interfaces.CorrelatorStateI;
import interfaces.EventBaseI;
import interfaces.EventI;
import interfaces.RuleI;

public class C05 implements RuleI {

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		return null;
	}

	@Override
	public boolean correlate(ArrayList<EventI> matchedEvents) {
		return false;
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		return false;
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		c.traceRuleTrigger("C05");
		TrafficCorrelatorStateI trafficState = (TrafficCorrelatorStateI)c;
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
	}

	

}
