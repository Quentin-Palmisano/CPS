package correlator;

import components.Correlator;
import components.interfaces.ActionExecutionCI;
import components.interfaces.EventEmissionCI;
import components.interfaces.EventReceptionCI;
import interfaces.CorrelatorStateI;
import interfaces.EventI;

public abstract class CorrelatorState implements EventReceptionCI, CorrelatorStateI {

	protected ActionExecutionCI executor;
	protected Correlator correlator;
	protected EventEmissionCI emitter;
	
	public void setExecutor(ActionExecutionCI executor) {
		this.executor = executor;
	}
	
	public void setEmitter(EventEmissionCI emitter) {
		this.emitter = emitter;
	}
	
	public void setCorrelator(Correlator correlator) {
		this.correlator = correlator;
	}

	@Override
	public void receiveEvents(String emitterURI, EventI[] events) throws Exception {
		for(EventI e : events) {
			receiveEvent(emitterURI, e);
		}
	}
	
	@Override
	public void traceRuleTrigger(String ruleName) {
		correlator.traceMessage("Rule " + ruleName + " was triggered \n");
	}
	
}
