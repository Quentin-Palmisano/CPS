package ports;

import java.io.Serializable;

import components.interfaces.ActionExecutionCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ActionI;
import interfaces.ResponseI;

public class ActionExecutionInboundPort extends AbstractInboundPort implements ActionExecutionCI {

	public ActionExecutionInboundPort(ComponentI owner) throws Exception {
		super(ActionExecutionCI.class, owner);
	}
	
	public ActionExecutionInboundPort(String URI, ComponentI owner) throws Exception {
		super(URI, ActionExecutionCI.class, owner);
	}
	
	@Override
	public ResponseI execute(ActionI a, Serializable[] params) throws Exception {
		return this.getOwner().handleRequest(owner -> {
			try {
				return ((ActionExecutionCI)owner).execute(a, params);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		});
	}

}
