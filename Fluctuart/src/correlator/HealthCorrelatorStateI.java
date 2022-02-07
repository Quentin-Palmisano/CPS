package correlator;

import interfaces.CorrelatorStateI;

public interface HealthCorrelatorStateI extends CorrelatorStateI {
	
	public boolean isAmbulanceAvailable();
	public void callAmbulance();

}
