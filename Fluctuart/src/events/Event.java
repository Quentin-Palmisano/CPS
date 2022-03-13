package events;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;

import interfaces.EventI;

public abstract class Event implements EventI {
	
	private static final long serialVersionUID = 2219550166009750377L;
	
	protected LocalTime time;
	protected HashMap<String, Serializable> property;

	public Event(LocalTime time) {
		this.time = time;
		property = new HashMap<String, Serializable>();
	}

	@Override
	public LocalTime getTimeStamp() {
		return time;
	}

	@Override
	public boolean hasProperty(String name) {
		return property.containsKey(name);
	}

	@Override
	public Serializable getPropertyValue(String name) {
		return property.get(name);
	}

}
