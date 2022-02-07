package events;

import java.time.LocalTime;

import classes.AtomicEvent;

public class HealthEvent extends AtomicEvent {

	public HealthEvent(LocalTime time) {
		super(time);
	}

}
