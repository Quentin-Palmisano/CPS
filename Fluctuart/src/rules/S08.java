package rules;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

import correlator.HealthCorrelatorStateI;
import events.ComplexEvent;
import events.HealthEventName;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import fr.sorbonne_u.cps.smartcity.utils.TimeManager;
import interfaces.CorrelatorStateI;
import interfaces.EventBaseI;
import interfaces.EventI;
import interfaces.RuleI;

public class S08 extends S07{

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI e1 = null;
		EventI e2 = null;
		for (int i = 0 ; i < eb.numberOfEvents() && (e1==null || e2==null) ; i++) {
			EventI e = eb.getEvent(i);
			if (e.hasProperty("type") && e.getPropertyValue("type")==TypeOfHealthAlarm.TRACKING &&
					e.hasProperty("name") && e.getPropertyValue("name")==HealthEventName.HEALTH_ALARM) {
				e1 = e;
			}else if(e.hasProperty("name") && e.getPropertyValue("name")==HealthEventName.MANUAL_SIGNAL) {
				e2 = e;
			}
		}
		if (e1!=null && e2!=null) {
			ArrayList<EventI> matchedEvents = new ArrayList<>();
			matchedEvents.add(e1);
			matchedEvents.add(e2);
			return matchedEvents;
		} else {
			return null;
		}
	}

	
	
	@Override
	public boolean correlate(ArrayList<EventI> matchedEvents) {
		boolean found = false;
		String id = "";
		for(EventI e : matchedEvents) {
			if(e.hasProperty("personId")) {
				String tmp = (String) e.getPropertyValue("personId");
				if(id == "") {
					id = tmp;
				} else if(id==tmp) {
					found = true;
					break;
				}
			}
		}
		
		if(!found) return false;

		LocalTime t1 = TimeManager.get().getCurrentLocalTime();
		EventI e = matchedEvents.get(0);
		LocalTime t2 = e.getTimeStamp();
		Duration d = Duration.between(t2, t1);
		boolean duration = d.compareTo(Duration.ofMinutes(10))<0;

		return duration;
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		HealthCorrelatorStateI samuState = (HealthCorrelatorStateI)c;
		return !samuState.isMedicAvailable() && samuState.getNextStation(matchedEvents.get(0))!=null;
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		c.traceRuleTrigger("S08");
		HealthCorrelatorStateI samuState = (HealthCorrelatorStateI)c;
		EventI e = matchedEvents.get(0);
		if(e.hasProperty("stationId")) {
			samuState.propagateEvent(e, null, HealthEventName.CONSCIOUS_FALL);
		}
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		for(EventI e : matchedEvents) {
			eb.removeEvent(e);
		}
	}

}
