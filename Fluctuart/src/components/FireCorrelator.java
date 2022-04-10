package components;

import components.interfaces.ActionExecutionCI;
import components.interfaces.CEPBusManagementCI;
import components.interfaces.EventEmissionCI;
import components.interfaces.EventReceptionCI;
import correlator.FireCorrelatorState;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import rules.F01;
import rules.F02;
import rules.F03;
import rules.RuleBase;

@OfferedInterfaces(offered={EventReceptionCI.class})
@RequiredInterfaces(required={ActionExecutionCI.class, EventEmissionCI.class, CEPBusManagementCI.class})
public class FireCorrelator extends Correlator {

	private String stationURI;
	
	protected FireCorrelator(String uri, String stationURI) throws Exception {
		super(uri, stationURI, new FireCorrelatorState(), new RuleBase());
		this.stationURI = stationURI;
		
		ruleBase.addRule(new F01());
		ruleBase.addRule(new F02());
		ruleBase.addRule(new F03());

		this.getTracer().setTitle(uri);
		this.getTracer().setRelativePosition(3, 1);
		this.toggleTracing();
		
	}
	
	@Override
	public synchronized void	execute() throws Exception
	{
		super.execute();

		managementPort.subscribe(uri, stationURI);
	}
	
}
