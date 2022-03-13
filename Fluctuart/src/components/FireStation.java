package components;

import fr.sorbonne_u.cps.smartcity.components.FireStationFacade;

public class FireStation extends FireStationFacade {

	protected FireStation(String stationId, String notificationInboundPortURI, String actionInboundPortURI)
			throws Exception {
		super(stationId, notificationInboundPortURI, actionInboundPortURI);
	}
	
	@Override
	public synchronized void	execute() throws Exception
	{
		
	}

}
