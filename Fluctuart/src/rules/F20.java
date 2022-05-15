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
import fr.sorbonne_u.cps.smartcity.utils.TimeManager;
import interfaces.CorrelatorStateI;
import interfaces.EventBaseI;
import interfaces.EventI;
import interfaces.RuleI;

public class F20 implements RuleI {
	
	public F20() {
	}
	
	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI e1 = null;
		EventI e2 = null;
		for (int i = 0 ; i < eb.numberOfEvents() && (e1==null || e2==null) ; i++) {
			EventI e = eb.getEvent(i);
			if (e.hasProperty("name") && e.getPropertyValue("name")==FireEventName.SECOND_FIRE_ALARM) {
				e1 = e;
			}else if(e.hasProperty("name") && e.getPropertyValue("name")==FireEventName.END_OF_FIRE) {
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
		AbsolutePosition p = null;
		AbsolutePosition tmp = null;
		for(EventI e : matchedEvents) {
			if(e.hasProperty("position")) tmp = (AbsolutePosition) e.getPropertyValue("position");
			if(p==null) {
				p=tmp;				
			}else if(p.distance(tmp)!=0) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		return true;
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		c.traceRuleTrigger("F20");
		FireCorrelatorStateI fireState = (FireCorrelatorStateI)c;
		AtomicEvent e = new AtomicEvent(TimeManager.get().getCurrentLocalTime());
		e.putProperty("name", FireEventName.END_OF_FIRE);
		e.putProperty("position", matchedEvents.get(0).getPropertyValue("position"));
		fireState.propagateEventToAllStation(e, null);
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		for(EventI e : matchedEvents) {
			eb.removeEvent(e);
		}
	}

}
