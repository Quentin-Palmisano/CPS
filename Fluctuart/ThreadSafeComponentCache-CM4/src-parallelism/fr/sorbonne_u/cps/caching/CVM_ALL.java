package fr.sorbonne_u.cps.caching;

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
import fr.sorbonne_u.cps.caching.components.CachedComputerMoreParallelism;
import fr.sorbonne_u.cps.caching.components.MultiClient;
import fr.sorbonne_u.cps.caching.utils.Computable;
import fr.sorbonne_u.cps.caching.utils.Factorisor;

// -----------------------------------------------------------------------------
/**
 * The class <code>CVM</code> deploys and execute and example of the cached
 * computation component using here the method that allows the computer
 * component to start several computations in parallel.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2020-03-11</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			CVM_ALL
extends		CVM
{
	public				CVM_ALL() throws Exception
	{
	}

	/**
	 * @see fr.sorbonne_u.cps.caching.CVM#deploy()
	 */
	@Override
	public void			deploy() throws Exception
	{
		// values that will be computed by the client.
		int[] values = new int[]{2*3,
								 2*3*5,
								 2*3*5*7,
								 2*3*5*7*11,
								 2*3*5*7*11*13,
								 2*3*5*7*11*13*17,
								 2*3*5*7*11*13*17*19,
								 2*3*5*7*11*13*17*19*23,
								 2*3*5*7*11*13*17*19*23,
								 2*3*5*7*11*13*17*19*23
								} ;

		AbstractComponent.createComponent(
				MultiClient.class.getCanonicalName(),
				new Object[]{10, COMPUTER_INBOUND_PORT_URI, values}) ;

		super.deploy();
	}

	/**
	 * create and deploy the cache component with the <code>computeAll</code>
	 * method.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true				// no more preconditions.
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.cps.caching.CVM#deployCachedComputer()
	 */
	@Override
	protected void		deployCachedComputer() throws Exception
	{
		Computable<Integer,Integer[]> computation =
									(i -> { assert	i >= 2 ;
											Thread.sleep(1000L) ;
											return Factorisor.factor(i) ; }) ;
		AbstractComponent.createComponent(
						CachedComputerMoreParallelism.class.getCanonicalName(),
						new Object[]{5,
									 computation,
									 COMPUTER_INBOUND_PORT_URI}) ;
	}

	/**
	 * execute the BCM application.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true			// no precondition.
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param args	command-line arguments.
	 */
	public static void	main(String[] args)
	{
		try {
			CVM_ALL c = new CVM_ALL() ;
			c.startStandardLifeCycle(15000L) ;
			Thread.sleep(20000L) ;
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
}
// -----------------------------------------------------------------------------
