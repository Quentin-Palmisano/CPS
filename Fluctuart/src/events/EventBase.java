package events;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

import interfaces.EventBaseI;
import interfaces.EventI;

public class EventBase implements EventBaseI {
	
	protected ArrayList<EventI> events;

	public EventBase() {
		this.events = new ArrayList<EventI>();
	}

	@Override
	public void addEvent(EventI e) {
		events.add(e);
	}
	
	@Override
	public void addEvents(EventI[] events) {
		for(EventI e : events) {
			addEvent(e);
		}
	}

	@Override
	public void removeEvent(EventI e) {
		events.remove(e);
	}

	@Override
	public EventI getEvent(int i) {
		return events.get(i);
	}

	@Override
	public ArrayList<EventI> getEvents() {
		return events;
	}

	@Override
	public int numberOfEvents() {
		return events.size();
	}

	@Override
	public boolean appearsIn(EventI e) {
		return events.contains(e);
	}

	@Override
	public void clearEvents(Duration d) {
		ArrayList<EventI> toDelete = new ArrayList<>();
		for(EventI e : events) {
			Duration d1 = Duration.between(e.getTimeStamp(), LocalTime.now());
			if (d.compareTo(d1) >= 0) {
				toDelete.add(e);
			}
		}
		events.removeAll(toDelete);
	}

}
