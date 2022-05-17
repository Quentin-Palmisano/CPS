package rules;

import java.util.ArrayList;

import correlator.TrafficCorrelatorStateI;
import events.AtomicEvent;
import events.ComplexEvent;
import events.FireEventName;
import events.TrafficLightEventName;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.Direction;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import interfaces.CorrelatorStateI;
import interfaces.EventBaseI;
import interfaces.EventI;
import interfaces.RuleI;

public class C02 implements RuleI {

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI e1 = null;
		EventI e2 = null;
		for (int i = 0 ; i < eb.numberOfEvents() && (e1==null || e2==null) ; i++) {
			EventI e = eb.getEvent(i);
				if (e.hasProperty("name") && e.getPropertyValue("name")==TrafficLightEventName.WAIT_FOR_VEHICLE_PASSAGE) {
					e1 = e;
				}else if(e.hasProperty("name") && e.getPropertyValue("name")==TrafficLightEventName.VEHICLE_PASSAGE) {
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
		String id = "";
		String tmp = null;
		for(EventI e : matchedEvents) {
			if(e.hasProperty("vehicleId")) tmp = (String) e.getPropertyValue("vehicleId");
			if(id=="") {
				id=tmp;				
			}else if(id!=tmp) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		TrafficCorrelatorStateI trafficState = (TrafficCorrelatorStateI)c;
		AtomicEvent v1 = null;
		AtomicEvent v2 = null;
		for(EventI i : matchedEvents) {
			if(i.getPropertyValue("name")==TrafficLightEventName.WAIT_FOR_VEHICLE_PASSAGE) v1 = (AtomicEvent) i;
			if(i.getPropertyValue("name")==TrafficLightEventName.VEHICLE_PASSAGE) v2 = (AtomicEvent) i;
		}
		AbsolutePosition df = (AbsolutePosition) v1.getPropertyValue("destination");
		Direction d = (Direction) v2.getPropertyValue("direction");
		IntersectionPosition i = (IntersectionPosition) v1.getPropertyValue("intersection");
		IntersectionPosition nexti = i.next(d);
		AbsolutePosition pi = new AbsolutePosition(nexti.getX(), nexti.getY());
		if(pi.equalAbsolutePosition(df)) return false;
		
		
		return true;
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		c.traceRuleTrigger("C02");
		
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		for(EventI e : matchedEvents) {
			eb.removeEvent(e);
		}
	}

	

}
