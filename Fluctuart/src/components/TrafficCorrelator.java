package components;

import components.interfaces.ActionExecutionCI;
import components.interfaces.CEPBusManagementCI;
import components.interfaces.EventEmissionCI;
import components.interfaces.EventReceptionCI;
import correlator.TrafficCorrelatorState;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import rules.RuleBase;

@OfferedInterfaces(offered={EventReceptionCI.class})
@RequiredInterfaces(required={ActionExecutionCI.class, EventEmissionCI.class, CEPBusManagementCI.class})
public class TrafficCorrelator extends Correlator {

	public final IntersectionPosition position;
	
	protected TrafficCorrelator(IntersectionPosition position) throws Exception {
		super(getURI(position), TrafficLight.getURI(position), new TrafficCorrelatorState(position), new RuleBase());
		
		this.position = position;

		this.getTracer().setTitle(uri);
		this.getTracer().setRelativePosition(3, 2);
		this.toggleTracing();
		
	}
	
	public static String getURI(IntersectionPosition position) {
		return "TrafficCorrelator " + position;
	}
}
