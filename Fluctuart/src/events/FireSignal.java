package events;

import java.time.LocalTime;

import classes.AtomicEvent;

public class FireSignal extends AtomicEvent {

	private static final long serialVersionUID = -6114245797064627941L;

	public FireSignal(LocalTime time) {
		super(time);
	}

}
