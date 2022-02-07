package composants.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import interfaces.EventI;

public interface EventReceptionCI extends RequiredCI, OfferedCI {
	
	public void receiveEvent(String emitterURI, EventI event);
	public void receiveEvents(String emitterURI, EventI[] events);

}
