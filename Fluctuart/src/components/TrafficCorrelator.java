package components;

import correlator.TrafficCorrelatorState;
import rules.RuleBase;

public class TrafficCorrelator extends Correlator {
	
	public TrafficCorrelator(String uri, String stationURI) throws Exception {
		super(uri, stationURI, new TrafficCorrelatorState(), new RuleBase());
		
		managementPort.subscribe(uri, stationURI);

		this.getTracer().setTitle(uri);
		this.getTracer().setRelativePosition(3, 2);
		this.toggleTracing();
		
	}
}
