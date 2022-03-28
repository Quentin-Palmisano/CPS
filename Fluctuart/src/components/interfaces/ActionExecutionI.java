package components.interfaces;

import java.io.Serializable;

import fr.sorbonne_u.components.interfaces.ComponentInterface;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import interfaces.ActionI;
import interfaces.ResponseI;

public interface ActionExecutionI {
	
	public ResponseI execute(ActionI a, Serializable[] params) throws Exception;

}
