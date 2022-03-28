package components.interfaces;

import fr.sorbonne_u.components.interfaces.ComponentInterface;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import interfaces.EventI;

public interface EventEmissionCI extends EventEmissionI, ComponentInterface, RequiredCI, OfferedCI {
	
}
