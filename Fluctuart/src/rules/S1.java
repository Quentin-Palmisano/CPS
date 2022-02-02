package rules;

import java.util.ArrayList;

import events.HealthEvent;
import interfaces.CorrelatorStateI;
import interfaces.EventBaseI;
import interfaces.EventI;
import interfaces.RuleI;

public class S1 implements RuleI {

	public S1() {
	}

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI he = null;
		for (int i = 0 ; i < eb.numberOfEvents() && (he == null) ; i++) {
			EventI e = eb.getEvent(i);
			if (e instanceof HealthEvent && e.hasProperty("type")
					&& ((String)e.getPropertyValue("type")).equals("emergency")) {
				he = e;
			}
		}		
		if (he != null) {
			ArrayList<EventI> matchedEvents = new ArrayList<>();
			matchedEvents.add(he);
			return matchedEvents;
		} else {
			return null;
		}
	}

	@Override
	public boolean correlate(ArrayList<EventI> matchedEvents) {
		return 	true;
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		// TODO Auto-generated method stub

	}

}
