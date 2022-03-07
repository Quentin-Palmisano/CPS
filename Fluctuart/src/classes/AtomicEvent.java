package classes;

import java.io.Serializable;
import java.time.LocalTime;

import interfaces.AtomicEventI;

public abstract class AtomicEvent extends Event implements AtomicEventI {
	
	private static final long serialVersionUID = 8860251323779443274L;

	public AtomicEvent(LocalTime time) {
		super(time);
	}
	
	public AtomicEvent(LocalTime time, String type) {
		super(time);
		this.putProperty("type", type);
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
