package rules;

import java.util.ArrayList;

import correlator.FireCorrelatorStateI;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import interfaces.CorrelatorStateI;
import interfaces.EventBaseI;
import interfaces.EventI;
import interfaces.RuleI;

public class F01 implements RuleI {
	
	public F01() {
	}
	
	EventI matched;
	
	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI he = null;
		for (int i = 0 ; i < eb.numberOfEvents() && (he == null) ; i++) {
			EventI e = eb.getEvent(i);
			if (e.hasProperty("type") && e.getPropertyValue("type") == TypeOfFire.Building) {
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
		FireCorrelatorStateI samuState = (FireCorrelatorStateI)c;
		return samuState.isHighLadderTruckAvailable();
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		c.traceRuleTrigger("F01");
		FireCorrelatorStateI samuState = (FireCorrelatorStateI)c;
		samuState.triggerFirstAlarm((AbsolutePosition) matchedEvents.get(0).getPropertyValue("position"), TypeOfFirefightingResource.HighLadderTruck);
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		for(EventI e : matchedEvents) {
			eb.removeEvent(e);
		}
		//AtomicEvent f = new FirstFireSignal(LocalTime.now());
		//eb.addEvent(f);
	}

}
