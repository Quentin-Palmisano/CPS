package components.interfaces;

import interfaces.EventI;

public interface EventReceptionI {
	
	public void receiveEvent(String emitterURI, EventI event) throws Exception;
	public void receiveEvents(String emitterURI, EventI[] events) throws Exception;

}
