package correlator;

import java.util.ArrayList;

import events.HealthEventName;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import interfaces.CorrelatorStateI;
import interfaces.EventI;

public interface HealthCorrelatorStateI extends CorrelatorStateI {
	
	void triggerIntervention(AbsolutePosition position, String personId, TypeOfSAMURessources type) throws Exception;
	void callMedic(AbsolutePosition position, String personId, TypeOfSAMURessources type) throws Exception;
	boolean isAmbulanceAvailable();
	boolean isMedicAvailable();
	void setAmbulancesAvailable();
	void setMedicsAvailable();
	void setAmbulancesNotAvailable();
	void setMedicsNotAvailable();
	String getNextStation(EventI event);
	boolean propagateEvent(EventI e, TypeOfHealthAlarm type, HealthEventName name) throws Exception;

}
