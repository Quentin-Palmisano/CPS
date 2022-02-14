package composants;

import java.io.Serializable;

import composants.interfaces.*;
import interfaces.EventI;

public class CEPBus implements EventEmissionCI, Serializable{

	private static final long serialVersionUID = 6632854676686822297L;

	@Override
	public void sendEvent(String emitterURI, EventI event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) {
		// TODO Auto-generated method stub
		
	}
	

}
