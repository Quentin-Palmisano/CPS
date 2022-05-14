package components;

import components.interfaces.ActionExecutionCI;
import components.interfaces.CEPBusManagementCI;
import components.interfaces.EventEmissionCI;
import components.interfaces.EventReceptionCI;
import components.interfaces.EventReceptionI;
import connectors.ActionExecutionConnector;
import connectors.CEPBusManagementConnector;
import connectors.EventEmissionConnector;
import correlator.CorrelatorState;
import events.EventBase;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.EventI;
import ports.ActionExecutionOutboundPort;
import ports.CEPBusManagementOutboundPort;
import ports.EventEmissionOutboundPort;
import ports.EventReceptionInboundPort;
import rules.RuleBase;


@OfferedInterfaces(offered={EventReceptionCI.class})
@RequiredInterfaces(required={ActionExecutionCI.class, EventEmissionCI.class, CEPBusManagementCI.class})
public abstract class Correlator extends AbstractComponent implements EventReceptionI {
	
	public final String uri;
	
	protected RuleBase ruleBase;
	protected EventBase eventBase = new EventBase();
	
	protected CorrelatorState state;
	
	protected final CEPBusManagementOutboundPort managementPort;
	protected final EventEmissionOutboundPort emissionPort;
	protected final EventReceptionInboundPort receptionPort;
	protected final ActionExecutionOutboundPort executionPort;
	
	private final String stationURI;

	protected Correlator(String uri, String stationURI, CorrelatorState state, RuleBase rb) throws Exception {
		super(1, 1);
		this.uri = uri;
		this.state = state;
		this.ruleBase = rb;
		this.stationURI = stationURI;
		
		managementPort = new CEPBusManagementOutboundPort(this);
		managementPort.localPublishPort();
		
		emissionPort = new EventEmissionOutboundPort(this);
		emissionPort.localPublishPort();
		
		receptionPort = new EventReceptionInboundPort(this);
		receptionPort.publishPort();
		
		executionPort = new ActionExecutionOutboundPort(this);
		executionPort.localPublishPort();
		
		state.setExecutor(executionPort);
		state.setCorrelator(this);
		state.setEmitter(emissionPort);
		
	}
	
	
	
	@Override
	public synchronized void start() throws ComponentStartException {
		super.start();
		
		try {
			this.doPortConnection(managementPort.getPortURI(), CEPBus.ManagementURI, CEPBusManagementConnector.class.getCanonicalName());
		} catch (Exception e) {
			throw new ComponentStartException(e) ;
		}
		
	}



	@Override
	public synchronized void	execute() throws Exception
	{
		
		Thread.sleep(100);
		
		String ibp = managementPort.registerCorrelator(uri, receptionPort.getPortURI());
		this.doPortConnection(emissionPort.getPortURI(), ibp, EventEmissionConnector.class.getCanonicalName());

		managementPort.subscribe(uri, stationURI);
		
		String eibp = managementPort.getExecutorInboundPortURI(stationURI);
		this.doPortConnection(executionPort.getPortURI(), eibp, ActionExecutionConnector.class.getCanonicalName());
		
	}
	
	@Override
	public synchronized void	finalise() throws Exception
	{
		
		this.doPortDisconnection(this.executionPort.getPortURI());
		
		//managementPort.unsubscribe(uri, stationURI);
		
		this.doPortDisconnection(this.emissionPort.getPortURI());
		
		//managementPort.unregisterCorrelator(uri);

		this.doPortDisconnection(this.managementPort.getPortURI());
		
		super.finalise();
	}

	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.emissionPort.unpublishPort();
			this.managementPort.unpublishPort();
			this.executionPort.unpublishPort();
			this.receptionPort.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
	}
	
	@Override
	public void receiveEvent(String emitterURI, EventI event) throws Exception {

		this.traceMessage("Event received from " + emitterURI + " of name " + event.getPropertyValue("name") + "\n");
		
		state.receiveEvent(emitterURI, event);
		eventBase.addEvent(event);
		ruleBase.fireAllOn(eventBase, state);
	}

	@Override
	public void receiveEvents(String emitterURI, EventI[] events) throws Exception {

		for(EventI event : events) {
			this.traceMessage("Event received from " + emitterURI + " of name " + event.getPropertyValue("name") + "\n");
		}
		
		state.receiveEvents(emitterURI, events);
		eventBase.addEvents(events);
		ruleBase.fireAllOn(eventBase, state);
	}

}
