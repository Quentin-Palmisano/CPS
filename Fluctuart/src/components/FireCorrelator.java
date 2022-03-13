package components;

import correlator.FireCorrelatorState;
import rules.F01;
import rules.F02;
import rules.F03;
import rules.RuleBase;

public class FireCorrelator extends Correlator {
	
	public FireCorrelator(String uri, String stationURI) throws Exception {
		super(uri, stationURI, new FireCorrelatorState(), new RuleBase());
		
		managementPort.subscribe(uri, stationURI);
		
		ruleBase.addRule(new F01());
		ruleBase.addRule(new F02());
		ruleBase.addRule(new F03());

		this.getTracer().setTitle(uri);
		this.getTracer().setRelativePosition(3, 1);
		this.toggleTracing();
		
	}
	
}
