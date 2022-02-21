package composants.interfaces;

import fr.sorbonne_u.components.interfaces.ComponentInterface;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import interfaces.EventI;

public interface EventReceptionCI extends ComponentInterface, RequiredCI, OfferedCI {
	
	public void receiveEvent(String emitterURI, EventI event) throws Exception;
	public void receiveEvents(String emitterURI, EventI[] events) throws Exception;

}
