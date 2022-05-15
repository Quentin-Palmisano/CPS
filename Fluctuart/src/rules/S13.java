package rules;

import java.util.ArrayList;

import correlator.HealthCorrelatorState;
import correlator.HealthCorrelatorStateI;
import events.HealthEventName;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import interfaces.CorrelatorStateI;
import interfaces.EventBaseI;
import interfaces.EventI;
import interfaces.RuleI;

public class S13 implements RuleI {

	public S13() {
	}
	

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI he = null;
		for (int i = 0 ; i < eb.numberOfEvents() && (he == null) ; i++) {
			EventI e = eb.getEvent(i);
			if (e.hasProperty("name") && e.getPropertyValue("name")==HealthEventName.CONSCIOUS_FALL) {
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
		if(!matchedEvents.get(0).getPropertyValue("stationId").equals(((HealthCorrelatorState) c).stationId)) return false;
		HealthCorrelatorStateI samuState = (HealthCorrelatorStateI)c;
		return samuState.isMedicAvailable();
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		c.traceRuleTrigger("S13");
		HealthCorrelatorStateI samuState = (HealthCorrelatorStateI)c;
		EventI e = matchedEvents.get(0);
		AbsolutePosition p = (AbsolutePosition) e.getPropertyValue("position");
		String s = (String) e.getPropertyValue("personId");
		TypeOfSAMURessources t = TypeOfSAMURessources.TELEMEDIC;
		samuState.callMedic(p, s, t);
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		for(EventI e : matchedEvents) {
			eb.removeEvent(e);
		}
	}

}
