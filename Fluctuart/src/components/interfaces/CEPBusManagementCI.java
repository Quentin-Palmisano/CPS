package components.interfaces;

import fr.sorbonne_u.components.interfaces.ComponentInterface;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface CEPBusManagementCI extends ComponentInterface, RequiredCI, OfferedCI {
	
	public String registerEmitter(String uri) throws Exception;
	public void unregisterEmitter(String uri) throws Exception;
	public String registerCorrelator(String uri, String inboundPortURI) throws Exception;
	public void unregisterCorrelator(String uri) throws Exception;
	public void registerExecutor(String uri, String inboundPortURI) throws Exception;
	public String getExecutorInboundPortURI(String uri);
	public void unregisterExecutor(String uri) throws Exception;
	public void subscribe(String subscriberURI, String emitterURI);
	public void unsubscribe(String subscriberURI, String emitterURI);

}
