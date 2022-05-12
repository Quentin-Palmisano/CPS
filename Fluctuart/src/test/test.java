package test;

import java.time.Duration;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import correlator.HealthCorrelatorStateI;
import events.AtomicEvent;
import events.EventBase;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import rules.RuleBase;

class test {

	@Test
	void healthEventTest() {
		AtomicEvent hp = new AtomicEvent(LocalTime.now());
		hp.putProperty("type", "emergency");
		assert ((String)hp.getPropertyValue("type")).equals("emergency");
	}
	
	@Test
	void clearEventTest() {
		EventBase eb = new EventBase();
		eb.addEvent(new AtomicEvent(LocalTime.now().minusMinutes(10)));
		eb.addEvent(new AtomicEvent(LocalTime.now()));
		eb.clearEvents(Duration.ofMinutes(5));
		assert (eb.numberOfEvents() == 1);
	}
	
}
