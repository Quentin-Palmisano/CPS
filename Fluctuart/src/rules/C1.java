package rules;

import java.util.ArrayList;

import interfaces.CorrelatorStateI;
import interfaces.EventBaseI;
import interfaces.EventI;
import interfaces.RuleI;

public class C1 implements RuleI {

	@Override
	public ArrayList<EventI> match(EventBaseI eb) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean correlate(ArrayList<EventI> matchedEvents) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		// TODO Auto-generated method stub
		
	}

	

}
