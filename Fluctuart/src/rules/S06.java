package rules;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

import correlator.HealthCorrelatorStateI;
import events.HealthEventName;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.utils.TimeManager;
import interfaces.CorrelatorStateI;
import interfaces.EventBaseI;
import interfaces.EventI;

public class S06 extends S05 {

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI he = null;
		for (int i = 0 ; i < eb.numberOfEvents() && (he == null) ; i++) {
			EventI e = eb.getEvent(i);
			if (e.hasProperty("type") && e.getPropertyValue("type")==TypeOfHealthAlarm.TRACKING &&
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
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		LocalTime t1 = TimeManager.get().getCurrentLocalTime();
		HealthCorrelatorStateI samuState = (HealthCorrelatorStateI)c;
		EventI e = matchedEvents.get(0);
		LocalTime t2 = e.getTimeStamp();
		Duration d = Duration.between(t2, t1);
		boolean duration = d.compareTo(Duration.ofMinutes(10))>0;
		return !samuState.isMedicAvailable() && duration && samuState.getNextStation(matchedEvents.get(0))!=null;
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		c.traceRuleTrigger("S06");
		HealthCorrelatorStateI samuState = (HealthCorrelatorStateI)c;
		EventI e = matchedEvents.get(0);
		if(e.hasProperty("stationId")) {
			samuState.propagateEvent(e, TypeOfHealthAlarm.MEDICAL, HealthEventName.INTERVENTION_REQUEST);
		}
	}
	
	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		for(EventI e : matchedEvents) {
			eb.removeEvent(e);
		}
	}
	
}
