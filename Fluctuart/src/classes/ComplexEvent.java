package classes;

import java.util.ArrayList;

import interfaces.ComplexEventI;
import interfaces.EventI;

public abstract class ComplexEvent extends Event implements ComplexEventI {
	
	protected ArrayList<EventI> events;
	
	public ComplexEvent() {
		super();
		this.events = new ArrayList<EventI>();
	}

	public ArrayList<EventI> getCorrelatedEvents(){
		return events;
	}
	
}
