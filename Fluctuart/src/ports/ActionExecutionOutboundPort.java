package ports;

import java.io.Serializable;

import components.interfaces.ActionExecutionCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ActionI;

public class ActionExecutionOutboundPort extends AbstractOutboundPort implements ActionExecutionCI {

	public ActionExecutionOutboundPort(ComponentI owner) throws Exception {
		super(ActionExecutionCI.class, owner);
	}
	
	public ActionExecutionOutboundPort(String URI, ComponentI owner) throws Exception {
		super(URI, ActionExecutionCI.class, owner);
	}
	
	@Override
	public void executeAction(ActionI a, Serializable[] params) throws Exception {
		((ActionExecutionCI) this.getConnector()).executeAction(a, params);
	}

}
