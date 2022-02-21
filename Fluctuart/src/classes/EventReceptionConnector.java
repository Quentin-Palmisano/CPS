package classes;

import composants.interfaces.EventReceptionCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.EventI;

public class EventReceptionConnector extends AbstractConnector implements EventReceptionCI {
	
	@Override
	public void receiveEvent(String emitterURI, EventI event) {
		((EventReceptionCI)this.offering).receiveEvent(emitterURI, event);
	}

	@Override
	public void receiveEvents(String emitterURI, EventI[] events) {
		((EventReceptionCI)this.offering).receiveEvents(emitterURI, events);
	}
	
}
