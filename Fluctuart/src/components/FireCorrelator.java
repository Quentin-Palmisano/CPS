package components;

import components.interfaces.ActionExecutionCI;
import components.interfaces.CEPBusManagementCI;
import components.interfaces.EventEmissionCI;
import components.interfaces.EventReceptionCI;
import correlator.FireCorrelatorState;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import rules.*;

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
		ruleBase.addRule(new F04());
		ruleBase.addRule(new F05());
		ruleBase.addRule(new F06());
		ruleBase.addRule(new F06bis());
		ruleBase.addRule(new F07());
		ruleBase.addRule(new F08());
		ruleBase.addRule(new F08bis());
//		ruleBase.addRule(new F09());
//		ruleBase.addRule(new F10());
//		ruleBase.addRule(new F10());
//		ruleBase.addRule(new F10());
//		ruleBase.addRule(new F10bis());
//		ruleBase.addRule(new F11());
//		ruleBase.addRule(new F12());
//		ruleBase.addRule(new F13());
//		ruleBase.addRule(new F14());
//		ruleBase.addRule(new F14bis());
//		ruleBase.addRule(new F15());
//		ruleBase.addRule(new F16());
//		ruleBase.addRule(new F17());
//		ruleBase.addRule(new F18());
//		ruleBase.addRule(new F19());
//		ruleBase.addRule(new F20());
//		ruleBase.addRule(new F21());
//		ruleBase.addRule(new F22());

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
