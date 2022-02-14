package correlator;

import interfaces.CorrelatorStateI;

public interface Fire1CorrelatorStateI extends CorrelatorStateI {
	
	public boolean isBigLadderAvailable();
	public void triggerAlarm();

}
