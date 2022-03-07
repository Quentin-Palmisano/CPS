package events;

import java.time.LocalTime;

import classes.AtomicEvent;

public class HealthEvent extends AtomicEvent {

	private static final long serialVersionUID = -9114519327196856107L;

	public HealthEvent(LocalTime time) {
		super(time);
	}
	
	public HealthEvent(LocalTime time, String type) {
		super(time, type);
	}
	

}
