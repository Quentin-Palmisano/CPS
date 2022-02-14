package correlator;

import interfaces.CorrelatorStateI;

public interface Health2CorrelatorStateI extends CorrelatorStateI {
	
	public boolean isAmbulanceAvailable();
	public void spreadEvent();

}
