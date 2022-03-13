package rules;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

import correlator.HealthCorrelatorStateI;
import interfaces.CorrelatorStateI;
import interfaces.EventBaseI;
import interfaces.EventI;

public class S06 extends S05 {

	@Override
	public boolean filter(ArrayList<EventI> matchedEvents, CorrelatorStateI c) {
		LocalTime t1 = LocalTime.now();
		HealthCorrelatorStateI samuState = (HealthCorrelatorStateI)c;
		EventI e = matchedEvents.get(0);
		LocalTime t2 = e.getTimeStamp();
		Duration d = Duration.between(t1, t2);
		Duration x = d.minus(Duration.ofMinutes(10));
		return !samuState.isMedicAvailable() && !x.isNegative();
	}

	@Override
	public void act(ArrayList<EventI> matchedEvents, CorrelatorStateI c) throws Exception {
		c.traceRuleTrigger("S06");
		// propagate
	}
	
	@Override
	public void update(ArrayList<EventI> matchedEvents, EventBaseI eb) {
		for(EventI e : matchedEvents) {
			eb.removeEvent(e);
		}
	}
	
}
