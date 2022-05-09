package connectors;

import java.io.Serializable;

import components.interfaces.ActionExecutionCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ActionI;
import interfaces.ResponseI;

public class ActionExecutionConnector extends AbstractConnector implements ActionExecutionCI {

	@Override
	public ResponseI executeAction(ActionI a, Serializable[] params) throws Exception {
		return ((ActionExecutionCI) this.offering).executeAction(a, params);
	}
	
}
