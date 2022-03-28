package components;

import components.interfaces.ActionExecutionCI;
import components.interfaces.CEPBusManagementCI;
import components.interfaces.EventEmissionCI;
import components.interfaces.EventReceptionCI;
import correlator.TrafficCorrelatorState;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import rules.RuleBase;

@OfferedInterfaces(offered={EventReceptionCI.class})
@RequiredInterfaces(required={ActionExecutionCI.class, EventEmissionCI.class, CEPBusManagementCI.class})
public class TrafficCorrelator extends Correlator {

	private String stationURI;
	
	protected TrafficCorrelator(String uri, String stationURI) throws Exception {
		super(uri, stationURI, new TrafficCorrelatorState(), new RuleBase());
		
		this.stationURI = stationURI;

		this.getTracer().setTitle(uri);
		this.getTracer().setRelativePosition(3, 2);
		this.toggleTracing();
		
	}
	
	@Override
	public synchronized void	execute() throws Exception
	{
		super.execute();
		
		System.out.println("trafficcorrelator");

		managementPort.subscribe(uri, stationURI);
	}
}
