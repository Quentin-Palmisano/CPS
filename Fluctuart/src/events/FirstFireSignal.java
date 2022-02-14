package events;

import java.time.LocalTime;

import classes.ComplexEvent;

public class FirstFireSignal extends ComplexEvent {

	private static final long serialVersionUID = 1190015518954869223L;

	public FirstFireSignal(LocalTime time) {
		super(time);
	}

}
