package fr.sorbonne_u.cps.smartcity.components;

import java.time.LocalTime;

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
import fr.sorbonne_u.cps.smartcity.BasicSimSmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.connections.TrafficLightActionInboundPort;
import fr.sorbonne_u.cps.smartcity.connections.TrafficLightNotificationConnector;
import fr.sorbonne_u.cps.smartcity.connections.TrafficLightNotificationOutboundPort;
import fr.sorbonne_u.cps.smartcity.grid.Direction;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;
import fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightActionCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightActionImplI;
import fr.sorbonne_u.cps.smartcity.interfaces.TrafficLightNotificationCI;
import fr.sorbonne_u.cps.smartcity.interfaces.TypeOfTrafficLightPriority;

// -----------------------------------------------------------------------------
/**
 * The class <code>TrafficLightProxy</code>
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
@RequiredInterfaces(required={TrafficLightNotificationCI.class})
@OfferedInterfaces(offered={TrafficLightActionCI.class})
public class			TrafficLightProxy
extends		AbstractComponent
implements	TrafficLightActionImplI
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/** position of the traffic light.										*/
	protected IntersectionPosition					position;
	/** URI of the notification inbound port.								*/
	protected String								notificationInboundPortURI;
	/** notification outbound port.											*/
	protected TrafficLightNotificationOutboundPort	notificationOBP;
	/** actions inbound port.												*/
	protected TrafficLightActionInboundPort			actionIBP;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create the traffic light proxy component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code position != null && BasicSimSmartCityDescriptor.isInCity(position)}
	 * pre	{@code actionInboundPortURI != null && !actionInboundPortURI.isEmpty()}
	 * pre	{@code notificationInboundPortURI != null && !notificationInboundPortURI.isEmpty()}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param position						position of the traffic light.
	 * @param actionInboundPortURI			URI of the action inbound port.
	 * @param notificationInboundPortURI	URI of the notification inbound port.
	 * @throws Exception					<i>to do</i>.
	 */
	protected			TrafficLightProxy(
		IntersectionPosition position,
		String actionInboundPortURI,
		String notificationInboundPortURI
		) throws Exception
	{
		super(2, 0);

		assert	position != null &&
								BasicSimSmartCityDescriptor.isInCity(position);
		assert	actionInboundPortURI != null && !actionInboundPortURI.isEmpty();
		assert	notificationInboundPortURI != null &&
										!notificationInboundPortURI.isEmpty();

		this.position = position;
		this.notificationInboundPortURI = notificationInboundPortURI;
		this.notificationOBP = new TrafficLightNotificationOutboundPort(this);
		this.notificationOBP.publishPort();
		this.actionIBP =
				new TrafficLightActionInboundPort(actionInboundPortURI, this);
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
					TrafficLightNotificationConnector.class.getCanonicalName());
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
		this.notificationOBP.vehiclePassage("ambulance 0", Direction.S,
											LocalTime.of(12, 0));
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
			this.actionIBP.unpublishPort();
			this.notificationOBP.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Component services implementation
	// -------------------------------------------------------------------------

	@Override
	public void			changePriority(TypeOfTrafficLightPriority priority)
	throws Exception
	{
		System.out.println("Traffic light at " + this.position +
						   " changes priority to " + priority);
	}

	@Override
	public void			returnToNormalMode() throws Exception
	{
		System.out.println("Traffic light at " + this.position +
						   " returns to normal mode.");
	}
}
// -----------------------------------------------------------------------------
