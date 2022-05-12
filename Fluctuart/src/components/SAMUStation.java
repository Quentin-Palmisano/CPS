package components;

import java.io.Serializable;
import java.time.LocalTime;

import actions.HealthAction;
import components.interfaces.ActionExecutionCI;
import components.interfaces.ActionExecutionI;
import components.interfaces.CEPBusManagementCI;
import components.interfaces.EventEmissionCI;
import connectors.CEPBusManagementConnector;
import connectors.EventEmissionConnector;
import events.AtomicEvent;
import events.HealthEventName;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.cps.smartcity.components.SAMUStationFacade;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUActionCI;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import interfaces.ActionI;
import interfaces.ResponseI;
import ports.ActionExecutionInboundPort;
import ports.CEPBusManagementOutboundPort;
import ports.EventEmissionOutboundPort;

@OfferedInterfaces(offered={SAMUNotificationCI.class, ActionExecutionCI.class})
@RequiredInterfaces(required={SAMUActionCI.class, EventEmissionCI.class, CEPBusManagementCI.class})
public class SAMUStation extends SAMUStationFacade implements ActionExecutionI {

	protected final CEPBusManagementOutboundPort managementPort;
	private EventEmissionOutboundPort emissionPort;
	private ActionExecutionInboundPort actionPort;

	public final String uri;
	
	protected SAMUStation(String uri, String stationId, String notificationInboundPortURI, String actionInboundPortURI)
			throws Exception {
		super(stationId, notificationInboundPortURI, actionInboundPortURI);
		this.uri = uri;
		
		managementPort = new CEPBusManagementOutboundPort(this);
		managementPort.localPublishPort();
		
		emissionPort = new EventEmissionOutboundPort(this);
		emissionPort.localPublishPort();
		
		actionPort = new ActionExecutionInboundPort(this);
		actionPort.publishPort();
		
		
		
	}
	
	
	@Override
	public synchronized void	execute() throws Exception
	{
		super.execute();

		this.doPortConnection(managementPort.getPortURI(), CEPBus.ManagementURI, CEPBusManagementConnector.class.getCanonicalName());
		
		String ibp = managementPort.registerEmitter(uri);
		this.doPortConnection(emissionPort.getPortURI(), ibp, EventEmissionConnector.class.getCanonicalName());
		
		managementPort.registerExecutor(uri, actionPort.getPortURI());
	}
	
	@Override
	public synchronized void	finalise() throws Exception
	{
		managementPort.unregisterExecutor(uri);
		this.doPortDisconnection(this.emissionPort.getPortURI());
		super.finalise();
	}

	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.emissionPort.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
	}
	
	
	
	// events
	
	@Override
	public void			healthAlarm(
		AbsolutePosition position,
		TypeOfHealthAlarm type,
		LocalTime occurrence
		) throws Exception
	{
		super.healthAlarm(position, type, occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		event.putProperty("name", HealthEventName.HEALTH_ALARM);
		event.putProperty("position", position);
		event.putProperty("type", type);
		event.putProperty("stationId", stationId);
		emissionPort.sendEvent(uri, event);
		
	}

	@Override
	public void			trackingAlarm(
		AbsolutePosition position,
		String personId,
		LocalTime occurrence
		) throws Exception
	{
		super.trackingAlarm(position, personId, occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		event.putProperty("name", HealthEventName.TRACKING_ALARM);
		event.putProperty("position", position);
		event.putProperty("personId", personId);
		event.putProperty("stationId", stationId);
		emissionPort.sendEvent(uri, event);
	}

	@Override
	public void			manualSignal(
		String personId,
		LocalTime occurrence
		) throws Exception
	{
		super.manualSignal(personId, occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		event.putProperty("name", HealthEventName.MANUAL_SIGNAL);
		event.putProperty("personId", personId);
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
		event.putProperty("name", HealthEventName.REQUEST_PRIORITY);
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
		event.putProperty("name", HealthEventName.AT_DESTINATION);
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
		event.putProperty("name", HealthEventName.AT_STATION);
		event.putProperty("vehicleId", vehicleId);
		event.putProperty("stationId", stationId);
		emissionPort.sendEvent(uri, event);
	}

	@Override
	public void			notifyMedicsAvailable(LocalTime occurrence)
	throws Exception
	{
		super.notifyMedicsAvailable(occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		event.putProperty("name", HealthEventName.NOTIFY_MEDICS_AVAILABLE);
		event.putProperty("stationId", stationId);
		emissionPort.sendEvent(uri, event);
	}

	@Override
	public void			notifyNoMedicAvailable(LocalTime occurrence)
	throws Exception
	{
		super.notifyNoMedicAvailable(occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		event.putProperty("name", HealthEventName.NOTIFY_NO_MEDIC_AVAILABLE);
		event.putProperty("stationId", stationId);
		emissionPort.sendEvent(uri, event);
	}

	@Override
	public void			notifyAmbulancesAvailable(LocalTime occurrence)
	throws Exception
	{
		super.notifyAmbulancesAvailable(occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		event.putProperty("name", HealthEventName.NOTIFY_AMBULANCES_AVAILABLE);
		event.putProperty("stationId", stationId);
		emissionPort.sendEvent(uri, event);
	}

	@Override
	public void			notifyNoAmbulanceAvailable(LocalTime occurrence)
	throws Exception
	{
		super.notifyNoAmbulanceAvailable(occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		event.putProperty("name", HealthEventName.NOTIFY_NO_AMBULANCE_AVAILABLE);
		event.putProperty("stationId", stationId);
		emissionPort.sendEvent(uri, event);
	}

	@Override
	public ResponseI executeAction(ActionI a, Serializable[] params) throws Exception {
		HealthAction action = (HealthAction) a;
		if(action == HealthAction.INTERVENTION) {
			this.actionOBP.triggerIntervention((AbsolutePosition) params[0], (String) params[1], (TypeOfSAMURessources) params[2]);	
		}
		return null;
	}
	
}
