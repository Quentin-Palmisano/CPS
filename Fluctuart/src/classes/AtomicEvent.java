package classes;

import java.io.Serializable;
import java.time.LocalTime;

import interfaces.AtomicEventI;

public abstract class AtomicEvent extends Event implements AtomicEventI {
	
	public AtomicEvent(LocalTime time) {
		super(time);
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
