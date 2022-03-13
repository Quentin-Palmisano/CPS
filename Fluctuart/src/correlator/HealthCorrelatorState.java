package correlator;

import java.io.Serializable;

import actions.HealthAction;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import interfaces.EventI;

public class HealthCorrelatorState extends CorrelatorState implements HealthCorrelatorStateI {
	
	boolean ambulanceAvailable = false;
	boolean medicAvailable = false;
	
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
		
		this.executor.execute(HealthAction.INTERVENTION, new Serializable[] {position, personId, type});
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
