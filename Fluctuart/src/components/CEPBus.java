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
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import interfaces.EventI;
import ports.CEPBusManagementInboundPort;
import ports.EventEmissionInboundPort;
import ports.EventReceptionOutboundPort;

@OfferedInterfaces(offered={EventEmissionCI.class, CEPBusManagementCI.class, EventEmissionCI.class})
@RequiredInterfaces(required={EventReceptionCI.class})
public class CEPBus extends AbstractComponent implements CEPBusManagementI, EventEmissionI{

	public static final String ManagementURI = "CEPBUS_MANAGEMENT_URI";
	
	private class EventReceptionOutboundConnection {
		public EventReceptionOutboundConnection(String inboundPortURI) throws Exception {
			receptionPort = new EventReceptionOutboundPort(CEPBus.this);
			receptionPort.localPublishPort();

			CEPBus.this.doPortConnection(receptionPort.getPortURI(), inboundPortURI, EventReceptionConnector.class.getCanonicalName());
		}
		public void destroy() throws Exception {
			CEPBus.this.doPortDisconnection(receptionPort.getPortURI());
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
		this.getTracer().setRelativePosition(0, 0);
		this.toggleTracing();
		
	}
	
	
	@Override
	public synchronized void finalise() throws Exception {
		
		Thread.sleep(2000);
		
		for(String uri : correlators.keySet()) {
			EventReceptionOutboundConnection conn = correlators.get(uri);
			conn.destroy();
		}
		correlators.clear();
		
		super.finalise();
	}

	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.emissionPort.unpublishPort();
			this.managementPort.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
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
		correlators.put(uri, new EventReceptionOutboundConnection(inboundPortURI));
		return emissionPort.getPortURI();
	}

	@Override
	public void unregisterCorrelator(String uri) throws Exception {
		correlators.get(uri).destroy();
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
