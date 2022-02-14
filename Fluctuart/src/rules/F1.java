package rules;

import java.time.LocalTime;
import java.util.ArrayList;

import classes.*;
import correlator.*;
import events.*;
import interfaces.*;

public class F1 implements RuleI {
	
	public F1() {
	}
	
	EventI matched;
	
	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI he = null;
		for (int i = 0 ; i < eb.numberOfEvents() && (he == null) ; i++) {
			EventI e = eb.getEvent(i);
			if (e instanceof HealthEvent && e.hasProperty("type")
					&& ((String)e.getPropertyValue("type")).equals("building")) {
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
		return true;
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		Fire1CorrelatorStateI samuState = (Fire1CorrelatorStateI)c;
		return samuState.isBigLadderAvailable();
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		Fire1CorrelatorStateI samuState = (Fire1CorrelatorStateI)c;
		samuState.triggerAlarm();
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		for(EventI e : matchedEvents) {
			eb.removeEvent(e);
		}
		FirstFireSignal f = new FirstFireSignal(LocalTime.now());
		eb.addEvent(f);
	}

}
