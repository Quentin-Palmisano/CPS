package classes;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;

import interfaces.EventI;

public abstract class Event implements EventI {
	
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
