package components.interfaces;

import java.io.Serializable;

import interfaces.ActionI;

public interface ActionExecutionI {
	
	public void executeAction(ActionI a, Serializable[] params) throws Exception;

}
