package components;

import java.io.Serializable;
import java.time.LocalTime;

import actions.FireAction;
import components.interfaces.ActionExecutionCI;
import components.interfaces.ActionExecutionI;
import components.interfaces.CEPBusManagementCI;
import components.interfaces.EventEmissionCI;
import connectors.CEPBusManagementConnector;
import connectors.EventEmissionConnector;
import events.AtomicEvent;
import events.FireEventName;
import events.TrafficLightEventName;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.smartcity.components.FireStationFacade;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.FireStationActionCI;
import fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import interfaces.ActionI;
import interfaces.ResponseI;
import ports.ActionExecutionInboundPort;
import ports.CEPBusManagementOutboundPort;
import ports.EventEmissionOutboundPort;

@OfferedInterfaces(offered={FireStationNotificationCI.class, ActionExecutionCI.class})
@RequiredInterfaces(required={FireStationActionCI.class, EventEmissionCI.class, CEPBusManagementCI.class})
public class FireStation extends FireStationFacade implements ActionExecutionI {

	protected final CEPBusManagementOutboundPort managementPort;
	private EventEmissionOutboundPort emissionPort;
	private ActionExecutionInboundPort actionPort;

	public final String uri;
	
	protected FireStation(String stationId, String notificationInboundPortURI, String actionInboundPortURI)
			throws Exception {
		super(stationId, notificationInboundPortURI, actionInboundPortURI);
		this.uri = getURI(stationId);
		
		managementPort = new CEPBusManagementOutboundPort(this);
		managementPort.localPublishPort();
		
		emissionPort = new EventEmissionOutboundPort(this);
		emissionPort.localPublishPort();
		
		actionPort = new ActionExecutionInboundPort(this);
		actionPort.publishPort();
		
	}
	
	@Override
	public synchronized void start() throws ComponentStartException {
		super.start();
		
		try {
			this.doPortConnection(managementPort.getPortURI(), CEPBus.ManagementURI, CEPBusManagementConnector.class.getCanonicalName());
		} catch (Exception e) {
			throw new ComponentStartException(e) ;
		}
		
	}
	
	@Override
	public synchronized void	execute() throws Exception
	{
		super.execute();
		
		String ibp = managementPort.registerEmitter(uri);
		this.doPortConnection(emissionPort.getPortURI(), ibp, EventEmissionConnector.class.getCanonicalName());
		
		managementPort.registerExecutor(uri, actionPort.getPortURI());
	}
	
	@Override
	public synchronized void	finalise() throws Exception
	{
		//managementPort.unregisterExecutor(uri);
		this.doPortDisconnection(this.emissionPort.getPortURI());
		//managementPort.unregisterEmitter(uri);
		this.doPortDisconnection(this.managementPort.getPortURI());
		super.finalise();
	}

	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.emissionPort.unpublishPort();
			this.managementPort.unpublishPort();
			this.actionPort.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
	}
	
	

	// events
	
	@Override
	public void			fireAlarm(
		AbsolutePosition position,
		LocalTime occurrence,
		TypeOfFire type
		) throws Exception
	{
		super.fireAlarm(position, occurrence, type);
		AtomicEvent event = new AtomicEvent(occurrence);
		event.putProperty("name", FireEventName.FIRE_ALARM);
		event.putProperty("position", position);
		event.putProperty("type", type);
		event.putProperty("stationId", stationId);
		emissionPort.sendEvent(uri, event);
	}

	@Override
	public void			endOfFire(
		AbsolutePosition position,
		LocalTime occurrence
		) throws Exception
	{
		super.endOfFire(position, occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		event.putProperty("name", FireEventName.END_OF_FIRE);
		event.putProperty("position", position);
		event.putProperty("stationId", stationId);
		emissionPort.sendEvent(uri, event);
	}

	@Override
	public void			requestPriority(
		IntersectionPosition intersection,
		TypeOfTrafficLightPriority priority,
		String vehicleId,
		AbsolutePosition destination,
		LocalTime occurrence
		) throws Exception
	{
		super.requestPriority(intersection, priority, vehicleId, destination, occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		event.putProperty("name", TrafficLightEventName.REQUEST_PRIORITY);
		event.putProperty("intersection", intersection);
		event.putProperty("priority", priority);
		event.putProperty("vehicleId", vehicleId);
		event.putProperty("destination", destination);
		event.putProperty("stationId", stationId);
		emissionPort.sendEvent(uri, event);
	}

	@Override
	public void			atDestination(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		super.atDestination(vehicleId, occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		event.putProperty("name", FireEventName.AT_DESTINATION);
		event.putProperty("vehicleId", vehicleId);
		event.putProperty("stationId", stationId);
		emissionPort.sendEvent(uri, event);
	}

	@Override
	public void			atStation(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		super.atStation(vehicleId, occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		event.putProperty("name", FireEventName.AT_STATION);
		event.putProperty("vehicleId", vehicleId);
		event.putProperty("stationId", stationId);
		emissionPort.sendEvent(uri, event);
	}

	@Override
	public void			notifyNoStandardTruckAvailable(LocalTime occurrence)
	throws Exception
	{
		super.notifyNoStandardTruckAvailable(occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		event.putProperty("name", FireEventName.NOTIFY_NO_STANDARD_TRUCK_AVAILABLE);
		event.putProperty("stationId", stationId);
		emissionPort.sendEvent(uri, event);
	}

	@Override
	public void			notifyStandardTrucksAvailable(LocalTime occurrence)
	throws Exception
	{
		super.notifyStandardTrucksAvailable(occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		event.putProperty("name", FireEventName.NOTIFY_STANDARD_TRUCKS_AVAILABLE);
		event.putProperty("stationId", stationId);
		emissionPort.sendEvent(uri, event);
	}

	@Override
	public void			notifyNoHighLadderTruckAvailable(LocalTime occurrence)
	throws Exception
	{
		super.notifyNoHighLadderTruckAvailable(occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		event.putProperty("name", FireEventName.NOTIFY_NO_HIGH_LADDER_TRUCK_AVAILABLE);
		event.putProperty("stationId", stationId);
		emissionPort.sendEvent(uri, event);
	}

	@Override
	public void 		notifyHighLadderTrucksAvailable(LocalTime occurrence)
	throws Exception
	{
		super.notifyHighLadderTrucksAvailable(occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		event.putProperty("name", FireEventName.NOTIFY_HIGH_LADDER_TRUCKS_AVAILABLE);
		event.putProperty("stationId", stationId);
		emissionPort.sendEvent(uri, event);
	}

	@Override
	public ResponseI executeAction(ActionI a, Serializable[] params) throws Exception {
		FireAction fa = (FireAction) a;
		if(fa == FireAction.FIRST_ALARM) {
			actionOBP.triggerFirstAlarm((AbsolutePosition) params[0], (TypeOfFirefightingResource) params[1]);
		} else if(fa == FireAction.SECOND_ALARM) {
			actionOBP.triggerSecondAlarm((AbsolutePosition) params[0]);
		} else if(fa == FireAction.GENERAL_ALARM) {
			actionOBP.triggerGeneralAlarm((AbsolutePosition) params[0]);
		}
		return null;
	}
	
	public static String getURI(String stationID) {
		return "FireStation " + stationID;
	}

}
