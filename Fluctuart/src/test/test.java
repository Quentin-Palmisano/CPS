package test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.text.RuleBasedCollator;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Calendar;

import org.junit.jupiter.api.Test;

import classes.EventBase;
import classes.RuleBase;
import correlator.HealthCorrelatorStateI;
import events.HealthEvent;
import events.SignalEvent;

class test {

	@Test
	void test() {
		HealthEvent hp = new HealthEvent(LocalTime.now());
		hp.putProperty("type", "emergency");
		assert ((String)hp.getPropertyValue("type")).equals("emergency");
	}
	
	@Test
	void clear() {
		EventBase eb = new EventBase();
		eb.addEvent(new HealthEvent(LocalTime.now().minusMinutes(10)));
		eb.addEvent(new SignalEvent(LocalTime.now()));
		eb.clearEvents(Duration.ofMinutes(5));
		assert (eb.numberOfEvents() == 1);
	}
	
	boolean called;
	
	@Test
	void S1() {
		
		RuleBase rulebase = new RuleBase();
		rulebase.addRule(new rules.S1());
		
		EventBase eventbase = new EventBase();
		HealthEvent hp = new HealthEvent(LocalTime.now());
		hp.putProperty("type", "emergency");
		eventbase.addEvent(hp);
		
		called = false;
		HealthCorrelatorStateI c = new HealthCorrelatorStateI() {
			
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
