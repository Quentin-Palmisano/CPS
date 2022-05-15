package ports;

import java.io.Serializable;

import components.interfaces.ActionExecutionCI;
import components.interfaces.ActionExecutionI;
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
	public void executeAction(ActionI a, Serializable[] params) throws Exception {
		this.getOwner().runTask(owner -> {
			try {
				((ActionExecutionI)owner).executeAction(a, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
