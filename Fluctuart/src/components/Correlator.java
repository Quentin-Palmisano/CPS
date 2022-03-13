package components;

import classes.EventBase;
import classes.RuleBase;
import components.interfaces.EventReceptionCI;
import connectors.ActionExecutionConnector;
import connectors.CEPBusManagementConnector;
import connectors.EventEmissionConnector;
import correlator.CorrelatorState;
import fr.sorbonne_u.components.AbstractComponent;
import interfaces.CorrelatorStateI;
import interfaces.EventI;
import ports.ActionExecutionOutboundPort;
import ports.CEPBusManagementOutboundPort;
import ports.EventEmissionOutboundPort;
import ports.EventReceptionInboundPort;

public abstract class Correlator extends AbstractComponent implements EventReceptionCI {
	
	public final String uri;
	
	protected RuleBase ruleBase;
	protected EventBase eventBase = new EventBase();
	
	protected CorrelatorState state;
	
	protected final CEPBusManagementOutboundPort managementPort;
	protected final EventEmissionOutboundPort emissionPort;
	protected final EventReceptionInboundPort receptionPort;
	protected final ActionExecutionOutboundPort executionPort;

	protected Correlator(String uri, String executorURI, CorrelatorState state, RuleBase rb) throws Exception {
		super(1, 1);
		this.uri = uri;
		this.state = state;
		this.ruleBase=rb;
		
		managementPort = new CEPBusManagementOutboundPort(this);
		managementPort.localPublishPort();
		this.doPortConnection(managementPort.getPortURI(), CEPBus.ManagementURI, CEPBusManagementConnector.class.getCanonicalName());
		
		emissionPort = new EventEmissionOutboundPort(this);
		emissionPort.localPublishPort();
		
		receptionPort = new EventReceptionInboundPort("zbeb", this);
		receptionPort.publishPort();
		
		String ibp = managementPort.registerCorrelator(uri, receptionPort.getPortURI());
		
		
		this.doPortConnection(emissionPort.getPortURI(), ibp, EventEmissionConnector.class.getCanonicalName());
		
		executionPort = new ActionExecutionOutboundPort(this);
		executionPort.localPublishPort();
		
		String eibp = managementPort.getExecutorInboundPortURI(executorURI);
		
		this.doPortConnection(executionPort.getPortURI(), eibp, ActionExecutionConnector.class.getCanonicalName());
		
		state.setExecutor(executionPort);
		
	}
	
	@Override
	public void receiveEvent(String emitterURI, EventI event) throws Exception {
		
		this.traceMessage("Event received from " + emitterURI + "\n");
		
		state.receiveEvent(emitterURI, event);
		eventBase.addEvent(event);
		ruleBase.fireAllOn(eventBase, state);
	}

	@Override
	public void receiveEvents(String emitterURI, EventI[] events) throws Exception {
		
		this.traceMessage("Events received from " + emitterURI + "\n");
		
		state.receiveEvents(emitterURI, events);
		eventBase.addEvents(events);
		ruleBase.fireAllOn(eventBase, state);
	}

}
