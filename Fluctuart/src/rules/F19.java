package rules;

import java.util.ArrayList;

import correlator.FireCorrelatorStateI;
import events.AtomicEvent;
import events.FireEventName;
import events.HealthEventName;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import interfaces.CorrelatorStateI;
import interfaces.EventBaseI;
import interfaces.EventI;
import interfaces.RuleI;

public class F19 implements RuleI {
	
	public F19() {
	}
	
	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI he = null;
		for (int i = 0 ; i < eb.numberOfEvents() && (he == null) ; i++) {
			EventI e = eb.getEvent(i);
			if (e.hasProperty("type") && e.getPropertyValue("type") == TypeOfFire.Building && 
				e.hasProperty("name") && e.getPropertyValue("name") == FireEventName.FIRE_ALARM) {
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
		return fireState.isHighLadderTruckAvailable();
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		c.traceRuleTrigger("F01");
		FireCorrelatorStateI fireState = (FireCorrelatorStateI)c;
		fireState.triggerFirstAlarm((AbsolutePosition) matchedEvents.get(0).getPropertyValue("position"), TypeOfFirefightingResource.HighLadderTruck);
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		((AtomicEvent) matchedEvents.get(0)).putProperty("name", FireEventName.FIRST_FIRE_ALARM);
	}

}
