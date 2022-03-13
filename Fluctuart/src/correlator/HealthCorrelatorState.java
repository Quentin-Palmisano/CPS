package correlator;

import actions.HealthAction;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import interfaces.EventI;

public class HealthCorrelatorState extends CorrelatorState implements HealthCorrelatorStateI {

	boolean ambulanceAvailable;
	boolean medicAvailable;
	
	@Override
	public void receiveEvent(String emitterURI, EventI event) throws Exception {
		if(event.hasProperty("NoAmbulanceAvailable")) {
			ambulanceAvailable = false;
		}else if(event.hasProperty("AmbulanceAvailable")) {
			ambulanceAvailable = true;
		}
		if(event.hasProperty("NoMedicAvailable")) {
			medicAvailable = false;
		}else if(event.hasProperty("MedicAvailable")) {
			medicAvailable = true;
		}
	}
	
	@Override
	public void triggerIntervention(AbsolutePosition position, String personId, TypeOfSAMURessources type) throws Exception{
		HealthAction action = new HealthAction(position, personId, type);
		this.executor.execute(action, null);
	}
	
	@Override
	public boolean isAmbulanceAvailable() {
		return ambulanceAvailable;
	}

	

}
