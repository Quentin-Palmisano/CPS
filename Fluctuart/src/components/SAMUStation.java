package components;

import java.io.Serializable;
import java.time.LocalTime;

import components.interfaces.ActionExecutionCI;
import connectors.EventEmissionConnector;
import events.HealthEvent;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.smartcity.components.SAMUStationFacade;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import interfaces.ActionI;
import interfaces.ResponseI;
import ports.ActionExecutionInboundPort;
import ports.EventEmissionOutboundPort;

public class SAMUStation extends SAMUStationFacade implements ActionExecutionCI {
	
	private EventEmissionOutboundPort emissionPort;
	private ActionExecutionInboundPort actionPort;

	public final String uri;
	
	protected SAMUStation(String uri, String stationId, String notificationInboundPortURI, String actionInboundPortURI)
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
	public synchronized void start() throws ComponentStartException
	{
		super.start();
	}
	
	@Override
	public synchronized void	execute() throws Exception
	{

	}
	
	@Override
	public synchronized void	finalise() throws Exception
	{
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
		
		emissionPort.sendEvent(new HealthEvent(occurrence));
		
	}

	@Override
	public void			trackingAlarm(
		AbsolutePosition position,
		String personId,
		LocalTime occurrence
		) throws Exception
	{
		super.trackingAlarm(position, personId, occurrence);
	}

	@Override
	public void			manualSignal(
		String personId,
		LocalTime occurrence
		) throws Exception
	{
		super.manualSignal(personId, occurrence);
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
	}

	@Override
	public void			atDestination(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		super.atDestination(vehicleId, occurrence);
	}

	@Override
	public void			atStation(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		super.atStation(vehicleId, occurrence);
	}

	@Override
	public void			notifyMedicsAvailable(LocalTime occurrence)
	throws Exception
	{
		super.notifyMedicsAvailable(occurrence);
	}

	@Override
	public void			notifyNoMedicAvailable(LocalTime occurrence)
	throws Exception
	{
		super.notifyNoMedicAvailable(occurrence);
	}

	@Override
	public void			notifyAmbulancesAvailable(LocalTime occurrence)
	throws Exception
	{
		super.notifyAmbulancesAvailable(occurrence);
	}

	@Override
	public void			notifyNoAmbulanceAvailable(LocalTime occurrence)
	throws Exception
	{
		super.notifyNoAmbulanceAvailable(occurrence);
	}

	@Override
	public ResponseI execute(ActionI a, Serializable[] params) throws Exception {
		return null;
	}
	
}
