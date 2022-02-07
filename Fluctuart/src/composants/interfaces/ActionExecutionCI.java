package composants.interfaces;

import java.io.Serializable;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import interfaces.ActionI;
import interfaces.ResponseI;

public interface ActionExecutionCI extends RequiredCI, OfferedCI {
	
	public ResponseI execute(ActionI a, Serializable[] params);

}
