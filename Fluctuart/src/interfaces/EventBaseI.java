package interfaces;

import java.time.Duration;
import java.util.ArrayList;

public interface EventBaseI {

	public void addEvent(EventI e);
	
	public void removeEvent(EventI e);
	
	public EventI getEvent(int i);
	public ArrayList<EventI> getEvents();
	
	public int numberOfEvents();
	
	public boolean appearsIn(EventI e);
	
	public void clearEvents(Duration d);
	
}
