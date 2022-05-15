package rules;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

import correlator.FireCorrelatorStateI;
import events.AtomicEvent;
import events.ComplexEvent;
import events.FireEventName;
import events.HealthEventName;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.utils.TimeManager;
import interfaces.CorrelatorStateI;
import interfaces.EventBaseI;
import interfaces.EventI;
import interfaces.RuleI;

public class F12 implements RuleI {

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI e1 = null;
		EventI e2 = null;
		for (int i = 0 ; i < eb.numberOfEvents() && (e1==null || e2==null) ; i++) {
			EventI event = eb.getEvent(i);
			if(event instanceof ComplexEvent) {
				ComplexEvent e = (ComplexEvent) event;
				if (e.hasProperty("type") && e.getPropertyValue("type")==TypeOfFire.House &&
					e.hasProperty("name") && e.getPropertyValue("name")==FireEventName.FIRST_FIRE_ALARM) {
					e1 = e;
				}else if(e.hasProperty("type") && e.getPropertyValue("type")==TypeOfFire.House &&
						e.hasProperty("name") && e.getPropertyValue("name")==FireEventName.FIRE_ALARM) {
					e2 = e;
				}
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
		AbsolutePosition p = null;
		AbsolutePosition tmp = null;
		for(EventI e : matchedEvents) {
			if(e.hasProperty("position")) tmp = (AbsolutePosition) e.getPropertyValue("position");
			if(p==null) {
				p=tmp;				
			}else if(p!=tmp) {
				return false;
			}
		}
		
		LocalTime t1 = TimeManager.get().getCurrentLocalTime();
		EventI e = matchedEvents.get(0);
		LocalTime t2 = e.getTimeStamp();
		Duration d = Duration.between(t2, t1);
		boolean duration = d.compareTo(Duration.ofMinutes(15))<0;

		return duration;
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		FireCorrelatorStateI fireState = (FireCorrelatorStateI)c;
		return !fireState.isStandardTruckAvailable() && fireState.getNextStation(matchedEvents.get(0))!=null;
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		c.traceRuleTrigger("F12");
		FireCorrelatorStateI fireState = (FireCorrelatorStateI)c;
		EventI e = matchedEvents.get(0);
		if(e.hasProperty("stationId")) {
			fireState.propagateEvent(e, FireEventName.SECOND_FIRE_ALARM);
		}
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		for(EventI e : matchedEvents) {
			AtomicEvent event = (AtomicEvent) e;
			if(event.getPropertyValue("name")==FireEventName.FIRST_FIRE_ALARM) {
				event.putProperty("name", FireEventName.SECOND_FIRE_ALARM);
			}
		}
	}

}
