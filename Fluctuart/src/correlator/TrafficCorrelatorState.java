package correlator;

import java.io.Serializable;

import actions.TrafficAction;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import interfaces.EventI;

public class TrafficCorrelatorState extends CorrelatorState implements TrafficCorrelatorStateI {

	@Override
	public void receiveEvent(String emitterURI, EventI event) throws Exception {
		
	}

	@Override
	public void changePriority(TypeOfTrafficLightPriority priority) throws Exception {
		executor.executeAction(TrafficAction.PRIORITY_CHANGE, new Serializable[] {priority});
	}

}
