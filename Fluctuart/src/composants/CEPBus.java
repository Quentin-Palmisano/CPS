package composants;

import composants.interfaces.*;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import interfaces.EventI;

@OfferedInterfaces(offered={EventEmissionCI.class})
@RequiredInterfaces(required={EventReceptionCI.class})
public class CEPBus extends AbstractComponent implements CEPBusManagementCI, EventEmissionCI, EventReceptionCI {

	public static final String URI = "URI_BUS";
	
	protected CEPBus() {
		super(1, 1);
	}

	@Override
	public void receiveEvent(String emitterURI, EventI event) {
	}

	@Override
	public void receiveEvents(String emitterURI, EventI[] events) {
	}

	@Override
	public void sendEvent(String emitterURI, EventI event) {
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) {
	}

	@Override
	public String registerEmitter(String uri) {
		return null;
	}

	@Override
	public void unregisterEmitter(String uri) {
	}

	@Override
	public String registerCorrelator(String uri, String inboundPortURI) {
		return null;
	}

	@Override
	public void unregisterCorrelator(String uri) {
	}

	@Override
	public void registerExecutor(String uri, String inboundPortURI) {
	}

	@Override
	public String getExecutorInboundPortURI(String uri) {
		return null;
	}

	@Override
	public void unregisterExecutor(String uri) {
	}

	@Override
	public void subscribe(String subscriberURI, String emitterURI) {
	}

	@Override
	public void unsubscribe(String subscriberURI, String emitterURI) {
	}


}
