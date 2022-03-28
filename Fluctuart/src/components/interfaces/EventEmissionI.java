package components.interfaces;

import interfaces.EventI;

public interface EventEmissionI {
	
	public void sendEvent(String emitterURI, EventI event) throws Exception;
	public void sendEvents(String emitterURI, EventI[] events) throws Exception;
	
}
