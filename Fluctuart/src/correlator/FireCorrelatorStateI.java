package correlator;

import interfaces.CorrelatorStateI;

public interface FireCorrelatorStateI extends CorrelatorStateI {
	
	public boolean isHighLadderTruckAvailable();
	public boolean isStandardTruckAvailable();
	public void triggerAlarm();

}
