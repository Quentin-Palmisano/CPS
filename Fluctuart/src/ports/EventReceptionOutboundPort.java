package ports;

import components.interfaces.EventReceptionCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.EventI;

public class EventReceptionOutboundPort extends AbstractOutboundPort implements EventReceptionCI {

	public EventReceptionOutboundPort(ComponentI owner) throws Exception {
		super(EventReceptionCI.class, owner);
	}
	
	public EventReceptionOutboundPort(String URI, ComponentI owner) throws Exception {
		super(URI, EventReceptionCI.class, owner);
	}
	
	@Override
	public void receiveEvent(String emitterURI, EventI event) throws Exception {
		((EventReceptionCI) this.getConnector()).receiveEvent(emitterURI, event);
	}

	@Override
	public void receiveEvents(String emitterURI, EventI[] events) throws Exception {
		((EventReceptionCI) this.getConnector()).receiveEvents(emitterURI, events);
	}

}
