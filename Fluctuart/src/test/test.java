package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import events.HealthEvent;

class test {

	@Test
	void test() {
		HealthEvent hp = new HealthEvent();
		hp.putProperty("type", "emergency");
		assert ((String)hp.getPropertyValue("type")).equals("emergency");
	}

}
