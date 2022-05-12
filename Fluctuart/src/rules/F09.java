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

public class F09 implements RuleI {

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI e1 = null;
		EventI e2 = null;
		for (int i = 0 ; i < eb.numberOfEvents() && (e1==null || e2==null) ; i++) {
			EventI event = eb.getEvent(i);
			if(event instanceof ComplexEvent) {
				ComplexEvent e = (ComplexEvent) event;
				if (e.hasProperty("type") && e.getPropertyValue("type")==TypeOfFire.Building &&
					e.hasProperty("name") && e.getPropertyValue("name")==FireEventName.FIRST_FIRE_ALARM) {
					e1 = e;
				}else if(e.hasProperty("type") && e.getPropertyValue("type")==TypeOfFire.Building &&
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
		Duration d = Duration.between(t1, t2);
		Duration x = d.minus(Duration.ofMinutes(15));
		
		return x.isNegative();
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		FireCorrelatorStateI fireState = (FireCorrelatorStateI)c;
		return fireState.isStandardTruckAvailable();
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		c.traceRuleTrigger("F09");
		FireCorrelatorStateI fireState = (FireCorrelatorStateI)c;
		fireState.triggerGeneralAlarm((AbsolutePosition) matchedEvents.get(0).getPropertyValue("position"));
		//pas fini
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		//pas fini
	}

}