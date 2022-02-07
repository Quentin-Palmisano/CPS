package composants.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import interfaces.EventI;

public interface EventEmissionCI extends RequiredCI, OfferedCI {
	
	public void sendEvent(String emitterURI, EventI event);
	public void sendEvents(String emitterURI, EventI[] events);
	
}
