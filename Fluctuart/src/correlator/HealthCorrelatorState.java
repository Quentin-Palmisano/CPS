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
		String name = (String) event.getPropertyValue("name");
		if(name.equals("notifyNoAmbulanceAvailable")) {
			ambulanceAvailable = false;
		} else if(name.equals("notifyAmbulancesAvailable")) {
			ambulanceAvailable = true;
		}
		if(name.equals("notifyNoMedicAvailable")) {
			medicAvailable = false;
		} else if(name.equals("notifyMedicsAvailable")) {
			medicAvailable = true;
		}
	}
	
	@Override
	public void triggerIntervention(AbsolutePosition position, String personId, TypeOfSAMURessources type) throws Exception{
		correlator.traceMessage("Trigger Intervention at position " + position + " for " + personId + " of type " + type + "\n");
		
		HealthAction action = new HealthAction(position, personId, type);
		this.executor.execute(action, null);
	}
	
	@Override
	public boolean isAmbulanceAvailable() {
		return ambulanceAvailable;
	}
	
	@Override
	public boolean isMedicAvailable() {
		return medicAvailable;
	}

	

}
