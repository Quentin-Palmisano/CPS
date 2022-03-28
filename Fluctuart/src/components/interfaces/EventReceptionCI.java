package components.interfaces;

import fr.sorbonne_u.components.interfaces.ComponentInterface;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import interfaces.EventI;

public interface EventReceptionCI extends EventReceptionI, ComponentInterface, RequiredCI, OfferedCI {

}
