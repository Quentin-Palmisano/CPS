package components;

import java.util.Iterator;

import components.interfaces.ActionExecutionCI;
import components.interfaces.CEPBusManagementCI;
import components.interfaces.EventEmissionCI;
import components.interfaces.EventReceptionCI;
import correlator.TrafficCorrelatorState;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import rules.*;

@OfferedInterfaces(offered={EventReceptionCI.class})
@RequiredInterfaces(required={ActionExecutionCI.class, EventEmissionCI.class, CEPBusManagementCI.class})
public class TrafficCorrelator extends Correlator {

	public final IntersectionPosition position;
	
	protected TrafficCorrelator(IntersectionPosition position) throws Exception {
		super(getURI(position), TrafficLight.getURI(position), new TrafficCorrelatorState(position), new RuleBase());
		
		this.position = position;
		
		ruleBase.addRule(new C01());
		ruleBase.addRule(new C02());
		
		
		this.getTracer().setTitle(uri);
		this.getTracer().setRelativePosition(3, 2);
		this.toggleTracing();
		
	}
	
	@Override
	public synchronized void	execute() throws Exception
	{
		super.execute();
		
		Iterator<String> samuStationsIditerator = SmartCityDescriptor.createSAMUStationIdIterator();
		while (samuStationsIditerator.hasNext()) {
			String samuStationId = samuStationsIditerator.next();
			
			managementPort.subscribe(uri, SAMUStation.getURI(samuStationId));
			
		}
		
		Iterator<String> fireStationsIditerator = SmartCityDescriptor.createFireStationIdIterator();
		while (fireStationsIditerator.hasNext()) {
			String fireStationId = fireStationsIditerator.next();
			
			managementPort.subscribe(uri, FireStation.getURI(fireStationId));
			
		}
		
	}
	
	public static String getURI(IntersectionPosition position) {
		return "TrafficCorrelator " + position;
	}
}
