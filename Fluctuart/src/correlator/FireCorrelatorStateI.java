package correlator;

import actions.FireAction;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import interfaces.CorrelatorStateI;

public interface FireCorrelatorStateI extends CorrelatorStateI {
	
	public boolean isHighLadderTruckAvailable();
	public boolean isStandardTruckAvailable();
	public void triggerFirstAlarm(AbsolutePosition position, TypeOfFirefightingResource resource) throws Exception;
	public void triggerSecondAlarm(AbsolutePosition position) throws Exception;
	public void triggerGeneralAlarm(AbsolutePosition position) throws Exception;

}
