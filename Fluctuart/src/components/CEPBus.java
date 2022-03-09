package components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import components.interfaces.CEPBusManagementCI;
import components.interfaces.EventEmissionCI;
import components.interfaces.EventReceptionCI;
import connectors.EventReceptionConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import interfaces.EventI;
import ports.EventEmissionInboundPort;
import ports.EventReceptionOutboundPort;

@OfferedInterfaces(offered={EventEmissionCI.class})
@RequiredInterfaces(required={EventReceptionCI.class})
public class CEPBus extends AbstractComponent implements CEPBusManagementCI, EventEmissionCI {

	//public static final String URI = "URI_BUS";
	
	public static CEPBus BUS;
	
	private class EventReceptionOutboundConnection {
		public EventReceptionOutboundConnection(CEPBus bus, String inboundPortURI) throws Exception {
			receptionPort = new EventReceptionOutboundPort(bus);
			receptionPort.localPublishPort();

			bus.doPortConnection(inboundPortURI, receptionPort.getPortURI(), EventReceptionConnector.class.getCanonicalName());
		}
		public void destroy(CEPBus bus) throws Exception {
			bus.doPortDisconnection(receptionPort.getPortURI());
			receptionPort.unpublishPort();
		}
		EventReceptionOutboundPort receptionPort;
	}
	
	private final HashSet<String> emitters = new HashSet<>();
	private final HashMap<String, EventReceptionOutboundConnection> correlators = new HashMap<>();
	private final HashMap<String, ArrayList<String>> subscriptions = new HashMap<>();
	
	private final EventEmissionInboundPort emissionPort;
	
	public CEPBus() throws Exception {
		super(1, 1);
		BUS = this;
		
		emissionPort = new EventEmissionInboundPort(this);
		emissionPort.publishPort();
		
		this.getTracer().setTitle("CEPBus");
		this.getTracer().setRelativePosition(0, 0);
		this.toggleTracing();
		
	}

	@Override
	public void sendEvent(String emitterURI, EventI event) throws Exception {
		
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
		emitters.add(uri);
		return emissionPort.getPortURI();
	}

	@Override
	public void unregisterEmitter(String uri) throws Exception {
		emitters.remove(uri);
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
	}

	@Override
	public String getExecutorInboundPortURI(String uri) {
		return null;
	}

	@Override
	public void unregisterExecutor(String uri) throws Exception {
		
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
