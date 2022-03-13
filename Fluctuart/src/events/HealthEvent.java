package events;

import java.time.LocalTime;

import classes.AtomicEvent;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;

public class HealthEvent extends AtomicEvent {

	private static final long serialVersionUID = -9114519327196856107L;
	
	public HealthEvent(LocalTime time) {
		super(time);
	}
	

}
