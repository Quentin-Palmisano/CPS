package rules;

import java.util.ArrayList;

import correlator.HealthCorrelatorStateI;
import events.HealthEventName;
import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import interfaces.CorrelatorStateI;
import interfaces.EventBaseI;
import interfaces.EventI;
import interfaces.RuleI;

public class S01 implements RuleI {

	public S01() {
	}
	

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI he = null;
		for (int i = 0 ; i < eb.numberOfEvents() && (he == null) ; i++) {
			EventI e = eb.getEvent(i);
			if (e.hasProperty("type") && e.getPropertyValue("type")==TypeOfHealthAlarm.EMERGENCY && 
				e.hasProperty("name") && e.getPropertyValue("name")==HealthEventName.HEALTH_ALARM) {
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
		HealthCorrelatorStateI samuState = (HealthCorrelatorStateI)c;
		
		//Test si la position de l'event est dans la zone d'action de sa station.
		boolean b = SmartCityDescriptor.dependsUpon((AbsolutePosition) matchedEvents.get(0).getPropertyValue("position"),
				(String) matchedEvents.get(0).getPropertyValue("stationId"));
		
		return samuState.isAmbulanceAvailable() && b;
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		c.traceRuleTrigger("S01");
		HealthCorrelatorStateI samuState = (HealthCorrelatorStateI)c;
		EventI e = matchedEvents.get(0);
		AbsolutePosition p = (AbsolutePosition) e.getPropertyValue("position");
		String s = (String) e.getPropertyValue("personId");
		TypeOfSAMURessources t = TypeOfSAMURessources.AMBULANCE;
		samuState.triggerIntervention(p, s, t);
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		for(EventI e : matchedEvents) {
			eb.removeEvent(e);
		}
	}

}
