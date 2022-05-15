package correlator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import actions.HealthAction;
import events.AtomicEvent;
import events.HealthEventName;
import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import fr.sorbonne_u.cps.smartcity.utils.TimeManager;
import interfaces.EventI;

public class HealthCorrelatorState extends CorrelatorState implements HealthCorrelatorStateI {

	boolean ambulanceAvailable = false;
	boolean medicAvailable = false;
	public final String stationId;

	public HealthCorrelatorState(String stationId) {
		this.stationId=stationId;
	}
	
	@Override
	public void receiveEvent(String emitterURI, EventI event) throws Exception {
		if(!(event.getPropertyValue("name") instanceof HealthEventName)) return;
		HealthEventName name = (HealthEventName) event.getPropertyValue("name");
		if(name==HealthEventName.NOTIFY_NO_AMBULANCE_AVAILABLE) {
			ambulanceAvailable = false;
		} else if(name==HealthEventName.NOTIFY_AMBULANCES_AVAILABLE) {
			ambulanceAvailable = true;
		}
		if(name==HealthEventName.NOTIFY_NO_MEDIC_AVAILABLE) {
			medicAvailable = false;
		} else if(name==HealthEventName.NOTIFY_MEDICS_AVAILABLE) {
			medicAvailable = true;
		}
	}

	@Override
	public void triggerIntervention(AbsolutePosition position, String personId, TypeOfSAMURessources type) throws Exception{
		if(position.equals(SmartCityDescriptor.getPosition(stationId))) return;
		correlator.traceMessage("Trigger Intervention at position " + position + " for " + personId + " of type " + type + "\n");		
		this.executor.executeAction(HealthAction.INTERVENTION, new Serializable[] {position, personId, type});
	}
	
	@Override
	public void callMedic(AbsolutePosition position, String personId, TypeOfSAMURessources type) throws Exception{
		correlator.traceMessage("Call Medic for position " + position + " for " + personId + " of type " + type + "\n");
		this.executor.executeAction(HealthAction.CALL_MEDIC, new Serializable[] {position, personId, type});
	}

	@Override
	public boolean isAmbulanceAvailable() {
		return ambulanceAvailable;
	}

	@Override
	public boolean isMedicAvailable() {
		return medicAvailable;
	}

	@Override
	public boolean propagateEvent(EventI event, TypeOfHealthAlarm type, HealthEventName name) throws Exception {
		AtomicEvent evnt = (AtomicEvent) event;

		String nearestStation = getNextStation(event);
		if(nearestStation==null)return false;
		
		if(name!=null) {
			evnt.putProperty("name", name);
		}

		if(type!=null) {
			evnt.putProperty("type", type);
		}
		
		evnt.putProperty("stationId", nearestStation);
		
		emitter.sendEvent(correlator.uri, evnt);
		return true;
	}

	@Override
	public String getNextStation(EventI event) {
		
		AtomicEvent e = (AtomicEvent) event;
		Iterator<String> samuStationsIditerator = SmartCityDescriptor.createSAMUStationIdIterator();
		String initial = (String) e.getPropertyValue("stationId");
		AbsolutePosition initialPos = SmartCityDescriptor.getPosition(initial);
		String nearestStation = "";
		AbsolutePosition nearestPos = null;

		ArrayList<String> used = (ArrayList<String>) e.getPropertyValue("lastStations");
		if(used==null) {
			used = new ArrayList<String>();
			e.putProperty("lastStations", used);
		}
		used.add(initial);
		
		
		while (samuStationsIditerator.hasNext()) {
			String id = samuStationsIditerator.next();
			if(id!=null && id!=initial && !used.contains(id)) {
				AbsolutePosition p = SmartCityDescriptor.getPosition(id);
				if(nearestStation=="" && nearestPos==null || initialPos.distance(p) < initialPos.distance(nearestPos)) {
					nearestStation = id;
					nearestPos = p;
				}
			}
		}
		if(nearestStation=="")return null;
		return nearestStation;
	}

	@Override
	public void setAmbulancesAvailable() {
		ambulanceAvailable = true;
	}

	@Override
	public void setMedicsAvailable() {
		medicAvailable = true;
	}

	@Override
	public void setAmbulancesNotAvailable() {
		ambulanceAvailable = false;
	}

	@Override
	public void setMedicsNotAvailable() {
		medicAvailable = false;
	}
}
