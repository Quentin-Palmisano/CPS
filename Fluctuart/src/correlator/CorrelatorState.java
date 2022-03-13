package correlator;

import components.interfaces.ActionExecutionCI;
import components.interfaces.EventReceptionCI;
import interfaces.CorrelatorStateI;
import interfaces.EventI;

public abstract class CorrelatorState implements EventReceptionCI, CorrelatorStateI {

	protected ActionExecutionCI executor;
	
	public void setExecutor(ActionExecutionCI executor) {
		this.executor = executor;
	}

	@Override
	public void receiveEvents(String emitterURI, EventI[] events) throws Exception {
		for(EventI e : events) {
			receiveEvent(emitterURI, e);
		}
	}
	
	
}
