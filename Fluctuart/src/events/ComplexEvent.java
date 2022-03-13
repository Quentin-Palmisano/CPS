package events;

import java.time.LocalTime;
import java.util.ArrayList;

import interfaces.ComplexEventI;
import interfaces.EventI;

public class ComplexEvent extends Event implements ComplexEventI {

	private static final long serialVersionUID = -853547081491829273L;

	protected ArrayList<EventI> events;

	public ComplexEvent(LocalTime time) {
		super(time);
		this.events = new ArrayList<EventI>();
	}

	public void addEvent(EventI e) {
		this.events.add(e);
	}

	public void addEvent(ArrayList<EventI> events) {
		for(EventI e : events) {
			this.events.add(e);
		}
	}

	public ArrayList<EventI> getCorrelatedEvents(){
		return events;
	}

}
