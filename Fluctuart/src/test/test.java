package test;

import java.time.Duration;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import classes.EventBase;
import classes.RuleBase;
import components.CEPBus;
import components.interfaces.CEPBusManagementCI;
import correlator.Health1CorrelatorStateI;
import events.HealthEvent;
import events.SignalEvent;

class test {

	@Test
	void healthEventTest() {
		HealthEvent hp = new HealthEvent(LocalTime.now());
		hp.putProperty("type", "emergency");
		assert ((String)hp.getPropertyValue("type")).equals("emergency");
	}
	
	@Test
	void clearEventTest() {
		EventBase eb = new EventBase();
		eb.addEvent(new HealthEvent(LocalTime.now().minusMinutes(10)));
		eb.addEvent(new SignalEvent(LocalTime.now()));
		eb.clearEvents(Duration.ofMinutes(5));
		assert (eb.numberOfEvents() == 1);
	}
	
	boolean called;
	
	@Test
	void S1Test() {
		
		RuleBase rulebase = new RuleBase();
		rulebase.addRule(new rules.S01());
		
		EventBase eventbase = new EventBase();
		HealthEvent hp = new HealthEvent(LocalTime.now());
		hp.putProperty("type", "emergency");
		eventbase.addEvent(hp);
		
		called = false;
		Health1CorrelatorStateI c = new Health1CorrelatorStateI() {
			
			@Override
			public boolean isAmbulanceAvailable() {
				return true;
			}
			
			@Override
			public void callAmbulance() {
				called = true;
			}
		};
		
		rulebase.fireAllOn(eventbase, c);
		
		assert (called);
		
	}
	
}
