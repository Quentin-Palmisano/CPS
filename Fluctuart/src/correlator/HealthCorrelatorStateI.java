package correlator;

import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import interfaces.CorrelatorStateI;

public interface HealthCorrelatorStateI extends CorrelatorStateI {
	
	void triggerIntervention(AbsolutePosition position, String personId, TypeOfSAMURessources type) throws Exception;
	boolean isAmbulanceAvailable();
	boolean isMedicAvailable();

}
