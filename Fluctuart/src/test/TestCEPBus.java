package test;

import java.time.LocalTime;
import java.util.Iterator;

import components.CEPBus;
import components.FireCorrelator;
import components.FireStation;
import components.HealthCorrelator;
import components.SAMUStation;
import components.TrafficCorrelator;
import components.TrafficLight;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.cps.smartcity.AbstractSmartCityCVM;
import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.utils.TimeManager;

class TestCEPBus extends AbstractSmartCityCVM {

	public TestCEPBus() throws Exception {
		super();
	}
	
	public void deploy() throws Exception
	{
		
		AbstractComponent.createComponent(CEPBus.class.getCanonicalName(), new Object[0]);
		
		Iterator<String> samuStationsIditerator =
					SmartCityDescriptor.createSAMUStationIdIterator();
		while (samuStationsIditerator.hasNext()) {
			String samuStationId = samuStationsIditerator.next();
			String notificationInboundPortURI = AbstractPort.generatePortURI();
			this.register(samuStationId, notificationInboundPortURI);

			AbstractComponent.createComponent(
					SAMUStation.class.getCanonicalName(),
					new Object[]{
							samuStationId,
							notificationInboundPortURI,
							SmartCityDescriptor.
										getActionInboundPortURI(samuStationId)
							});
			
			AbstractComponent.createComponent(HealthCorrelator.class.getCanonicalName(), new Object[] {samuStationId});
			
		}
		

		
		Iterator<String> fireStationIdsIterator =
				SmartCityDescriptor.createFireStationIdIterator();
		while (fireStationIdsIterator.hasNext()) {
			String fireStationId = fireStationIdsIterator.next();
			String notificationInboundPortURI = AbstractPort.generatePortURI();
			this.register(fireStationId, notificationInboundPortURI);
			
			AbstractComponent.createComponent(
				FireStation.class.getCanonicalName(),
				new Object[]{
						fireStationId,
						notificationInboundPortURI,
						SmartCityDescriptor.
										getActionInboundPortURI(fireStationId)
						});
			
			AbstractComponent.createComponent(FireCorrelator.class.getCanonicalName(), new Object[] {fireStationId});
			
		}

		
		Iterator<IntersectionPosition> trafficLightsIterator =
				SmartCityDescriptor.createTrafficLightPositionIterator();
		while (trafficLightsIterator.hasNext()) {
			IntersectionPosition p = trafficLightsIterator.next();
			String notificationInboundPortURI = AbstractPort.generatePortURI();
			this.register(p.toString(), notificationInboundPortURI);
			
			AbstractComponent.createComponent(
					TrafficLight.class.getCanonicalName(),
					new Object[]{
							p,
							notificationInboundPortURI,
							SmartCityDescriptor.
												getActionInboundPortURI(p)
							});
			

			AbstractComponent.createComponent(TrafficCorrelator.class.getCanonicalName(), new Object[] {p});
		}
		
		
		super.deploy();
	}
	
	public static void main(String[] args) {
		try {
			simulatedStartTime = LocalTime.of(12, 0);
			simulatedEndTime = LocalTime.of(12, 0).plusMinutes(200);
			TestCEPBus c = new TestCEPBus();
			c.startStandardLifeCycle(
					TimeManager.get().computeExecutionDuration() + START_DELAY);
			Thread.sleep(10000L);
			System.exit(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
