package components;

import components.interfaces.ActionExecutionCI;
import components.interfaces.CEPBusManagementCI;
import components.interfaces.EventEmissionCI;
import components.interfaces.EventReceptionCI;
import connectors.ActionExecutionConnector;
import connectors.EventEmissionConnector;
import correlator.HealthCorrelatorState;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import rules.*;
@OfferedInterfaces(offered={EventReceptionCI.class})
@RequiredInterfaces(required={ActionExecutionCI.class, EventEmissionCI.class, CEPBusManagementCI.class})
public class HealthCorrelator extends Correlator {

	private String stationURI;
	
	protected HealthCorrelator(String uri, String stationURI) throws Exception {
		super(uri, stationURI, new HealthCorrelatorState(stationURI), new RuleBase());
		this.stationURI = stationURI;
		
		ruleBase.addRule(new S01());
		ruleBase.addRule(new S02());
		ruleBase.addRule(new S03());
		ruleBase.addRule(new S04());
		ruleBase.addRule(new S05());
		ruleBase.addRule(new S06());
		ruleBase.addRule(new S07());
		ruleBase.addRule(new S08());
		ruleBase.addRule(new S09());
		ruleBase.addRule(new S10());

		this.getTracer().setTitle(uri);
		this.getTracer().setRelativePosition(3, 0);
		this.toggleTracing();
		
	}
	
	@Override
	public synchronized void	execute() throws Exception
	{
		super.execute();

		managementPort.subscribe(uri, stationURI);
	}

}
