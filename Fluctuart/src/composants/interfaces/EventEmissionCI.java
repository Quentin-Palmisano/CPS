package composants.interfaces;

import fr.sorbonne_u.components.interfaces.ComponentInterface;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import interfaces.EventI;

public interface EventEmissionCI extends ComponentInterface, RequiredCI, OfferedCI {
	
	public void sendEvent(String emitterURI, EventI event) throws Exception;
	public void sendEvents(String emitterURI, EventI[] events) throws Exception;
	
}
