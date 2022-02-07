package composants;

import composants.interfaces.CEPBusManagementCI;
import composants.interfaces.EventEmissionCI;
import interfaces.EventI;

public class CEPBus implements EventEmissionCI, CEPBusManagementCI{

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
