package components;

import classes.RuleBase;
import components.interfaces.EventReceptionCI;
import connectors.EventEmissionConnector;
import fr.sorbonne_u.components.AbstractComponent;
import interfaces.EventI;
import ports.EventEmissionOutboundPort;

public abstract class Correlator extends AbstractComponent implements EventReceptionCI {
	
	protected RuleBase ruleBase;
	
	private final EventEmissionOutboundPort emissionPort;

	protected Correlator(RuleBase rb) throws Exception {
		super(1, 1);
		this.ruleBase=rb;
		
		emissionPort = new EventEmissionOutboundPort(this);
		emissionPort.localPublishPort();
		
		String ibp = CEPBus.BUS.registerEmitter(emissionPort.getPortURI());
		this.doPortConnection(emissionPort.getPortURI(), ibp, EventEmissionConnector.class.getCanonicalName());
		
	}
	


	public void sendEvent(EventI event) throws Exception {
		emissionPort.sendEvent(event);
	}

	public void sendEvents(EventI[] events) throws Exception {
		emissionPort.sendEvents(events);
	}

}
