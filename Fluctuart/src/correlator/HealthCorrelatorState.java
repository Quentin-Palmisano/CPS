package correlator;

import interfaces.EventI;

public class HealthCorrelatorState extends CorrelatorState implements HealthCorrelatorStateI {

	@Override
	public void receiveEvent(String emitterURI, EventI event) throws Exception {
		
		
	}
	
	@Override
	public boolean isAmbulanceAvailable() {
		return false;
	}

	@Override
	public void callAmbulance() {
		
	}

	@Override
	public void spreadEvent() {
		
	}

	

}
