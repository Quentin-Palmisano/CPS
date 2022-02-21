package components.interfaces;

import fr.sorbonne_u.components.interfaces.ComponentInterface;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface CEPBusManagementCI extends ComponentInterface, RequiredCI, OfferedCI {
	
	public String registerEmitter(String uri);
	public void unregisterEmitter(String uri);
	public String registerCorrelator(String uri, String inboundPortURI);
	public void unregisterCorrelator(String uri);
	public void registerExecutor(String uri, String inboundPortURI);
	public String getExecutorInboundPortURI(String uri);
	public void unregisterExecutor(String uri);
	public void subscribe(String subscriberURI, String emitterURI);
	public void unsubscribe(String subscriberURI, String emitterURI);

}
