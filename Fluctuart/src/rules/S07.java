package rules;

import java.util.ArrayList;

import events.ComplexEvent;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import interfaces.CorrelatorStateI;
import interfaces.EventBaseI;
import interfaces.EventI;
import interfaces.RuleI;

public class S07 implements RuleI{

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		EventI he = null;
		for (int i = 0 ; i < eb.numberOfEvents() && (he == null) ; i++) {
			EventI event = eb.getEvent(i);
			if(event instanceof ComplexEvent) {
				ComplexEvent e = (ComplexEvent) event;
				if (e.hasProperty("type") && (e.getPropertyValue("type")==TypeOfHealthAlarm.TRACKING)) {
					he = e;
				}
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
	
	//A TERMINER

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		return false;
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
	}

}
