package rules;

import java.util.ArrayList;

import correlator.TrafficCorrelatorStateI;
import events.AtomicEvent;
import events.TrafficLightEventName;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import interfaces.CorrelatorStateI;
import interfaces.EventBaseI;
import interfaces.EventI;
import interfaces.RuleI;

public class C01 implements RuleI {

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI he = null;
		for (int i = 0 ; i < eb.numberOfEvents() && (he == null) ; i++) {
			EventI e = eb.getEvent(i);
			if (e.hasProperty("name") && e.getPropertyValue("name") == TrafficLightEventName.REQUEST_PRIORITY) {
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
		return true;
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		c.traceRuleTrigger("C01");
		TrafficCorrelatorStateI trafficState = (TrafficCorrelatorStateI)c;
		AtomicEvent e = (AtomicEvent) matchedEvents.get(0);
		trafficState.changePriority((TypeOfTrafficLightPriority) e.getPropertyValue("priority"));
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		((AtomicEvent) matchedEvents.get(0)).putProperty("name", TrafficLightEventName.WAIT_FOR_VEHICLE_PASSAGE);
	}

	

}
