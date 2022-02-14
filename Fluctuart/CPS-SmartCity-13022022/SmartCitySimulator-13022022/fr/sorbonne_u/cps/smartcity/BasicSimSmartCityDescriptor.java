package fr.sorbonne_u.cps.smartcity;

import java.util.HashMap;

import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.cps.smartcity.components.FireStationProxy;
import fr.sorbonne_u.cps.smartcity.components.SAMUStationProxy;

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

import fr.sorbonne_u.cps.smartcity.descriptions.SmartCityDescriptor;
import fr.sorbonne_u.cps.smartcity.grid.AbsolutePosition;
import fr.sorbonne_u.cps.smartcity.grid.IntersectionPosition;

// -----------------------------------------------------------------------------
/**
 * The class <code>BasicSimSmartCityDescriptor</code> defines the information
 * about the smart city assets in the basic simulator.
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
public abstract class	BasicSimSmartCityDescriptor
extends			SmartCityDescriptor
{
	/** the identifier of the zone covering the entire city.				*/
	public static final String	UNIQUE_ZONE_ID = "zone0";

	/**
	 * initialise the smart city descriptor.
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
		SmartCityDescriptor.initialise();
		southeastCorner = new AbsolutePosition(2.0, 2.5);
		cityZone = new Zone(northwestCorner, southeastCorner);

		fireStations = new HashMap<>();
		fireStations.put(
				FireStationProxy.ASSET_ID,
				new FireStation(
						FireStationProxy.ASSET_ID,
						FireStationProxy.POSITION,
						cityZone,
						AbstractPort.generatePortURI()));

		samuStations = new HashMap<>();
		samuStations.put(
				SAMUStationProxy.ASSET_ID,
				new SAMUStation(
						SAMUStationProxy.ASSET_ID,
						SAMUStationProxy.POSITION,
						cityZone,
						AbstractPort.generatePortURI()));

		trafficLights = new HashMap<>();
		IntersectionPosition p = new IntersectionPosition(1.0, 1.0);
		trafficLights.put(
				p,
				new TrafficLight(p.toString(), p,
								 AbstractPort.generatePortURI()));
		p = new IntersectionPosition(1.0, 2.0);
		trafficLights.put(
				p,
				new TrafficLight(p.toString(), p,
								 AbstractPort.generatePortURI()));
		p = new IntersectionPosition(2.0, 1.0);
		trafficLights.put(
				p,
				new TrafficLight(p.toString(), p,
								 AbstractPort.generatePortURI()));
		p = new IntersectionPosition(2.0, 2.0);
		trafficLights.put(
				p,
				new TrafficLight(p.toString(), p,
								 AbstractPort.generatePortURI()));
	}
}
// -----------------------------------------------------------------------------
