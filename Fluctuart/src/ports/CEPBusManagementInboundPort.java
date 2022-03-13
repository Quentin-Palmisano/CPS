package ports;

import components.interfaces.CEPBusManagementCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class CEPBusManagementInboundPort extends AbstractInboundPort implements CEPBusManagementCI {

	public CEPBusManagementInboundPort(ComponentI owner) throws Exception {
		super(CEPBusManagementCI.class, owner);
	}
	
	public CEPBusManagementInboundPort(String URI, ComponentI owner) throws Exception {
		super(URI, CEPBusManagementCI.class, owner);
	}

	@Override
	public String registerEmitter(String uri) throws Exception {
		return this.getOwner().handleRequest(owner -> {
			try {
				return ((CEPBusManagementCI)owner).registerEmitter(uri);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		});
	}

	@Override
	public void unregisterEmitter(String uri) throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((CEPBusManagementCI)owner).unregisterEmitter(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public String registerCorrelator(String uri, String inboundPortURI) throws Exception {
		return this.getOwner().handleRequest(owner -> {
			try {
				return ((CEPBusManagementCI)owner).registerCorrelator(uri, inboundPortURI);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		});
	}

	@Override
	public void unregisterCorrelator(String uri) throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((CEPBusManagementCI)owner).unregisterCorrelator(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void registerExecutor(String uri, String inboundPortURI) throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((CEPBusManagementCI)owner).registerExecutor(uri, inboundPortURI);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public String getExecutorInboundPortURI(String uri) throws Exception {
		return this.getOwner().handleRequest(owner -> {
			try {
				return ((CEPBusManagementCI)owner).getExecutorInboundPortURI(uri);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		});
	}

	@Override
	public void unregisterExecutor(String uri) throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((CEPBusManagementCI)owner).unregisterExecutor(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void subscribe(String subscriberURI, String emitterURI) throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((CEPBusManagementCI)owner).subscribe(subscriberURI, emitterURI);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void unsubscribe(String subscriberURI, String emitterURI) throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((CEPBusManagementCI)owner).unsubscribe(subscriberURI, emitterURI);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
