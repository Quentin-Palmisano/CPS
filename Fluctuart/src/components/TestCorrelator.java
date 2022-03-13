package components;

import classes.EventBase;
import classes.RuleBase;
import interfaces.CorrelatorStateI;
import interfaces.EventI;
import rules.S01;
import rules.S02;

public class TestCorrelator extends Correlator {
	
	private CorrelatorStateI state;

	protected TestCorrelator() throws Exception {
		super(new RuleBase());
		
		ruleBase.addRule(new S01());
		ruleBase.addRule(new S02());

		this.getTracer().setTitle("Test Correlator");
		this.getTracer().setRelativePosition(3, 0);
		this.toggleTracing();
		
	}

	@Override
	public void receiveEvent(String emitterURI, EventI event) throws Exception {
		EventBase eb = new EventBase();
		eb.addEvent(event);
		ruleBase.fireAllOn(eb, state);
	}

	@Override
	public void receiveEvents(String emitterURI, EventI[] events) throws Exception {
		EventBase eb = new EventBase();
		eb.addEvents(events);
		ruleBase.fireAllOn(eb, state);
	}

}
