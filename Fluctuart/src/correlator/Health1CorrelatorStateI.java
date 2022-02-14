package correlator;

import interfaces.CorrelatorStateI;

public interface Health1CorrelatorStateI extends CorrelatorStateI {
	
	public boolean isAmbulanceAvailable();
	public void callAmbulance();

}
