package components;

import java.io.Serializable;
import java.time.LocalTime;

import classes.AtomicEvent;
import components.interfaces.ActionExecutionCI;
import connectors.EventEmissionConnector;
import fr.sorbonne_u.cps.smartcity.components.SAMUStationFacade;
import fr.sorbonne_u.cps.smartcity.components.TrafficLightFacade;
import fr.sorbonne_u.cps.smartcity.grid.Direction;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import interfaces.ActionI;
import interfaces.ResponseI;
import ports.ActionExecutionInboundPort;
import ports.EventEmissionOutboundPort;

public class TrafficLight extends TrafficLightFacade implements ActionExecutionCI{

	private EventEmissionOutboundPort emissionPort;
	private ActionExecutionInboundPort actionPort;

	public final String uri;

	protected TrafficLight(String uri, IntersectionPosition position, String notificationInboundPortURI,
			String actionInboundPortURI) throws Exception {
		super(position, notificationInboundPortURI, actionInboundPortURI);
		
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
	
	
	//events
	
	@Override
	public void	vehiclePassage(String vehicleId, Direction d, LocalTime occurrence) throws Exception {
		super.vehiclePassage(vehicleId, d, occurrence);
		AtomicEvent event = new AtomicEvent(occurrence);
		event.putProperty("vehicleId", vehicleId);
		event.putProperty("direction", d);
		emissionPort.sendEvent(uri, event);
	}



	@Override
	public ResponseI execute(ActionI a, Serializable[] params) throws Exception {
		return null;
	}

}
