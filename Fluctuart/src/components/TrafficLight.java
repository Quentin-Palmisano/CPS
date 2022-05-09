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
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
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

	}
	
	@Override
	public synchronized void	execute() throws Exception
	{
		super.execute();
		
		String ibp = managementPort.registerEmitter(uri);
		this.doPortConnection(emissionPort.getPortURI(), ibp, EventEmissionConnector.class.getCanonicalName());
		
		managementPort.registerExecutor(uri, actionPort.getPortURI());
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
	public ResponseI executeAction(ActionI a, Serializable[] params) throws Exception {
		TrafficAction ta = (TrafficAction) a;
		if(ta == TrafficAction.PRIORITY_CHANGE) {
			actionOBP.changePriority((TypeOfTrafficLightPriority) params[0]);
		}
		return null;
	}

}
