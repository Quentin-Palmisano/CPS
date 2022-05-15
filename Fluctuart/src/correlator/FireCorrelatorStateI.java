package correlator;

import events.FireEventName;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import interfaces.CorrelatorStateI;
import interfaces.EventI;

public interface FireCorrelatorStateI extends CorrelatorStateI {
	
	public boolean isHighLadderTruckAvailable();
	public boolean isStandardTruckAvailable();
	void setHighLadderTruckAvailable();
	void setStandardTruckAvailable();
	void setHighLadderTruckNotAvailable();
	void setStandardTruckNotAvailable();
	public void triggerFirstAlarm(AbsolutePosition position, TypeOfFirefightingResource resource) throws Exception;
	public void triggerSecondAlarm(AbsolutePosition position) throws Exception;
	public void triggerGeneralAlarm(AbsolutePosition position) throws Exception;
	public boolean propagateEvent(EventI event, FireEventName type) throws Exception;
	public String getNextStation(EventI event);
	boolean propagateEventToAllStation(EventI event, String stationId) throws Exception;

}
