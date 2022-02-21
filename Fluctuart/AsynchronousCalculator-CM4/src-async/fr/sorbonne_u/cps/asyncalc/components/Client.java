package fr.sorbonne_u.cps.asyncalc.components;

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
import fr.sorbonne_u.cps.asyncalc.connectors.AsynchronousCalculatorServicesConnector;
import fr.sorbonne_u.cps.asyncalc.interfaces.AsynchronousCalculatorServicesCI;
import fr.sorbonne_u.cps.asyncalc.interfaces.ResultReceptionCI;
import fr.sorbonne_u.cps.asyncalc.ports.AsynchronousCalculatorServicesOutboundPort;
import fr.sorbonne_u.cps.asyncalc.ports.ResultReceptionInboundPort;
import java.util.HashMap;

// -----------------------------------------------------------------------------
/**
 * The class <code>Client</code> implements a component that calls the
 * asynchronous calculator component to show how it works.
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
@OfferedInterfaces(offered = {ResultReceptionCI.class})
@RequiredInterfaces(required = {AsynchronousCalculatorServicesCI.class})
public class			Client
extends		AbstractComponent
implements	ResultReceptorI
{
	protected long							serialNo = 0;
	protected ResultReceptionInboundPort	rrip;
	protected HashMap<Long, Object[]>		awaitedComputations;

	protected			Client() throws Exception
	{
		super(2, 0);
		this.initialise();
	}

	protected			Client(String reflectionInboundPortURI) throws Exception
	{
		super(reflectionInboundPortURI, 1, 0);
		this.initialise();
	}

	protected void	initialise() throws Exception
	{
		this.rrip = new ResultReceptionInboundPort(this);
		this.rrip.publishPort();

		this.getTracer().setTitle("Client");
		this.getTracer().setRelativePosition(1, 0);
		this.toggleTracing();

		this.awaitedComputations = new HashMap<Long, Object[]>();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 */
	@Override
	public void			execute() throws Exception
	{
		AsynchronousCalculatorServicesOutboundPort p =
						new AsynchronousCalculatorServicesOutboundPort(this);
		p.publishPort();
		this.doPortConnection(
			p.getPortURI(),
			AsynchronousCalculator.INBOUND_PORT_URI,
			AsynchronousCalculatorServicesConnector.class.getCanonicalName());

		long no = this.serialNo++;
		this.awaitedComputations.put(no, new Object[]{"addition", 10, 15});
		p.addition(10, 15, no, this.rrip.getPortURI());
		no = this.serialNo++;
		this.awaitedComputations.put(no, new Object[]{"subtraction", 100, 50});
		p.subtraction(100, 50, no, this.rrip.getPortURI());

		Thread.sleep(500L);
		this.doPortDisconnection(p.getPortURI());
		p.unpublishPort();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public void			shutdown() throws ComponentShutdownException
	{
		try {
			this.rrip.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	/**
	 * @see fr.sorbonne_u.cps.asyncalc.components.ResultReceptorI#acceptResult(long, java.lang.Object)
	 */
	@Override
	public void			acceptResult(long serialNo, Object result)
	throws Exception
	{
		assert	this.awaitedComputations.containsKey(serialNo);
		Object[] operation = this.awaitedComputations.remove(serialNo);
		this.traceMessage("result of the " + operation[0] +
						  " of the operands (" + operation[1] + ", " +
						  						 operation[2] +
						  ") is " + result + "\n");
	}
}
// -----------------------------------------------------------------------------
