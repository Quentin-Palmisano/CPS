package correlator;

import actions.HealthAction;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import interfaces.EventI;

public class HealthCorrelatorState extends CorrelatorState implements HealthCorrelatorStateI {

	@Override
	public void receiveEvent(String emitterURI, EventI event) throws Exception {
		
		
	}
	
	@Override
	public void triggerIntervention(AbsolutePosition position, String personId, TypeOfSAMURessources type) throws Exception{
		HealthAction action = new HealthAction(position, personId, type);
		this.executor.execute(action, null);
	}
	
	@Override
	public boolean isAmbulanceAvailable() {
		return false;
	}

	

}
