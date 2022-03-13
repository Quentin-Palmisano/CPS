package actions;

import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import interfaces.ActionI;

public class HealthAction implements ActionI {

	public AbsolutePosition position;
	public String personId;
	public TypeOfSAMURessources type;
	
	private static final long serialVersionUID = -1081522029620557118L;
	
	public HealthAction() {}
	
	public HealthAction(AbsolutePosition position, String personId, TypeOfSAMURessources type) {
		this.position=position;
		this.personId=personId;
		this.type=type;
	}

}
