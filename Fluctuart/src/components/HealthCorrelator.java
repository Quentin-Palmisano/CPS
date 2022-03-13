package components;

import classes.RuleBase;
import correlator.HealthCorrelatorState;
import rules.S01;
import rules.S02;

public class HealthCorrelator extends Correlator {

	protected HealthCorrelator(String uri, String stationURI) throws Exception {
		super(uri, stationURI, new HealthCorrelatorState(), new RuleBase());
		
		managementPort.subscribe(uri, stationURI);
		
		ruleBase.addRule(new S01());
		ruleBase.addRule(new S02());

		this.getTracer().setTitle(uri);
		this.getTracer().setRelativePosition(3, 0);
		this.toggleTracing();
		
	}

}
