package test;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import components.CEPBus;
import components.SAMUStation;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.cps.smartcity.AbstractBasicSimCVM;
import fr.sorbonne_u.cps.smartcity.BasicSimFacadeCVM;
import fr.sorbonne_u.cps.smartcity.BasicSimSmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.components.FireStationFacade;
import fr.sorbonne_u.cps.smartcity.components.SAMUStationFacade;
import fr.sorbonne_u.cps.smartcity.components.TrafficLightFacade;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;

class TestCEPBus extends AbstractBasicSimCVM {

	public TestCEPBus() throws Exception {
		super();
	}
	
	public void			deploy() throws Exception
	{
		
		AbstractComponent.createComponent(CEPBus.class.getCanonicalName(), new Object[0]);
		

		Iterator<String> fireStationIdsIterator =
					BasicSimSmartCityDescriptor.createFireStationIdIterator();
		while (fireStationIdsIterator.hasNext()) {
			String fireStationId = fireStationIdsIterator.next();
			String notificationInboundPortURI = AbstractPort.generatePortURI();
			this.register(fireStationId, notificationInboundPortURI);
			AbstractComponent.createComponent(
				FireStationFacade.class.getCanonicalName(),
				new Object[]{
						fireStationId,
						notificationInboundPortURI,
						BasicSimSmartCityDescriptor.
										getActionInboundPortURI(fireStationId)
						});
		}

		Iterator<String> samuStationsIditerator =
					BasicSimSmartCityDescriptor.createSAMUStationIdIterator();
		while (samuStationsIditerator.hasNext()) {
			String samuStationId = samuStationsIditerator.next();
			String notificationInboundPortURI = AbstractPort.generatePortURI();
			this.register(samuStationId, notificationInboundPortURI);
			
			String uri = "SAMUStation " + samuStationId;
			
			AbstractComponent.createComponent(
					SAMUStation.class.getCanonicalName(),
					new Object[]{
							uri,
							samuStationId,
							notificationInboundPortURI,
							BasicSimSmartCityDescriptor.
										getActionInboundPortURI(samuStationId)
							});
			
		}

		Iterator<IntersectionPosition> trafficLightsIterator =
					BasicSimSmartCityDescriptor.createTrafficLightPositionIterator();
		while (trafficLightsIterator.hasNext()) {
			IntersectionPosition p = trafficLightsIterator.next();
			String notificationInboundPortURI = AbstractPort.generatePortURI();
			this.register(p.toString(), notificationInboundPortURI);
			AbstractComponent.createComponent(
					TrafficLightFacade.class.getCanonicalName(),
					new Object[]{
							p,
							notificationInboundPortURI,
							BasicSimSmartCityDescriptor.
												getActionInboundPortURI(p)
							});
		}
		
		
		super.deploy();
	}
	
	public static void main(String[] args) {
		try {
			TestCEPBus c = new TestCEPBus();
			c.startStandardLifeCycle(10000L);
			Thread.sleep(100000L);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}