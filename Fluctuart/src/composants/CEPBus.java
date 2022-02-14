package composants;

import composants.interfaces.*;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;

@OfferedInterfaces(offered={EventEmissionCI.class})
@RequiredInterfaces(required={EventReceptionCI.class})
public class CEPBus extends AbstractComponent{

	protected CEPBus() {
		super(1, 1);
	}

	

}
