package components;

import correlator.HealthCorrelatorState;
import rules.RuleBase;
import rules.S01;
import rules.S02;
import rules.S03;

public class HealthCorrelator extends Correlator {

	protected HealthCorrelator(String uri, String stationURI) throws Exception {
		super(uri, stationURI, new HealthCorrelatorState(), new RuleBase());
		
		managementPort.subscribe(uri, stationURI);
		
		ruleBase.addRule(new S01());
		ruleBase.addRule(new S02());
		ruleBase.addRule(new S03());

		this.getTracer().setTitle(uri);
		this.getTracer().setRelativePosition(3, 0);
		this.toggleTracing();
		
	}

}
