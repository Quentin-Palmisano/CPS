package components;

import java.util.ArrayList;
import java.util.HashMap;

import components.interfaces.CEPBusManagementCI;
import components.interfaces.CEPBusManagementI;
import components.interfaces.EventEmissionCI;
import components.interfaces.EventEmissionI;
import components.interfaces.EventReceptionCI;
import connectors.EventReceptionConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import interfaces.EventI;
import ports.CEPBusManagementInboundPort;
import ports.EventEmissionInboundPort;
import ports.EventReceptionOutboundPort;

@OfferedInterfaces(offered={EventEmissionCI.class, CEPBusManagementCI.class})
@RequiredInterfaces(required={EventReceptionCI.class})
public class CEPBus extends AbstractComponent implements CEPBusManagementI, EventEmissionI {

	public static final String ManagementURI = "CEPBUS_MANAGEMENT_URI";
	
	private class EventReceptionOutboundConnection {
		public EventReceptionOutboundConnection(CEPBus bus, String inboundPortURI) throws Exception {
			receptionPort = new EventReceptionOutboundPort(bus);
			receptionPort.localPublishPort();

			bus.doPortConnection(receptionPort.getPortURI(), inboundPortURI, EventReceptionConnector.class.getCanonicalName());
		}
		public void destroy(CEPBus bus) throws Exception {
			bus.doPortDisconnection(receptionPort.getPortURI());
			receptionPort.unpublishPort();
		}
		EventReceptionOutboundPort receptionPort;
	}
	
	private final HashMap<String, EventReceptionOutboundConnection> correlators = new HashMap<>();
	private final HashMap<String, ArrayList<String>> subscriptions = new HashMap<>();
	private final HashMap<String, String> executors = new HashMap<>();
	
	private final EventEmissionInboundPort emissionPort;
	private final CEPBusManagementInboundPort managementPort;
	
	protected CEPBus() throws Exception {
		super(1, 1);
		
		emissionPort = new EventEmissionInboundPort(this);
		emissionPort.publishPort();
		
		managementPort = new CEPBusManagementInboundPort(ManagementURI, this);
		managementPort.publishPort();
		
		this.getTracer().setTitle("CEPBus");
		this.getTracer().setRelativePosition(2, 0);
		this.toggleTracing();
		
	}

	@Override
	public void sendEvent(String emitterURI, EventI event) throws Exception {

		this.traceMessage("Event received from " + emitterURI + " of name " + event.getPropertyValue("name") + "\n");
		
		if(subscriptions.containsKey(emitterURI)) {
			ArrayList<String> subscribers = subscriptions.get(emitterURI);
			for(String subscriber : subscribers) {
				EventReceptionOutboundConnection correlation = correlators.get(subscriber);
				correlation.receptionPort.receiveEvent(emitterURI, event);
			}
		}
		
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) throws Exception {
		
		for(EventI event : events) {
			this.traceMessage("Events received from " + emitterURI + " of type " + event.getPropertyValue("name") + "\n");
		}
		
		if(subscriptions.containsKey(emitterURI)) {
			ArrayList<String> subscribers = subscriptions.get(emitterURI);
			for(String subscriber : subscribers) {
				EventReceptionOutboundConnection correlation = correlators.get(subscriber);
				correlation.receptionPort.receiveEvents(emitterURI, events);
			}
		}
		
	}
	
	

	@Override
	public String registerEmitter(String uri) throws Exception {
		return emissionPort.getPortURI();
	}

	@Override
	public void unregisterEmitter(String uri) throws Exception {
		
	}

	@Override
	public String registerCorrelator(String uri, String inboundPortURI) throws Exception {
		correlators.put(uri, new EventReceptionOutboundConnection(this, inboundPortURI));
		return emissionPort.getPortURI();
	}

	@Override
	public void unregisterCorrelator(String uri) throws Exception {
		correlators.get(uri).destroy(this);
		correlators.remove(uri);
	}

	@Override
	public void registerExecutor(String uri, String inboundPortURI) throws Exception {
		executors.put(uri, inboundPortURI);
	}

	@Override
	public String getExecutorInboundPortURI(String uri) {
		return executors.get(uri);
	}

	@Override
	public void unregisterExecutor(String uri) throws Exception {
		executors.remove(uri);
		
	}
	
	@Override
	public void subscribe(String subscriberURI, String emitterURI) {
		ArrayList<String> subscribers = subscriptions.get(emitterURI);
		if(subscribers == null) {
			subscribers = new ArrayList<String>();
			subscriptions.put(emitterURI, subscribers);
		}
		subscribers.add(subscriberURI);
	}

	@Override
	public void unsubscribe(String subscriberURI, String emitterURI) {
		ArrayList<String> subscribers = subscriptions.get(emitterURI);
		assert subscribers != null;
		assert subscribers.contains(subscriberURI);
		subscribers.remove(subscriberURI);
	}

}
