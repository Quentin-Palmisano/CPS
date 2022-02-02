package classes;

import java.util.ArrayList;

import interfaces.CorrelatorStateI;
import interfaces.EventBaseI;
import interfaces.EventI;
import interfaces.RuleI;

public abstract class RuleBase {

	private ArrayList<RuleI> rules;
	
	public RuleBase(ArrayList<RuleI> rules) {
		this.rules = rules;
	}
	
	public void addRule(RuleI r) {
		rules.add(r);
	}
	
	public boolean fireFirstOn(EventBaseI eb, CorrelatorStateI c) {
		for(RuleI r : rules) {
			ArrayList<EventI> matchedEvents = r.match(eb);
			if(matchedEvents!=null) {
				if(r.correlate(matchedEvents)) {
					if(r.filter(matchedEvents, c)){
						r.act(matchedEvents, c);
						r.update(matchedEvents, eb);
						return true;
					}
				}
			}
		}
		return false;		
	}
	
	public boolean fireAllOn(EventBaseI eb, CorrelatorStateI c) {
		boolean b = false;
		while(fireFirstOn(eb, c)) b=true;
		return b;
		
	}

}
