package components;

import java.io.Serializable;
import java.time.LocalTime;

import actions.TrafficAction;
import components.interfaces.ActionExecutionCI;
import components.interfaces.ActionExecutionI;
import components.interfaces.CEPBusManagementCI;
import components.interfaces.EventEmissionCI;
import connectors.CEPBusManagementConnector;
import connectors.EventEmissionConnector;
import events.AtomicEvent;
import events.TrafficLightEventName;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.smartcity.components.TrafficLightFacade;
import fr.sorbonne_u.cps.smartcity.grid.Direction;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightActionCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightNotificationCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import interfaces.ActionI;
import interfaces.ResponseI;
import ports.ActionExecutionInboundPort;
import ports.CEPBusManagementOutboundPort;
import ports.EventEmissionOutboundPort;

@OfferedInterfaces(offered={TrafficLightNotificationCI.class, ActionExecutionCI.class})
@RequiredInterfaces(required={TrafficLightActionCI.class, EventEmissionCI.class, CEPBusManagementCI.class})
public class TrafficLight extends TrafficLightFacade implements ActionExecutionI {

	protected final CEPBusManagementOutboundPort managementPort;
	private EventEmissionOutboundPort emissionPort;
	private ActionExecutionInboundPort actionPort;

	public final String uri;

	protected TrafficLight(IntersectionPosition position, String notificationInboundPortURI, String actionInboundPortURI) throws Exception {
		super(position, notificationInboundPortURI, actionInboundPortURI);
		
		this.uri = getURI(position);
		
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
	
	
	//events
	
	@Override
	public void	vehiclePassage(String vehicleId, Direction d, LocalTime occurrence) throws Exception {
		super.vehiclePassage(vehicleId, d, occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		event.putProperty("name", TrafficLightEventName.VEHICLE_PASSAGE);
		event.putProperty("vehicleId", vehicleId);
		event.putProperty("direction", d);
		emissionPort.sendEvent(uri, event);
	}



	@Override
	public void executeAction(ActionI a, Serializable[] params) throws Exception {
		TrafficAction ta = (TrafficAction) a;
		if(ta == TrafficAction.PRIORITY_CHANGE) {
			actionOBP.changePriority((TypeOfTrafficLightPriority) params[0]);
		}else if(ta == TrafficAction.RETURN_TO_NORMAL_MODE) {
			actionOBP.returnToNormalMode();
		}
	}
	
	public static String getURI(IntersectionPosition position) {
		return "TrafficLight " + position;
	}

}
