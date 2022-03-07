package components;

import java.util.HashMap;

import components.interfaces.CEPBusManagementCI;
import components.interfaces.EventEmissionCI;
import components.interfaces.EventReceptionCI;
import connectors.EventEmissionConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import interfaces.EventI;
import ports.EventEmissionInboundPort;

@OfferedInterfaces(offered={EventEmissionCI.class})
@RequiredInterfaces(required={EventReceptionCI.class})
public class CEPBus extends AbstractComponent implements CEPBusManagementCI, EventEmissionCI, EventReceptionCI {

	public static final String URI = "URI_BUS";
	
	public static CEPBus CEPBUS;
	
	private final HashMap<String, String> emitters = new HashMap<>();
	//private final HashMap<String, String> subscribers = new HashMap<>();
	
	private final EventEmissionInboundPort emissionPort;
	
	public CEPBus() throws Exception {
		super(1, 1);
		CEPBUS = this;
		
		emissionPort = new EventEmissionInboundPort(URI, this);
		emissionPort.publishPort();
		
		this.getTracer().setTitle("CEPBus");
		this.getTracer().setRelativePosition(0, 0);
		this.toggleTracing();
		
	}

	@Override
	public void receiveEvent(String emitterURI, EventI event) {
		
	}

	@Override
	public void receiveEvents(String emitterURI, EventI[] events) {
		
	}

	@Override
	public void sendEvent(String emitterURI, EventI event) {
		this.traceMessage("Event received of type " + event.getPropertyValue("type"));
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) {
		
	}

	@Override
	public String registerEmitter(String uri) throws Exception {
		emitters.put(uri, URI);
		this.doPortConnection(uri, URI, EventEmissionConnector.class.getCanonicalName());
		return URI;
	}

	@Override
	public void unregisterEmitter(String uri) throws Exception {
		emitters.remove(uri);
		this.doPortDisconnection(uri);
	}

	@Override
	public String registerCorrelator(String uri, String inboundPortURI) throws Exception {
		return null;
	}

	@Override
	public void unregisterCorrelator(String uri) throws Exception {
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
	}

	@Override
	public void unsubscribe(String subscriberURI, String emitterURI) {
	}
	
	

}
