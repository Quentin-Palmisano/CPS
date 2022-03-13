package correlator;

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
	public void triggerAlarm() {
		correlator.traceMessage("Trigger Alarm\n");
		
		// TODO trigger alarm
		
	}

}
