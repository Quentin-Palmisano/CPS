package ports;

import components.interfaces.EventEmissionCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.EventI;

public class EventEmissionOutboundPort extends AbstractOutboundPort implements EventEmissionCI {

	private static final long serialVersionUID = 6434128801996724805L;

	public EventEmissionOutboundPort(ComponentI owner) throws Exception {
		super(EventEmissionCI.class, owner);
	}
	
	public EventEmissionOutboundPort(String URI, ComponentI owner) throws Exception {
		super(URI, EventEmissionCI.class, owner);
	}
	
	public void sendEvent(EventI event) throws Exception {
		sendEvent(this.getPortURI(), event);
	}
	
	public void sendEvents(EventI[] events) throws Exception {
		sendEvents(this.getPortURI(), events);
	}
	
	@Override
	public void sendEvent(String emitterURI, EventI event) throws Exception {
		((EventEmissionCI) this.getConnector()).sendEvent(emitterURI, event);
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) throws Exception {
		((EventEmissionCI) this.getConnector()).sendEvents(emitterURI, events);
	}

}
