package components.interfaces;

import java.io.Serializable;

import interfaces.ActionI;
import interfaces.ResponseI;

public interface ActionExecutionI {
	
	public ResponseI executeAction(ActionI a, Serializable[] params) throws Exception;

}
