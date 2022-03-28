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
import fr.sorbonne_u.cps.smartcity.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.connections.TrafficLightActionConnector;
import fr.sorbonne_u.cps.smartcity.connections.TrafficLightActionOutboundPort;
import fr.sorbonne_u.cps.smartcity.connections.TrafficLightNotificationInboundPort;
import fr.sorbonne_u.cps.smartcity.grid.Direction;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightActionCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightNotificationCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightNotificationImplI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;
import fr.sorbonne_u.cps.smartcity.utils.TimeManager;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

// -----------------------------------------------------------------------------
/**
 * The class <code>TrafficLightFacade</code>
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2022-02-13</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
@OfferedInterfaces(offered={TrafficLightNotificationCI.class})
@RequiredInterfaces(required={TrafficLightActionCI.class})
public class			TrafficLightFacade
extends		AbstractComponent
implements	TrafficLightNotificationImplI
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/** set to true if actions must be tested.							 	*/
	protected static final boolean					TEST_ACTIONS = false;

	/** position of the traffic light.										*/
	protected IntersectionPosition					position;
	/** URI of the action inbound port.										*/
	protected String								actionInboundPortURI;
	/** notification inbound port.											*/
	protected TrafficLightNotificationInboundPort	notificationIBP;
	/** action outbound port.												*/
	protected TrafficLightActionOutboundPort		actionOBP;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	protected			TrafficLightFacade(
		IntersectionPosition position,
		String notificationInboundPortURI,
		String actionInboundPortURI
		) throws Exception
	{
		super(2, 1);

		assert	position != null &&
								SmartCityDescriptor.isInCity(position);
		assert	actionInboundPortURI != null && !actionInboundPortURI.isEmpty();
		assert	notificationInboundPortURI != null &&
									!notificationInboundPortURI.isEmpty();

		this.position = position;
		this.actionInboundPortURI = actionInboundPortURI;
		this.notificationIBP =
			new TrafficLightNotificationInboundPort(notificationInboundPortURI,
													this);
		this.notificationIBP.publishPort();
		this.actionOBP = new TrafficLightActionOutboundPort(this);
		this.actionOBP.publishPort();

		this.getTracer().setTitle("TrafficLightFacade " + this.position);
		this.getTracer().setRelativePosition(1, 2);
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
					TrafficLightActionConnector.class.getCanonicalName());
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
			if (this.position.equals(new IntersectionPosition(1.0, 1.0))) {
				LocalTime t1 =
					TimeManager.get().getSimulatedStartTime().plusSeconds(10);
				LocalTime t2 =
					TimeManager.get().getSimulatedStartTime().plusSeconds(15);
				LocalTime t3 =
					TimeManager.get().getSimulatedStartTime().plusSeconds(20);
				LocalTime t4 =
					TimeManager.get().getSimulatedStartTime().plusSeconds(25);
				LocalTime t5 =
					TimeManager.get().getSimulatedStartTime().plusSeconds(30);
				LocalTime t6 =
					TimeManager.get().getSimulatedStartTime().plusSeconds(40);

				long t1NanoDelay = TimeManager.get().localTime2nanoDelay(t1);
				long t2NanoDelay = TimeManager.get().localTime2nanoDelay(t2);
				long t3NanoDelay = TimeManager.get().localTime2nanoDelay(t3);
				long t4NanoDelay = TimeManager.get().localTime2nanoDelay(t4);
				long t5NanoDelay = TimeManager.get().localTime2nanoDelay(t5);
				long t6NanoDelay = TimeManager.get().localTime2nanoDelay(t6);

				this.scheduleTask(
						(o -> { try {
							this.traceMessage(
									"Change priority to NORTH_SOUTH at "
											+ t1 + "\n");
							this.actionOBP.changePriority(
									TypeOfTrafficLightPriority.NORTH_SOUTH);
						} catch (Throwable e) {
							e.printStackTrace();
						}
						}),
						t1NanoDelay, 
						TimeUnit.NANOSECONDS);
				this.scheduleTask(
						(o -> { try {
							this.traceMessage("Return to normal mode at " +
									t2 + "\n");
							this.actionOBP.returnToNormalMode();
						} catch (Throwable e) {
							e.printStackTrace();
						}
						}),
						t2NanoDelay, 
						TimeUnit.NANOSECONDS);
				this.scheduleTask(
						(o -> { try {
							this.traceMessage(
									"Change priority to EAST_WEST at "
											+ t3 + "\n");
							this.actionOBP.changePriority(
									TypeOfTrafficLightPriority.EAST_WEST);
						} catch (Throwable e) {
							e.printStackTrace();
						}
						}),
						t3NanoDelay, 
						TimeUnit.NANOSECONDS);
				this.scheduleTask(
						(o -> { try {
							this.traceMessage("Return to normal mode at " +
									t4 + "\n");
							this.actionOBP.returnToNormalMode();
						} catch (Throwable e) {
							e.printStackTrace();
						}
						}),
						t4NanoDelay, 
						TimeUnit.NANOSECONDS);
				this.scheduleTask(
						(o -> { try {
							this.traceMessage("Change priority to EMERGENCY at "
									+ t5 + "\n");
							this.actionOBP.changePriority(
									TypeOfTrafficLightPriority.EMERGENCY);
						} catch (Throwable e) {
							e.printStackTrace();
						}
						}),
						t5NanoDelay, 
						TimeUnit.NANOSECONDS);
				this.scheduleTask(
						(o -> { try {
							this.traceMessage("Return to normal mode at " +
									t6 + "\n");
							this.actionOBP.returnToNormalMode();
						} catch (Throwable e) {
							e.printStackTrace();
						}
						}),
						t6NanoDelay, 
						TimeUnit.NANOSECONDS);
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
			this.actionOBP.unpublishPort();
			this.notificationIBP.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightNotificationImplI#vehiclePassage(java.lang.String, fr.sorbonne_u.cps.smartcity.grid.Direction, java.time.LocalTime)
	 */
	@Override
	public void			vehiclePassage(
		String vehicleId,
		Direction d,
		LocalTime occurrence
		) throws Exception
	{
		this.traceMessage(
				"Traffic light at " + this.position +
				" receives the notification of the passage of " + vehicleId +
				(d != null ? " in the direction of " + d : "") +
				" at " + occurrence + "\n");
	}
}
// -----------------------------------------------------------------------------
