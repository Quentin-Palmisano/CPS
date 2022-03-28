package fr.sorbonne_u.cps.smartcity.components;

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

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.smartcity.connections.SAMUNotificationInboundPort;
import fr.sorbonne_u.cps.smartcity.connections.SAMUActionConnector;
import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.connections.SAMUActionOutboundPort;
import fr.sorbonne_u.cps.smartcity.descriptions.AbstractSmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationCI;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI;
import fr.sorbonne_u.cps.smartcity.interfaces.SAMUActionCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfSAMURessources;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import fr.sorbonne_u.cps.smartcity.utils.TimeManager;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

// -----------------------------------------------------------------------------
/**
 * The class <code>SAMUStationFacade</code>
 *
 * <p><strong>Description</strong></p>
 * 
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
public class			SAMUStationFacade
extends		AbstractComponent
implements	SAMUNotificationImplI
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/** set to true if actions must be tested.							 	*/
	protected static final boolean			TEST_ACTIONS = true;

	/** identifier of the corresponding SAMU station.						*/
	protected String						stationId;
	/** URI of the action inbound port.										*/
	protected String						actionInboundPortURI;
	/** notification inbound port.											*/
	protected SAMUNotificationInboundPort	notificationIBP;
	/** action outbound port.												*/
	protected SAMUActionOutboundPort		actionOBP;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a SAMU station facade component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code BasicSimSmartCityDescriptor.isValidSAMUStationId(stationId)}
	 * pre	{@code notificationInboundPortURI != null && !notificationInboundPortURI.isEmpty()}
	 * pre	{@code actionInboundPortURI != null && !actionInboundPortURI.isEmpty()}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param stationId						identifier of the corresponding SAMU station.
	 * @param notificationInboundPortURI	URI of the notification inbound port to be used by this facade component.
	 * @param actionInboundPortURI			URI of the action inbound port of the proxy component.
	 * @throws Exception					<i>to do</i>.
	 */
	protected			SAMUStationFacade(
		String stationId,
		String notificationInboundPortURI,
		String actionInboundPortURI
		) throws Exception
	{
		super(2, 1);

		assert	SmartCityDescriptor.isValidSAMUStationId(stationId);
		assert	notificationInboundPortURI != null && !notificationInboundPortURI.isEmpty();
		assert	actionInboundPortURI != null && !actionInboundPortURI.isEmpty();

		this.stationId = stationId;
		this.actionInboundPortURI = actionInboundPortURI;
		this.notificationIBP =
			new SAMUNotificationInboundPort(notificationInboundPortURI, this);
		this.notificationIBP.publishPort();
		this.actionOBP = new SAMUActionOutboundPort(this);
		this.actionOBP.publishPort();

		this.getTracer().setTitle("SAMUStationFacade " + this.stationId);
		this.getTracer().setRelativePosition(1, 0);
		this.toggleTracing();
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 */
	@Override
	public synchronized void	start() throws ComponentStartException
	{
		super.start();

		try {
			this.doPortConnection(
					this.actionOBP.getPortURI(),
					this.actionInboundPortURI,
					SAMUActionConnector.class.getCanonicalName());
		} catch (Exception e) {
			throw new ComponentStartException(e) ;
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 */
	@Override
	public synchronized void	execute() throws Exception
	{
		if (TEST_ACTIONS) {
			if (this.stationId.equals("SAMU-1")) {
				LocalTime intervention1 =
					TimeManager.get().getSimulatedStartTime().plusSeconds(20);
				long intervention1delay =
					TimeManager.get().localTime2nanoDelay(intervention1);
				AbsolutePosition p1 = new AbsolutePosition(2.0, 0.5);
				this.scheduleTask(
					o -> {
						try {
							this.traceMessage(
								"Trigger ambulance intervention at "
								+ intervention1 + "\n");
							this.actionOBP.triggerIntervention(
								p1, null, TypeOfSAMURessources.AMBULANCE);
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}, intervention1delay, TimeUnit.NANOSECONDS);

//				LocalTime intervention2 =
//					TimeManager.get().getSimulatedStartTime().plusSeconds(40);
//				long intervention2delay =
//					TimeManager.get().localTime2nanoDelay(intervention2);
//				AbsolutePosition p2 = new AbsolutePosition(3.5, 1.0);
//				this.scheduleTask(
//					o -> {
//						try {
//							this.traceMessage(
//								"Trigger telemedic intervention at "
//								+ intervention2 + "\n");
//							this.actionOBP.triggerIntervention(
//								p2, "person-0", TypeOfSAMURessources.TELEMEDIC);
//							} catch (Throwable e) {
//								e.printStackTrace();
//							}
//						}, intervention2delay, TimeUnit.NANOSECONDS);
//
//				LocalTime intervention3 =
//					TimeManager.get().getSimulatedStartTime().plusSeconds(60);
//				long intervention3delay =
//					TimeManager.get().localTime2nanoDelay(intervention3);
//				AbsolutePosition p3 = new AbsolutePosition(3.5, 3.0);
//				this.scheduleTask(
//					o -> {
//						try {
//							this.traceMessage(
//								"Trigger medic intervention at " +
//								intervention3 + "\n");
//							this.actionOBP.triggerIntervention(
//								p3, "person-1", TypeOfSAMURessources.MEDIC);
//						} catch (Throwable e) {
//							e.printStackTrace();
//						}
//					}, intervention3delay, TimeUnit.NANOSECONDS);
			}
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#finalise()
	 */
	@Override
	public synchronized void	finalise() throws Exception
	{
		this.doPortDisconnection(this.actionOBP.getPortURI());
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.notificationIBP.unpublishPort();
			this.actionOBP.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI#healthAlarm(fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition, fr.sorbonne_u.cps.smartcity.interfaces.TypeOfHealthAlarm, java.time.LocalTime)
	 */
	@Override
	public void			healthAlarm(
		AbsolutePosition position,
		TypeOfHealthAlarm type,
		LocalTime occurrence
		) throws Exception
	{
		assert	position != null;
		assert	!type.isTracking();
		assert	occurrence != null;

		assert	AbstractSmartCityDescriptor.dependsUpon(position, this.stationId);

		this.traceMessage("Health notification of type " + type +
						  " at position " + position +
						  " received at " + occurrence + "\n");
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI#trackingAlarm(fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition, java.lang.String, java.time.LocalTime)
	 */
	@Override
	public void			trackingAlarm(
		AbsolutePosition position,
		String personId,
		LocalTime occurrence
		) throws Exception
	{
		assert	position != null;
		assert	personId != null && !personId.isEmpty();
		assert	occurrence != null;

		assert	AbstractSmartCityDescriptor.dependsUpon(position, this.stationId);

		this.traceMessage("Health notification of type tracking for " +
						  personId + " at position " + position +
						  " received at " + occurrence + "\n");
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI#manualSignal(java.lang.String, java.time.LocalTime)
	 */
	@Override
	public void			manualSignal(
		String personId,
		LocalTime occurrence
		) throws Exception
	{
		assert	personId != null && !personId.isEmpty();
		assert	occurrence != null;

		this.traceMessage("Manual signal emitted by " + personId +
						  " received at " + occurrence + "\n");
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI#requestPriority(fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition, fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority, java.lang.String, fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition, java.time.LocalTime)
	 */
	@Override
	public void			requestPriority(
		IntersectionPosition intersection,
		TypeOfTrafficLightPriority priority,
		String vehicleId,
		AbsolutePosition destination,
		LocalTime occurrence
		) throws Exception
	{
		this.traceMessage("priority " + priority + " requested for vehicle " +
						  vehicleId + " at intersection " + intersection +
						  " towards " + destination + " at " + occurrence + "\n");
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI#atDestination(java.lang.String, java.time.LocalTime)
	 */
	@Override
	public void			atDestination(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		this.traceMessage("Vehicle " + vehicleId +
						  " has arrived at destination at " + occurrence + "\n");
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI#atStation(java.lang.String, java.time.LocalTime)
	 */
	@Override
	public void			atStation(String vehicleId, LocalTime occurrence)
	throws Exception
	{
		this.traceMessage("Vehicle " + vehicleId + " has arrived at station at "
								+ occurrence + "\n");
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI#notifyMedicsAvailable(java.time.LocalTime)
	 */
	@Override
	public void			notifyMedicsAvailable(LocalTime occurrence)
	throws Exception
	{
		assert	occurrence != null;

		this.traceMessage(
				"Notification that medics are available received at " +
															occurrence + "\n");
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI#notifyNoMedicAvailable(java.time.LocalTime)
	 */
	@Override
	public void			notifyNoMedicAvailable(LocalTime occurrence)
	throws Exception
	{
		assert	occurrence != null;

		this.traceMessage(
				"Notification that no medic are available received at " +
															occurrence + "\n");
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI#notifyAmbulancesAvailable(java.time.LocalTime)
	 */
	@Override
	public void			notifyAmbulancesAvailable(LocalTime occurrence)
	throws Exception
	{
		assert	occurrence != null;

		this.traceMessage(
				"Notification that ambulances are available received at " +
															occurrence + "\n");
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.SAMUNotificationImplI#notifyNoAmbulanceAvailable(java.time.LocalTime)
	 */
	@Override
	public void			notifyNoAmbulanceAvailable(LocalTime occurrence)
	throws Exception
	{
		assert	occurrence != null;

		this.traceMessage(
				"Notification that no ambulance are available received at " +
															occurrence + "\n");
	}
}
// -----------------------------------------------------------------------------
