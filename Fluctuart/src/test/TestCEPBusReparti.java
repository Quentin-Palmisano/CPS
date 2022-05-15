package test;

import components.CEPBus;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;
import fr.sorbonne_u.components.examples.ddeployment_cs.components.DynamicAssembler;
import fr.sorbonne_u.components.helpers.CVMDebugModes;

public class TestCEPBusReparti extends AbstractDistributedCVM {
	
	protected static String		JVM_1_URI = "JVM_1" ;
	protected static String		JVM_2_URI = "JVM_2" ;

	public TestCEPBusReparti(String[] args) throws Exception {
		super(args);
	}
	
	@Override
	public void			initialise() throws Exception
	{
		super.initialise() ;

		String[] jvmURIs = this.configurationParameters.getJvmURIs() ;
		boolean JVM_1_ok = false ;
		boolean JVM_2_ok = false ;
		for (int i = 0 ; i < jvmURIs.length &&
										(!JVM_1_ok ||
										!JVM_2_ok)  ; i++) {
			if (jvmURIs[i].equals(JVM_1_URI)) {
				JVM_1_ok = true ;
			} else if (jvmURIs[i].equals(JVM_2_URI)) {
				JVM_2_ok = true ;
			}
		}
		assert	JVM_1_ok && JVM_1_ok;
	}
	
	@Override
	public void			instantiateAndPublish() throws Exception
	{
		// ---------------------------------------------------------------------
		// Configuration phase
		// ---------------------------------------------------------------------

		// debugging mode configuration; comment and uncomment the line to see
		// the difference
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.LIFE_CYCLE);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.INTERFACES);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.PORTS);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.CONNECTING);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.CALLING);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.EXECUTOR_SERVICES);

		// ---------------------------------------------------------------------
		// Creation phase
		// ---------------------------------------------------------------------

		if (thisJVMURI.equals(JVM_1_URI)) {
			
			AbstractComponent.createComponent(CEPBus.class.getCanonicalName(), new Object[0]);

		} else if (thisJVMURI.equals(JVM_2_URI)) {
			
			AbstractComponent.createComponent(CEPBus.class.getCanonicalName(), new Object[0]);
			
		} else {
			
			
			
		}

		super.instantiateAndPublish();
	}
	
	@Override
	public void interconnect()throws Exception {
		super.interconnect();
		
	}

}
