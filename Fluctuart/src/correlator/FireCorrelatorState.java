package correlator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import actions.FireAction;
import events.AtomicEvent;
import events.FireEventName;
import events.HealthEventName;
import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.utils.TimeManager;
import interfaces.EventI;

public class FireCorrelatorState extends CorrelatorState implements FireCorrelatorStateI {

	boolean highLadderTruckAvailable = false;
	boolean standardTruckAvailable = false;
	public final String stationId;

	public FireCorrelatorState(String stationId) {
		this.stationId=stationId;
	}
	
	@Override
	public void receiveEvent(String emitterURI, EventI event) throws Exception {
		FireEventName name = (FireEventName) event.getPropertyValue("name");
		if(name==FireEventName.NOTIFY_NO_HIGH_LADDER_TRUCK_AVAILABLE) {
			highLadderTruckAvailable = false;
		} else if(name==FireEventName.NOTIFY_NO_HIGH_LADDER_TRUCK_AVAILABLE) {
			highLadderTruckAvailable = true;
		}
		if(name==FireEventName.NOTIFY_NO_STANDARD_TRUCK_AVAILABLE) {
			standardTruckAvailable = false;
		} else if(name==FireEventName.NOTIFY_STANDARD_TRUCKS_AVAILABLE) {
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
	
	@Override
	public boolean propagateEvent(EventI event, FireEventName name) throws Exception {
		AtomicEvent evnt = (AtomicEvent) event;

		String nearestStation = getNextStation(event);
		if(nearestStation==null)return false;
		
		if(name!=null) {
			evnt.putProperty("name", name);
		}
		
		evnt.putProperty("stationId", nearestStation);
		
		emitter.sendEvent(correlator.uri, evnt);
		return true;
	}
	
	@Override
	public boolean propagateEventToAllStation(EventI event, String stationId) throws Exception {
		Iterator<String> fireStationsIditerator = SmartCityDescriptor.createFireStationIdIterator();
		
		while (fireStationsIditerator.hasNext()) {
			String id = fireStationsIditerator.next();
			if(id!=null && id!=stationId) {
				AtomicEvent e = (AtomicEvent) event;
				e.putProperty("stationId", id);
				emitter.sendEvent(correlator.uri, e);
				
			}
		}
		
		return true;
	}
	
	@Override
	public String getNextStation(EventI event) {
		
		AtomicEvent e = (AtomicEvent) event;
		Iterator<String> fireStationsIditerator = SmartCityDescriptor.createFireStationIdIterator();
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
		
		
		while (fireStationsIditerator.hasNext()) {
			String id = fireStationsIditerator.next();
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
	public void setHighLadderTruckAvailable() {
		highLadderTruckAvailable = true;
	}

	@Override
	public void setStandardTruckAvailable() {
		standardTruckAvailable = true;
	}

	@Override
	public void setHighLadderTruckNotAvailable() {
		highLadderTruckAvailable = false;
	}

	@Override
	public void setStandardTruckNotAvailable() {
		standardTruckAvailable = false;
	}

}
