package events;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;

import interfaces.AtomicEventI;

public class AtomicEvent extends Event implements AtomicEventI {
	
	private static final long serialVersionUID = 8860251323779443274L;
	
	protected HashMap<String, Serializable> property;

	public AtomicEvent(LocalTime time) {
		super(time);
		property = new HashMap<String, Serializable>();
	}
	
	public AtomicEvent(LocalTime time, String type) {
		this(time);
		this.putProperty("type", type);
	}

	@Override
	public boolean hasProperty(String name) {
		return property.containsKey(name);
	}

	@Override
	public Serializable getPropertyValue(String name) {
		return property.get(name);
	}

	@Override
	public Serializable putProperty(String name, Serializable value) {
		return this.property.put(name, value);
	}

	@Override
	public void removeProperty(String name) {
		this.property.remove(name);	
	}	

}
