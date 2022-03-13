package components;

import java.io.Serializable;
import java.time.LocalTime;

import classes.AtomicEvent;
import components.interfaces.ActionExecutionCI;
import connectors.EventEmissionConnector;
import fr.sorbonne_u.cps.smartcity.components.FireStationFacade;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import interfaces.ActionI;
import interfaces.ResponseI;
import ports.ActionExecutionInboundPort;
import ports.EventEmissionOutboundPort;

public class FireStation extends FireStationFacade implements ActionExecutionCI{

	private EventEmissionOutboundPort emissionPort;
	private ActionExecutionInboundPort actionPort;

	public final String uri;
	
	protected FireStation(String uri, String stationId, String notificationInboundPortURI, String actionInboundPortURI)
			throws Exception {
		super(stationId, notificationInboundPortURI, actionInboundPortURI);
		this.uri = uri;
		
		emissionPort = new EventEmissionOutboundPort(this);
		emissionPort.localPublishPort();
		
		actionPort = new ActionExecutionInboundPort(this);
		actionPort.publishPort();
		
		String ibp = CEPBus.BUS.registerEmitter(uri);
		this.doPortConnection(emissionPort.getPortURI(), ibp, EventEmissionConnector.class.getCanonicalName());
		
		CEPBus.BUS.registerExecutor(uri, actionPort.getPortURI());
		
	}
	
	@Override
	public synchronized void	execute() throws Exception
	{
		
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
		event.putProperty("position", position);
		event.putProperty("type", type);
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
		event.putProperty("position", position);
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
		event.putProperty("intersection", intersection);
		event.putProperty("priority", priority);
		event.putProperty("vehicleId", vehicleId);
		event.putProperty("destination", destination);
		emissionPort.sendEvent(uri, event);
	}

	@Override
	public void			atDestination(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		super.atDestination(vehicleId, occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		event.putProperty("vehicleId", vehicleId);
		emissionPort.sendEvent(uri, event);
	}

	@Override
	public void			atStation(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		super.atStation(vehicleId, occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		event.putProperty("vehicleId", vehicleId);
		emissionPort.sendEvent(uri, event);
	}

	@Override
	public void			notifyNoStandardTruckAvailable(LocalTime occurrence)
	throws Exception
	{
		super.notifyNoStandardTruckAvailable(occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		emissionPort.sendEvent(uri, event);
	}

	@Override
	public void			notifyStandardTrucksAvailable(LocalTime occurrence)
	throws Exception
	{
		super.notifyStandardTrucksAvailable(occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		emissionPort.sendEvent(uri, event);	
	}

	@Override
	public void			notifyNoHighLadderTruckAvailable(LocalTime occurrence)
	throws Exception
	{
		super.notifyNoHighLadderTruckAvailable(occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		emissionPort.sendEvent(uri, event);
	}

	@Override
	public void 		notifyHighLadderTrucksAvailable(LocalTime occurrence)
	throws Exception
	{
		super.notifyHighLadderTrucksAvailable(occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		emissionPort.sendEvent(uri, event);
	}

	@Override
	public ResponseI execute(ActionI a, Serializable[] params) throws Exception {
		return null;
	}

}
