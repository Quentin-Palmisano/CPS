package events;

import java.time.LocalTime;

import interfaces.EventI;

public abstract class Event implements EventI {
	
	private static final long serialVersionUID = 2219550166009750377L;
	
	protected LocalTime time;
	
	public Event(LocalTime time) {
		this.time = time;
	}
	
	@Override
	public LocalTime getTimeStamp() {
		return time;
	}

}
