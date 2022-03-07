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

		Iterator<String> samuStationsIditerator =
					BasicSimSmartCityDescriptor.createSAMUStationIdIterator();
		while (samuStationsIditerator.hasNext()) {
			String samuStationId = samuStationsIditerator.next();
			String notificationInboundPortURI = AbstractPort.generatePortURI();
			this.register(samuStationId, notificationInboundPortURI);
			AbstractComponent.createComponent(
					SAMUStation.class.getCanonicalName(),
					new Object[]{
							samuStationId,
							notificationInboundPortURI,
							BasicSimSmartCityDescriptor.
										getActionInboundPortURI(samuStationId)
							});
		}


		super.deploy();
	}

	@Test
	void CEPBusTest() {
		String bus;
		try {
			 bus = AbstractComponent.createComponent(CEPBus.class.getCanonicalName(), new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		try {
			BasicSimFacadeCVM c = new BasicSimFacadeCVM();
			c.startStandardLifeCycle(10000L);
			Thread.sleep(100000L);
			System.exit(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
}
