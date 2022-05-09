package correlator;

import java.io.Serializable;

import actions.FireAction;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import interfaces.EventI;

public class FireCorrelatorState extends CorrelatorState implements FireCorrelatorStateI {

	boolean highLadderTruckAvailable = false;
	boolean standardTruckAvailable = false;
	
	@Override
	public void receiveEvent(String emitterURI, EventI event) throws Exception {
		String name = (String) event.getPropertyValue("name");
		if(name.equals("notifyNoHighLadderTruckAvailable")) {
			highLadderTruckAvailable = false;
		} else if(name.equals("notifyHighLadderTrucksAvailable")) {
			highLadderTruckAvailable = true;
		}
		if(name.equals("notifyNoStandardTruckAvailable")) {
			standardTruckAvailable = false;
		} else if(name.equals("notifyStandardTrucksAvailable")) {
			standardTruckAvailable = true;
		}
	}

	@Override
	public boolean isHighLadderTruckAvailable() {
		return highLadderTruckAvailable;
	}
	
	@Override
	public boolean isStandardTruckAvailable() {
		return standardTruckAvailable;
	}

	@Override
	public void triggerFirstAlarm(AbsolutePosition position, TypeOfFirefightingResource resource) throws Exception {
		correlator.traceMessage("Trigger Alarm\n");
		
		executor.executeAction(FireAction.FIRST_ALARM, new Serializable[] {position, resource});
	}
	
	@Override
	public void triggerSecondAlarm(AbsolutePosition position) throws Exception {
		correlator.traceMessage("Trigger Alarm\n");
		
		executor.executeAction(FireAction.SECOND_ALARM, new Serializable[] {position});
	}
	
	@Override
	public void triggerGeneralAlarm(AbsolutePosition position) throws Exception {
		correlator.traceMessage("Trigger Alarm\n");
		
		executor.executeAction(FireAction.GENERAL_ALARM, new Serializable[] {position});
	}

}
