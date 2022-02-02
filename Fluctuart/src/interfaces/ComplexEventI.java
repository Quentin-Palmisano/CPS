package interfaces;

import java.util.ArrayList;

/**
 * @author Quentin
 *
 */
public interface ComplexEventI extends EventI {
	
	public ArrayList<EventI> getCorrelatedEvents();

}
