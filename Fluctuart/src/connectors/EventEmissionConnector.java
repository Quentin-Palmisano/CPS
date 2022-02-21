package connectors;

import components.interfaces.EventEmissionCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.EventI;

public class EventEmissionConnector extends AbstractConnector implements EventEmissionCI {
	
	@Override
	public void sendEvent(String emitterURI, EventI event) throws Exception {
		((EventEmissionCI)this.offering).sendEvent(emitterURI, event);
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) throws Exception {
		((EventEmissionCI)this.offering).sendEvents(emitterURI, events);
	}
	
}
