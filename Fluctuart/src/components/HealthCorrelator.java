package components;

import java.util.Iterator;

import components.interfaces.ActionExecutionCI;
import components.interfaces.CEPBusManagementCI;
import components.interfaces.EventEmissionCI;
import components.interfaces.EventReceptionCI;
import correlator.HealthCorrelatorState;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;
import rules.RuleBase;
import rules.S01;
import rules.S02;
import rules.S03;
import rules.S04;
import rules.S05;
import rules.S06;
import rules.S07;
import rules.S08;
import rules.S09;
import rules.S10;
import rules.S10bis;
import rules.S11;
import rules.S12;
import rules.S12bis;
import rules.S13;
import rules.S14;
import rules.S15;
import rules.S16;
import rules.S17;
import rules.S18;
import rules.S19;
@OfferedInterfaces(offered={EventReceptionCI.class})
@RequiredInterfaces(required={ActionExecutionCI.class, EventEmissionCI.class, CEPBusManagementCI.class})
public class HealthCorrelator extends Correlator {
	
	public final String stationID;
	
	protected HealthCorrelator(String stationID) throws Exception {
		super(getURI(stationID), SAMUStation.getURI(stationID), new HealthCorrelatorState(stationID), new RuleBase());
		this.stationID = stationID;
		
		
		ruleBase.addRule(new S01());
		ruleBase.addRule(new S02());
		ruleBase.addRule(new S03());
		ruleBase.addRule(new S04());
		ruleBase.addRule(new S05());
		ruleBase.addRule(new S06());
		ruleBase.addRule(new S07());
		ruleBase.addRule(new S08());
		ruleBase.addRule(new S09());
		ruleBase.addRule(new S10());
		ruleBase.addRule(new S10bis());
		ruleBase.addRule(new S11());
		ruleBase.addRule(new S12());
		ruleBase.addRule(new S12bis());
		ruleBase.addRule(new S13());
		ruleBase.addRule(new S14());
		ruleBase.addRule(new S15());
		ruleBase.addRule(new S16());
		ruleBase.addRule(new S17());
		ruleBase.addRule(new S18());
		ruleBase.addRule(new S19());
		

		this.getTracer().setTitle(uri);
		this.getTracer().setRelativePosition(3, 0);
		this.toggleTracing();
		
	}
	
	@Override
	public synchronized void	execute() throws Exception
	{
		super.execute();
		
		Iterator<String> samuStationsIditerator = SmartCityDescriptor.createSAMUStationIdIterator();
		while (samuStationsIditerator.hasNext()) {
			String samuStationId = samuStationsIditerator.next();
			if(samuStationId.equals(stationID)) continue;
			
			managementPort.subscribe(uri, getURI(samuStationId));
			
		}
		
	}
	
	public static String getURI(String stationID) {
		return "HealthCorrelator " + stationID;
	}

}
