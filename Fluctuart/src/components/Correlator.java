package components;

import classes.RuleBase;
import components.interfaces.EventEmissionCI;
import components.interfaces.EventReceptionCI;
import fr.sorbonne_u.components.AbstractComponent;
import interfaces.EventI;

public class Correlator extends AbstractComponent implements EventEmissionCI, EventReceptionCI{
	
	public static final String URI = "URI_CORRELATOR";
	
	RuleBase ruleBase;

	protected Correlator(RuleBase rb) {
		super(1, 1);
		this.ruleBase=rb;
		
	}

	@Override
	public void receiveEvent(String emitterURI, EventI event) throws Exception {
	}

	@Override
	public void receiveEvents(String emitterURI, EventI[] events) throws Exception {
	}

	@Override
	public void sendEvent(String emitterURI, EventI event) throws Exception {
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) throws Exception {
	}


	

}
