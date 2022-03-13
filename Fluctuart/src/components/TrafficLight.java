package components;

import java.io.Serializable;
import java.time.LocalTime;

import components.interfaces.ActionExecutionCI;
import connectors.CEPBusManagementConnector;
import connectors.EventEmissionConnector;
import events.AtomicEvent;
import fr.sorbonne_u.cps.smartcity.components.SAMUStationFacade;
import fr.sorbonne_u.cps.smartcity.components.TrafficLightFacade;
import fr.sorbonne_u.cps.smartcity.grid.Direction;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import interfaces.ActionI;
import interfaces.ResponseI;
import ports.ActionExecutionInboundPort;
import ports.CEPBusManagementOutboundPort;
import ports.EventEmissionOutboundPort;

public class TrafficLight extends TrafficLightFacade implements ActionExecutionCI{

	protected final CEPBusManagementOutboundPort managementPort;
	private EventEmissionOutboundPort emissionPort;
	private ActionExecutionInboundPort actionPort;

	public final String uri;

	protected TrafficLight(String uri, IntersectionPosition position, String notificationInboundPortURI,
			String actionInboundPortURI) throws Exception {
		super(position, notificationInboundPortURI, actionInboundPortURI);
		
		this.uri = uri;
		
		managementPort = new CEPBusManagementOutboundPort(this);
		managementPort.localPublishPort();
		this.doPortConnection(managementPort.getPortURI(), CEPBus.ManagementURI, CEPBusManagementConnector.class.getCanonicalName());

		emissionPort = new EventEmissionOutboundPort(this);
		emissionPort.localPublishPort();

		actionPort = new ActionExecutionInboundPort(this);
		actionPort.publishPort();

		String ibp = managementPort.registerEmitter(uri);
		this.doPortConnection(emissionPort.getPortURI(), ibp, EventEmissionConnector.class.getCanonicalName());

		managementPort.registerExecutor(uri, actionPort.getPortURI());

	}

	@Override
	public synchronized void	execute() throws Exception
	{

	}
	
	
	//events
	
	@Override
	public void	vehiclePassage(String vehicleId, Direction d, LocalTime occurrence) throws Exception {
		super.vehiclePassage(vehicleId, d, occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		event.putProperty("name", "vehiclePassage");
		event.putProperty("vehicleId", vehicleId);
		event.putProperty("direction", d);
		emissionPort.sendEvent(uri, event);
	}



	@Override
	public ResponseI execute(ActionI a, Serializable[] params) throws Exception {
		actionOBP.changePriority((TypeOfTrafficLightPriority) params[0]);
		return null;
	}

}
