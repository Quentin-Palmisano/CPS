package components;

import classes.RuleBase;
import correlator.HealthCorrelatorState;
import rules.S01;
import rules.S02;

public class TestCorrelator extends Correlator {

	protected TestCorrelator(String executorURI) throws Exception {
		super("Test Correlator", executorURI, new HealthCorrelatorState(), new RuleBase());
		
		ruleBase.addRule(new S01());
		ruleBase.addRule(new S02());

		this.getTracer().setTitle("Test Correlator");
		this.getTracer().setRelativePosition(3, 0);
		this.toggleTracing();
		
	}

}
