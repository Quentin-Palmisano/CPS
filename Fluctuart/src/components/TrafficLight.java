package components;

import fr.sorbonne_u.cps.smartcity.components.TrafficLightFacade;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;

public class TrafficLight extends TrafficLightFacade {
	
	protected TrafficLight(IntersectionPosition position, String notificationInboundPortURI,
			String actionInboundPortURI) throws Exception {
		super(position, notificationInboundPortURI, actionInboundPortURI);
	}

	@Override
	public synchronized void	execute() throws Exception
	{
		
	}

}
