package fr.sorbonne_u.cps.smartcity.interfaces;

// Copyright Jacques Malenfant, Sorbonne Universite.
// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to provide a
// basic component programming model to program with components
// distributed applications in the Java programming language.
//
// This software is governed by the CeCILL-C license under French law and
// abiding by the rules of distribution of free software.  You can use,
// modify and/ or redistribute the software under the terms of the
// CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
// URL "http://www.cecill.info".
//
// As a counterpart to the access to the source code and  rights to copy,
// modify and redistribute granted by the license, users are provided only
// with a limited warranty  and the software's author,  the holder of the
// economic rights,  and the successive licensors  have only  limited
// liability. 
//
// In this respect, the user's attention is drawn to the risks associated
// with loading,  using,  modifying and/or developing or reproducing the
// software by the user in light of its specific status of free software,
// that may mean  that it is complicated to manipulate,  and  that  also
// therefore means  that it is reserved for developers  and  experienced
// professionals having in-depth computer knowledge. Users are therefore
// encouraged to load and test the software's suitability as regards their
// requirements in conditions enabling the security of their systems and/or 
// data to be ensured and,  more generally, to use and operate it in the 
// same conditions as regards security. 
//
// The fact that you are presently reading this means that you have had
// knowledge of the CeCILL-C license and that you accept its terms.

import java.time.LocalTime;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;

// -----------------------------------------------------------------------------
/**
 * The interface <code>SAMUNotificationImplI</code> declares the notifications
 * that can be sent by a SAMU station.
 *
 * <p><strong>Description</strong></p>
 * 
 * Ambulance
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>Created on : 2022-02-04</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public interface		SAMUNotificationImplI
{
	/**
	 * notify an health alarm of the given type at the given position and
	 * time of occurrence.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code position != null}
	 * pre	{@code !type.isTracking()}
	 * pre	{@code occurrence != null}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param position		position at which the alarm occurred.
	 * @param type			type of health alarm.
	 * @param occurrence	time of occurrence of the event.
	 * @throws Exception	<i>to do</i>.
	 */
	public void			healthAlarm(
		AbsolutePosition position,
		TypeOfHealthAlarm type,
		LocalTime occurrence
		) throws Exception;

	/**
	 * notify an health tracking alarm at the given position and time of
	 * occurrence.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code position != null}
	 * pre	{@code personId != null && !personId.isEmpty()}
	 * pre	{@code occurrence != null}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param position		position at which the alarm occurred.
	 * @param personId		identifier of the tracked person.
	 * @param occurrence	time of occurrence of the event.
	 * @throws Exception	<i>to do</i>.
	 */
	public void			trackingAlarm(
		AbsolutePosition position,
		String personId,
		LocalTime occurrence
		) throws Exception;

	/**
	 * manual signal sent by the given tracked person at the given time.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code personId != null && !personId.isEmpty()}
	 * pre	{@code occurrence != null}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param personId		identifier of the tracked person.
	 * @param occurrence	time of occurrence of the event.
	 * @throws Exception	<i>to do</i>.
	 */
	public void			manualSignal(String personId, LocalTime occurrence)
	throws Exception;

	/**
	 * request from the given intersection the given priority for the vehicle
	 * of the given identifier going into the given direction.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code intersection != null}
	 * pre	{@code vehicleId != null && !vehicleId.isEmpty()}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param intersection	intersection from which the priority is requested.
	 * @param priority		requested priority.
	 * @param vehicleId		unique identifier of the vehicle.
	 * @param destination	final destination of the vehicle.
	 * @param occurrence	time of occurrence of the event.
	 * @throws Exception	<i>to do</i>.
	 */
	public void			requestPriority(
		IntersectionPosition intersection,
		TypeOfTrafficLightPriority priority,
		String vehicleId,
		AbsolutePosition destination,
		LocalTime occurrence
		) throws Exception;

	/**
	 * signal that the given vehicle has arrived at destination.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param vehicleId		vehicle identifier.
	 * @param occurrence	time of occurrence of the event.
	 * @throws Exception	<i>to do</i>.
	 */
	public void			atDestination(String vehicleId, LocalTime occurrence)
	throws Exception;

	/**
	 * signal that the given vehicle has arrived at its base.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param vehicleId		vehicle identifier.
	 * @param occurrence	time of occurrence of the event.
	 * @throws Exception	<i>to do</i>.
	 */
	public void			atStation(String vehicleId, LocalTime occurrence)
	throws Exception;

	/**
	 * notify the availability of medics at the transmitting SAMU center.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code occurrence != null}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param occurrence	time of occurrence of the event.
	 * @throws Exception	<i>to do</i>.
	 */
	public void			notifyMedicsAvailable(LocalTime occurrence)
	throws Exception;

	/**
	 * notify the non-availability of medics at the transmitting SAMU center.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code occurrence != null}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param occurrence	time of occurrence of the event.
	 * @throws Exception	<i>to do</i>.
	 */
	public void			notifyNoMedicAvailable(LocalTime occurrence)
	throws Exception;

	/**
	 * notify the availability of ambulances at the transmitting SAMU center.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code occurrence != null}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param occurrence	time of occurrence of the event.
	 * @throws Exception	<i>to do</i>.
	 */
	public void			notifyAmbulancesAvailable(LocalTime occurrence)
	throws Exception;

	/**
	 * notify the non-availability of ambulances at the transmitting SAMU
	 * center.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code occurrence != null}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param occurrence	time of occurrence of the event.
	 * @throws Exception	<i>to do</i>.
	 */
	public void			notifyNoAmbulanceAvailable(LocalTime occurrence)
	throws Exception;
}
// -----------------------------------------------------------------------------
