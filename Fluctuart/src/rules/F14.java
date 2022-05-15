package rules;

import java.util.ArrayList;

import correlator.FireCorrelatorStateI;
import events.AtomicEvent;
import events.FireEventName;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import interfaces.CorrelatorStateI;
import interfaces.EventBaseI;
import interfaces.EventI;
import interfaces.RuleI;

public class F14 implements RuleI {

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI he = null;
		for (int i = 0 ; i < eb.numberOfEvents() && (he == null) ; i++) {
			EventI e = eb.getEvent(i);
			if (e.hasProperty("name") && e.getPropertyValue("name") == FireEventName.SECOND_FIRE_ALARM) {
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
		FireCorrelatorStateI fireState = (FireCorrelatorStateI)c;
		return !fireState.isStandardTruckAvailable() && fireState.getNextStation(matchedEvents.get(0))!=null;
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		c.traceRuleTrigger("F14");
		FireCorrelatorStateI fireState = (FireCorrelatorStateI)c;
		EventI e = matchedEvents.get(0);
		if(e.hasProperty("stationId")) {
			fireState.propagateEvent(e, null);
		}
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		for(EventI e : matchedEvents) {
			eb.removeEvent(e);
		}
	}

}
