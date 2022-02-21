package fr.sorbonne_u.cps.asyncalc.interfaces;

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

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

// -----------------------------------------------------------------------------
/**
 * The component interface <code>AsynchronousCalculatorServicesCI</code>
 * defines the signatures of methods that compute some result but rather
 * than returning it as usual, they send it back using a specific result
 * transmission interface and a port URI passed as last parameter.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2020-05-05</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public interface		AsynchronousCalculatorServicesCI
extends		OfferedCI,
			RequiredCI
{
	/**
	 * add the two first parameter and return the result by calling a port
	 * offering the interface <code>ResultReceptionCI</code> which URI
	 * is given as the last parameter; the serial number allows the caller
	 * to identify its call when receiving the result.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	resultReceptionInboundPortURI != null
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param x				first operand.
	 * @param y				first operand.
	 * @param serialNo		serial number of the call.
	 * @param resultReceptionInboundPortURI	URI of the port offering the interface <code>ResultReceptionCI</code> to return the result.
	 * @throws Exception	<i>to do</i>.
	 */
	public void			addition(
		double x,
		double y,
		long serialNo,
		String resultReceptionInboundPortURI
		) throws Exception;

	/**
	 * subtract the second parameter from the first and return the result by
	 * calling a port offering the interface <code>ResultReceptionCI</code>
	 * which URI is given as the last parameter; the serial number allows the
	 * caller to identify its call when receiving the result.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	resultReceptionInboundPortURI != null
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param x				first operand.
	 * @param y				first operand.
	 * @param serialNo		serial number of the call.
	 * @param resultReceptionInboundPortURI	URI of the port offering the interface <code>ResultReceptionCI</code> to return the result.
	 * @throws Exception	<i>to do</i>.
	 */
	public void			subtraction(
			double x,
			double y,
			long serialNo,
			String resultReceptionInboundPortURI
			) throws Exception;
}
// -----------------------------------------------------------------------------
