package rules;

import java.util.ArrayList;

import correlator.HealthCorrelatorStateI;
import events.HealthEventName;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import interfaces.CorrelatorStateI;
import interfaces.EventBaseI;
import interfaces.EventI;
import interfaces.RuleI;

public class S18 implements RuleI{

	public S18() {
	}
	

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI he = null;
		for (int i = 0 ; i < eb.numberOfEvents() && (he == null) ; i++) {
			EventI e = eb.getEvent(i);
			if (e.hasProperty("name") && e.getPropertyValue("name")==HealthEventName.NOTIFY_AMBULANCES_AVAILABLE) {
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
		c.traceRuleTrigger("S18");
		HealthCorrelatorStateI samuState = (HealthCorrelatorStateI)c;
		samuState.setAmbulancesAvailable();
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		for(EventI e : matchedEvents) {
			eb.removeEvent(e);
		}
	}

}
