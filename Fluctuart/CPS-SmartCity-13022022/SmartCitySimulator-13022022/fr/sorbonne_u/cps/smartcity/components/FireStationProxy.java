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
import fr.sorbonne_u.cps.smartcity.connections.FireStationNotificationConnector;
import fr.sorbonne_u.cps.smartcity.connections.FireStationNotificationOutboundPort;
import fr.sorbonne_u.cps.smartcity.connections.FireStationActionInboundPort;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.FireStationNotificationCI;
import fr.sorbonne_u.cps.smartcity.interfaces.FireStationActionCI;
import fr.sorbonne_u.cps.smartcity.interfaces.FireStationActionImplI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFire;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;

import java.time.LocalTime;

// -----------------------------------------------------------------------------
/**
 * The class <code>FireStationProxy</code> implements a fire station for the
 * basic simulator, which is meant to simply test all of the possible
 * notifications sent by a fire station and all of the possible actions that
 * a fire station can execute, without doing anything <i>per se</i> but rather
 * act as a test plug-in.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>Created on : 2022-02-03</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
@RequiredInterfaces(required={FireStationNotificationCI.class})
@OfferedInterfaces(offered={FireStationActionCI.class})
public class			FireStationProxy
extends		AbstractComponent
implements	FireStationActionImplI
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/** identifier of the corresponding fire station.						*/
	public static final String				ASSET_ID = "Fire Station 0";
	/** position of the corresponding fire station.							*/
	public static final AbsolutePosition	POSITION =
												new AbsolutePosition(0.5, 1.0);
	/** URI of the notification inbound port.								*/
	protected String								notificationInboundPortURI;
	/** notification outbound port.											*/
	protected FireStationNotificationOutboundPort	notificationOBP;
	/** actions inbound port.												*/
	protected FireStationActionInboundPort			actionIBP;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create the fire station proxy component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code actionInboundPortURI != null && !actionInboundPortURI.isEmpty()}
	 * pre	{@code notificationInboundPortURI != null && !notificationInboundPortURI.isEmpty()}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param actionInboundPortURI			URI of the action inbound port.
	 * @param notificationInboundPortURI	URI of the notification inbound port of the component in charge of receiving the notifications from this proxy.
	 * @throws Exception 					<i>to do</i>.
	 */
	protected			FireStationProxy(
		String actionInboundPortURI,
		String notificationInboundPortURI
		) throws Exception
	{
		super(2, 0);

		assert	actionInboundPortURI != null && !actionInboundPortURI.isEmpty();
		assert	notificationInboundPortURI != null &&
										!notificationInboundPortURI.isEmpty();

		
		this.notificationInboundPortURI = notificationInboundPortURI;
		this.notificationOBP = new FireStationNotificationOutboundPort(this);
		this.notificationOBP.publishPort();
		this.actionIBP =
			new FireStationActionInboundPort(actionInboundPortURI, this);
		this.actionIBP.publishPort();
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
					this.notificationOBP.getPortURI(),
					this.notificationInboundPortURI,
					FireStationNotificationConnector.class.getCanonicalName());
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 */
	@Override
	public synchronized void	execute() throws Exception
	{
		LocalTime t = LocalTime.of(12, 0);
		AbsolutePosition p = new AbsolutePosition(1.5, 2.0);
		this.notificationOBP.notifyStandardTrucksAvailable(t);
		this.notificationOBP.notifyHighLadderTrucksAvailable(t.plusMinutes(5));
		this.notificationOBP.fireAlarm(p, t.plusMinutes(10), TypeOfFire.Building);
		this.notificationOBP.requestPriority(
				new IntersectionPosition(2.0,  2.0),
				TypeOfTrafficLightPriority.EMERGENCY,
				"high ladder 0",
				new AbsolutePosition(0.5, 2.0),
				t.plusMinutes(15));
		this.notificationOBP.atDestination("high ladder 0", t.plusMinutes(15));
		this.notificationOBP.atStation("high ladder 0", t.plusMinutes(45));
		this.notificationOBP.notifyNoHighLadderTruckAvailable(t.plusMinutes(16));
		this.notificationOBP.endOfFire(p, t.plusMinutes(40));
		this.notificationOBP.notifyHighLadderTrucksAvailable(t.plusMinutes(41));
		this.notificationOBP.notifyNoStandardTruckAvailable(t.plusHours(1));
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#finalise()
	 */
	@Override
	public synchronized void	finalise() throws Exception
	{
		this.doPortDisconnection(this.notificationOBP.getPortURI());
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.notificationOBP.unpublishPort();
			this.actionIBP.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationActionImplI#triggerFirstAlarm(fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition, fr.sorbonne_u.cps.smartcity.interfaces.TypeOfFirefightingResource)
	 */
	@Override
	public void			triggerFirstAlarm(AbsolutePosition p, TypeOfFirefightingResource r)
	throws Exception
	{
		System.out.println("First alarm triggered at " + p +
						   " using resource type " + r);
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationActionImplI#triggerSecondAlarm(fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition)
	 */
	@Override
	public void			triggerSecondAlarm(AbsolutePosition p) throws Exception
	{
		System.out.println("Second alarm triggered at " + p);
	}

	/**
	 * @see fr.sorbonne_u.cps.smartcity.interfaces.FireStationActionImplI#triggerGeneralAlarm(fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition)
	 */
	@Override
	public void			triggerGeneralAlarm(AbsolutePosition p)
	throws Exception
	{
		System.out.println("General alarm triggered at " + p);
	}
}
// -----------------------------------------------------------------------------
