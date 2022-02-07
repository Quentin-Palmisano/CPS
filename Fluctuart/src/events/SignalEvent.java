package events;

import java.time.LocalTime;

import classes.AtomicEvent;

public class SignalEvent extends AtomicEvent {

	public SignalEvent(LocalTime time) {
		super(time);
	}

}
