package fr.sorbonne_u.cps.smartcity.descriptions;

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

import java.util.Map;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;

import java.util.Iterator;

// -----------------------------------------------------------------------------
/**
 * The class <code>SmartCityDescriptor</code> defines all of the necessary
 * information for the CEP system to interact with the smart city simulators.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p>
 * To ease the access to the information from everywhere in the program, all
 * of the variables and methods in this class are declared as static members.
 * Hence, there is no need to instantiate this class and pass an object
 * reference everywhere.
 * </p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 * 
 * <p>Created on : 2022-01-11</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public abstract class	SmartCityDescriptor
{
	// -------------------------------------------------------------------------
	// Inner types and classes
	// -------------------------------------------------------------------------

	/**
	 * The class <code>Zone</code> defines a zone in the city.
	 *
	 * <p><strong>Description</strong></p>
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
	protected static class	Zone
	{
		protected final AbsolutePosition	northwestCorner;
		protected final AbsolutePosition	southeastCorner;

		public			Zone(
			AbsolutePosition northwestCorner,
			AbsolutePosition southeastCorner
			)
		{
			super();
			this.northwestCorner = northwestCorner;
			this.southeastCorner = southeastCorner;
		}

		public boolean	inZone(AbsolutePosition p)
		{
			return	this.northwestCorner.getX() <= p.getX() &&
					this.northwestCorner.getY() <= p.getY() &&
					this.southeastCorner.getX() >= p.getX() &&
					this.southeastCorner.getY() >= p.getY();
		}
	}

	/**
	 * The abstract class <code>AbstractAsset</code> defines the common
	 * data and methods for assets in the smart city and its simulator.
	 *
	 * <p><strong>Description</strong></p>
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
	protected static abstract class	AbstractAsset
	{
		/** URI of the station.												*/
		protected final String				id;
		/** position of the station in the city.							*/
		protected final AbsolutePosition	position;
		/** zone covered by the station in the city.						*/
		protected final Zone				zone;
		/** URI of the inbound port offering the actions of the asset.		*/
		protected final String				actionsInboundPortURI;

		/**
		 * create an asset descriptor.
		 * 
		 * <p><strong>Contract</strong></p>
		 * 
		 * <pre>
		 * pre	{@code id != null && !id.isEmpty()}
		 * pre	{@code position != null}
		 * pre	{@code zone == null || zone.inZone(position)}
		 * pre	{@code }
		 * post	true		// no postcondition.
		 * </pre>
		 *
		 * @param id					URI of the asset.
		 * @param position				position of the asset in the city.
		 * @param zone					zone covered by the asset in the city.
		 * @param actionsInboundPortURI	URI of the inbound port offering the services of the asset.
		 */
		public				AbstractAsset(
			String id,
			AbsolutePosition position,
			Zone zone,
			String actionsInboundPortURI
			)
		{
			super();

			assert	id != null && !id.isEmpty();
			assert	position != null;
			assert	zone == null || zone.inZone(position);
			assert	actionsInboundPortURI != null &&
											!actionsInboundPortURI.isEmpty();

			this.id = id;
			this.position = position;
			this.zone = zone;
			this.actionsInboundPortURI = actionsInboundPortURI;
		}

		/**
		 * return the position of the asset.
		 * 
		 * <p><strong>Contract</strong></p>
		 * 
		 * <pre>
		 * pre	true		// no precondition.
		 * post	{@code return != null}
		 * </pre>
		 *
		 * @return	the position of the station.
		 */
		public AbsolutePosition	getPosition()
		{
			return this.position;
		}

		/**
		 * return the URI of the inbound port offering the actions of the
		 * asset.
		 * 
		 * <p><strong>Contract</strong></p>
		 * 
		 * <pre>
		 * pre	true		// no precondition.
		 * post	{@code return != null && !return.isEmpty()}
		 * </pre>
		 *
		 * @return	the URI of the inbound port offering the actions of the asset.
		 */
		public String		getActionsInboundPortURI()
		{
			return this.actionsInboundPortURI;
		}

		/**
		 * return true if {@code position} is covered by the asset.
		 * 
		 * <p><strong>Contract</strong></p>
		 * 
		 * <pre>
		 * pre	{@code position != null}
		 * post	true		// no postcondition.
		 * </pre>
		 *
		 * @param position	a position in the city.
		 * @return			true if {@code position} is covered by the asset.
		 */
		public boolean	dependsUpon(AbsolutePosition position)
		{
			assert	position != null;
			return this.zone.inZone(position);
		}
	}

	/**
	 * The class <code>SAMUStation</code> describes a SAMU station in the city
	 * and its simulation.
	 *
	 * <p><strong>Description</strong></p>
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
	protected static class	SAMUStation
	extends		AbstractAsset	
	{
		/**
		 * create a SAMU station descriptor.
		 * 
		 * <p><strong>Contract</strong></p>
		 * 
		 * <pre>
		 * pre	{@code id != null && !id.isEmpty()}
		 * pre	{@code position != null}
		 * pre	{@code zone != null && zone.inZone(position)}
		 * pre	{@code }
		 * post	true			// no postcondition.
		 * </pre>
		 *
		 * @param id					URI of the station.
		 * @param position				position of the station in the city.
		 * @param zone					zone covered by the station in the city.
		 * @param actionsInboundPortURI	URI of the inbound port offering the actions of the station.
		 */
		public			SAMUStation(
			String id,
			AbsolutePosition position,
			Zone zone,
			String actionsInboundPortURI
			)
		{
			super(id, position, zone, actionsInboundPortURI);
		}
	}

	/**
	 * The class <code>FireStation</code> describes a fire station in the city
	 * and its simulation.
	 *
	 * <p><strong>Description</strong></p>
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
	protected static class	FireStation
	extends		AbstractAsset
	{
		/**
		 * create a fire station descriptor.
		 * 
		 * <p><strong>Contract</strong></p>
		 * 
		 * <pre>
		 * pre	{@code id != null && !id.isEmpty()}
		 * pre	{@code position != null}
		 * pre	{@code zone != null && zone.inZone(position)}
		 * pre	{@code }
		 * post	true			// no postcondition.
		 * </pre>
		 *
		 * @param id					URI of the station.
		 * @param position				position of the station in the city.
		 * @param zone					zone covered by the station in the city.
		 * @param actionsInboundPortURI	URI of the inbound port offering the actions of the station.
		 */
		public			FireStation(
			String id,
			AbsolutePosition position,
			Zone zone,
			String actionsInboundPortURI
			)
		{
			super(id, position, zone, actionsInboundPortURI);
		}
	}

	protected static class	TrafficLight
	extends		AbstractAsset
	{
		public			TrafficLight(
			String id,
			IntersectionPosition position,
			String actionsInboundPortURI
			)
		{
			super(id, position, null, actionsInboundPortURI);
		}
		
	}
	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/** position of the northwest corner of the city.						*/
	protected static AbsolutePosition			northwestCorner;
	/** position of the southeast corner of the city.						*/
	protected static AbsolutePosition			southeastCorner;
	/** a zone covering the entier city.									*/
	protected static Zone						cityZone;

	/** map from fire stations id to the information on this station.		*/
	protected static Map<String,FireStation>				fireStations;
	/** map from SAMU stations id to the information on this station.		*/
	protected static Map<String,SAMUStation>				samuStations;
	/** map from intersection position to the information on its traffic
	 *  light.																*/
	protected static Map<IntersectionPosition,TrafficLight>	trafficLights;

	/**
	 * initialise the class variables.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 */
	public static void	initialise()
	{
		northwestCorner = new AbsolutePosition(0.0, 0.5);
	}

	// -------------------------------------------------------------------------
	// Methods
	// -------------------------------------------------------------------------

	/**
	 * return true if {@code position} is in the city.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code position != null}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param position	a position.
	 * @return			true if {@code position} is in the city.
	 */
	public static boolean	isInCity(AbsolutePosition position)
	{
		assert	position != null;
		return cityZone.inZone(position);
	}

	/**
	 * return true if {@code assetId} is a valid asset identifier.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code assetId != null && !assetId.isEmpty()}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param assetId	identifier to be tested.
	 * @return			true if {@code assetId} is a valid asset identifier.
	 */
	public static boolean	isValidAssetId(String assetId)
	{
		assert	assetId != null && !assetId.isEmpty();

		return isValidFireStationId(assetId) ||
			   isValidSAMUStationId(assetId);
	}

	/**
	 * return true if {@code assetId} is a valid fire station identifier.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code assetId != null && !assetId.isEmpty()}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param assetId	identifier to be tested.
	 * @return			true if {@code assetId} is a valid fire station identifier.
	 */
	public static boolean	isValidFireStationId(String assetId)
	{
		assert	assetId != null && !assetId.isEmpty();

		return fireStations.containsKey(assetId);
	}

	/**
	 * return true if {@code assetId} is a valid SAMU station identifier.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code assetId != null && !assetId.isEmpty()}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param assetId	identifier to be tested.
	 * @return			true if {@code assetId} is a valid SAMU station identifier.
	 */
	public static boolean	isValidSAMUStationId(String assetId)
	{
		assert	assetId != null && !assetId.isEmpty();

		return samuStations.containsKey(assetId);
	}

	/**
	 * return true if {@code position} is a valid traffic light position.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code position != null}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param position	intersection position to be tested.
	 * @return			true if {@code assetId} is a valid traffic light identifier.
	 */
	public static boolean	isValidTrafficLight(IntersectionPosition position)
	{
		assert	position != null;

		return trafficLights.containsKey(position);
	}

	/**
	 * return true if {@code position} is in the zone covered by
	 * {@code assetId}.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code position != null && isInCity(position)}
	 * pre	{@code isValidAssetId(assetId)}
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @param position	position in the city.
	 * @param assetId	station identifier (SAMU or Fire station).
	 * @return			true if {@code position} is in the zone covered by {@code assetId}.
	 */
	public static boolean	dependsUpon(
		AbsolutePosition position,
		String assetId
		)
	{
		assert	position != null && isInCity(position);
		assert	isValidAssetId(assetId);

		FireStation fs = fireStations.get(assetId);
		if (fs != null) {
			return fs.dependsUpon(position);
		}
		SAMUStation sc = samuStations.get(assetId);
		if (sc != null) {
			return sc.dependsUpon(position);
		}
		return true;
	}

	/**
	 * return the position of the given asset.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code isValidAssetId(assetId)}
	 * post	{@code return != null && isInCity(return)}
	 * </pre>
	 *
	 * @param assetId	asset identifier.
	 * @return			the position of the station.
	 */
	public static AbsolutePosition	getPosition(String assetId)
	{
		assert	isValidAssetId(assetId);

		FireStation fs = fireStations.get(assetId);
		if (fs != null) {
			return fs.getPosition();
		}
		SAMUStation sc = samuStations.get(assetId);
		if (sc != null) {
			return sc.getPosition();
		}
		return null;
	}

	/**
	 * return the URI of the actions inbound port of the component representing
	 * the given asset.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code isValidAssetId(assetId)}
	 * post	{@code return != null}
	 * </pre>
	 *
	 * @param assetId	asset identifier.
	 * @return			the URI of the actions inbound port of the component representing this asset.
	 */
	public static String	getActionInboundPortURI(String assetId)
	{
		assert	isValidAssetId(assetId);

		FireStation fs = fireStations.get(assetId);
		if (fs != null) {
			return fs.getActionsInboundPortURI();
		}
		SAMUStation sc = samuStations.get(assetId);
		if (sc != null) {
			return sc.getActionsInboundPortURI();
		}
		return null;
	}

	/**
	 * return the URI of the actions inbound port of the component representing
	 * the given traffic light.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code isValidTrafficLight(position)}
	 * post	{@code return != null}
	 * </pre>
	 *
	 * @param position	intersection position.
	 * @return			the URI of the actions inbound port of the component representing this asset.
	 */
	public static String	getActionInboundPortURI(
		IntersectionPosition position
		)
	{
		assert	isValidTrafficLight(position);

		return trafficLights.get(position).getActionsInboundPortURI();
	}
	
	/**
	 * return an iterator over valid fire station identifiers.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @return			an iterator over valid station identifiers.
	 */
	public static Iterator<String>	createFireStationIdIterator()
	{
		return fireStations.keySet().iterator();
	}
	
	/**
	 * return an iterator over valid SAMU station identifiers.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @return	an iterator over valid SAMU station identifiers.
	 */
	public static Iterator<String>	createSAMUStationIdIterator()
	{
		return samuStations.keySet().iterator();
	}
	
	/**
	 * return an iterator over valid traffic light positions.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true		// no precondition.
	 * post	true		// no postcondition.
	 * </pre>
	 *
	 * @return	an iterator over valid traffic light identifiers.
	 */
	public static Iterator<IntersectionPosition> createTrafficLightIdIterator()
	{
		return trafficLights.keySet().iterator();
	}

	/**
	 * return the distance between the two given stations.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code (isValidFireStationId(stationId1) && isValidFireStationId(stationId2)) || (isValidSAMUStationId(stationId1) && isValidSAMUStationId(stationId2))}
	 * post	{@code return >= 0.0}
	 * </pre>
	 *
	 * @param stationId1	identifier of a first station.
	 * @param stationId2	identifier of a second station.
	 * @return				the distance between the tow given fire stations.
	 */
	public static double	distance(String stationId1, String stationId2)
	{
		assert		(isValidFireStationId(stationId1) &&
										isValidFireStationId(stationId2))
				||	(isValidSAMUStationId(stationId1) &&
										isValidSAMUStationId(stationId2));

		AbsolutePosition p1, p2;
		if (isValidFireStationId(stationId1)) {
			p1 = fireStations.get(stationId1).getPosition();
			p2 = fireStations.get(stationId2).getPosition();
		} else {
			p1 = samuStations.get(stationId1).getPosition();
			p2 = samuStations.get(stationId2).getPosition();
		}
		return p1.distance(p2);
	}
}
// -----------------------------------------------------------------------------
