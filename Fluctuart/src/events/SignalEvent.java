package events;

import java.time.LocalTime;

import classes.AtomicEvent;

public class SignalEvent extends AtomicEvent {

	private static final long serialVersionUID = -5795955436289734603L;

	public SignalEvent(LocalTime time) {
		super(time);
	}
	
	public SignalEvent(LocalTime time, String type) {
		super(time, type);
	}

}
