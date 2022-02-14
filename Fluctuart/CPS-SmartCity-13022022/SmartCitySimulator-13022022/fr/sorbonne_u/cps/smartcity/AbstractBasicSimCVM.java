package fr.sorbonne_u.cps.smartcity;

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
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.cps.smartcity.components.FireStationProxy;
import fr.sorbonne_u.cps.smartcity.components.SAMUStationProxy;
import fr.sorbonne_u.cps.smartcity.components.TrafficLightProxy;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// -----------------------------------------------------------------------------
/**
 * The class <code>AbstractBasicSimCVM</code> deploys the components that are
 * implemented in the basic simulator of the smart city.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * This class deploys the components used in the basic simulator of the smart
 * city, and mainly the proxy components that will represent the assets in the
 * smart city (fire stations, SAMU stations, controllable traffic lights, etc.
 * </p>
 * <p>
 * As proxy components in the simulator needs to be connected with events
 * emitter and action executors components in the CEP system, a simple protocol
 * is used to share the inbound ports URI among them. Proxy components use
 * the URI defined in the smart city descriptor to create their inbound port,
 * hence action executors can retrieve these URI from the smart city descriptor.
 * In order for the proxy component to get the URI of the notification inbound
 * ports that are created in the events emitter components, the protocol put
 * in place here is to require that the {@code deploy} method in the subclass
 * registers the association asset identifier to the URI of the inbound port of
 * the corresponding events emitter component using the method {@code register}
 * and then call the {@code deploy} method in this class (using {@code super}
 * which will retrieve the URI and used them to create the proxy components.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>Created on : 2022-02-10</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public abstract class	AbstractBasicSimCVM
extends		AbstractCVM
{
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/** map that will contain the URI of the action inbound ports used
	 *  in proxy components to offer their services in the smart city
	 *  and the URI of notification inbound ports used by events emitter
	 *  components to receive the notifications from the smart city.	*/
	private	Map<String,String>	facadeNotificationInboundPortsURI;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public				AbstractBasicSimCVM() throws Exception
	{
		// initialise the basic simulator smart city descriptor.
		BasicSimSmartCityDescriptor.initialise();
		// create a map that will contain the URI of the notification inbound
		// ports used in event emitter components to receive the notifications
		// from the smart city.
		this.facadeNotificationInboundPortsURI = new HashMap<>();
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * return true if the asset has already a URI registered, false otherwise.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code assetId != null && !assetId.isEmpty()}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param assetId	asset identifier as define the the smart city descriptor.
	 * @return			true if the asset has already a URI registered, false otherwise.
	 */
	protected boolean	registered(String assetId)
	{
		assert	assetId != null && !assetId.isEmpty();
		return this.facadeNotificationInboundPortsURI.containsKey(assetId);
	}

	/**
	 * register the URI if the notification inbound port used in the events
	 * emitter component associated with the asset identifier {@code assetId}.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code assetId != null && !assetId.isEmpty()}
	 * pre	{@code !registered(assetId)}
	 * pre	{@code uri != null && !uri.isEmpty()}
	 * post	{@code registered(assetId)}
	 * </pre>
	 *
	 * @param assetId	asset identifier as define the the smart city descriptor.
	 * @param uri		URI of the notification inbound port of the corresponding events emitter component.
	 */
	protected void		register(String assetId, String uri)
	{
		assert	assetId != null && !assetId.isEmpty();
		assert	!this.registered(assetId);
		assert	uri != null && !uri.isEmpty();
		this.facadeNotificationInboundPortsURI.put(assetId, uri);
	}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#deploy()
	 */
	@Override
	public void			deploy() throws Exception
	{
		Iterator<String> fireStationsIterator =
					BasicSimSmartCityDescriptor.createFireStationIdIterator();
		while (fireStationsIterator.hasNext()) {
			String stationId = fireStationsIterator.next();
			AbstractComponent.createComponent(
					FireStationProxy.class.getCanonicalName(),
					new Object[]{
							BasicSimSmartCityDescriptor.
										getActionInboundPortURI(stationId),
							this.facadeNotificationInboundPortsURI.
										get(stationId)
							});
		}		

		Iterator<String> samuStationsIditerator =
				BasicSimSmartCityDescriptor.createSAMUStationIdIterator();
		while (samuStationsIditerator.hasNext()) {
			String stationId = samuStationsIditerator.next();
			AbstractComponent.createComponent(
					SAMUStationProxy.class.getCanonicalName(),
					new Object[]{
							BasicSimSmartCityDescriptor.
										getActionInboundPortURI(stationId),
							this.facadeNotificationInboundPortsURI.
										get(stationId)
							});
		}

		Iterator<IntersectionPosition> trafficLightsIterator =
				BasicSimSmartCityDescriptor.createTrafficLightIdIterator();
		while (trafficLightsIterator.hasNext()) {
			IntersectionPosition p = trafficLightsIterator.next();
			AbstractComponent.createComponent(
					TrafficLightProxy.class.getCanonicalName(),
					new Object[]{
							p,
							BasicSimSmartCityDescriptor.
												getActionInboundPortURI(p),
							this.facadeNotificationInboundPortsURI.
								 				get(p.toString())
							});
		}
		super.deploy();
	}
}
// -----------------------------------------------------------------------------
