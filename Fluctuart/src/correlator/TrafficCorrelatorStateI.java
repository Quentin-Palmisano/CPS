package correlator;

import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;

public interface TrafficCorrelatorStateI {
	public void changePriority(TypeOfTrafficLightPriority priority) throws Exception;
}
