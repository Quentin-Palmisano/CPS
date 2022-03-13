package ports;

import components.interfaces.EventReceptionCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.EventI;

public class EventReceptionInboundPort extends AbstractInboundPort implements EventReceptionCI {

	public EventReceptionInboundPort(ComponentI owner) throws Exception {
		super(EventReceptionCI.class, owner);
	}
	
	public EventReceptionInboundPort(String URI, ComponentI owner) throws Exception {
		super(URI, EventReceptionCI.class, owner);
	}
	
	@Override
	public void receiveEvent(String emitterURI, EventI event) throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((EventReceptionCI)owner).receiveEvent(emitterURI, event);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void receiveEvents(String emitterURI, EventI[] events) throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((EventReceptionCI)owner).receiveEvents(emitterURI, events);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
