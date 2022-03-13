package ports;

import components.interfaces.EventEmissionCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.EventI;

public class EventEmissionOutboundPort extends AbstractOutboundPort implements EventEmissionCI {

	public EventEmissionOutboundPort(ComponentI owner) throws Exception {
		super(EventEmissionCI.class, owner);
	}
	
	public EventEmissionOutboundPort(String URI, ComponentI owner) throws Exception {
		super(URI, EventEmissionCI.class, owner);
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
