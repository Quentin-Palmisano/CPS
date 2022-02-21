package ports;

import components.interfaces.EventEmissionCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.EventI;

public class EventEmissionInboundPort extends AbstractInboundPort implements EventEmissionCI {

	private static final long serialVersionUID = 1756730000799415123L;

	public EventEmissionInboundPort(ComponentI owner) throws Exception {
		super(EventEmissionCI.class, owner);
	}
	
	public EventEmissionInboundPort(String URI, ComponentI owner) throws Exception {
		super(URI, EventEmissionCI.class, owner);
	}
	
	@Override
	public void sendEvent(String emitterURI, EventI event) throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((EventEmissionCI)owner).sendEvent(emitterURI, event);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((EventEmissionCI)owner).sendEvents(emitterURI, events);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
