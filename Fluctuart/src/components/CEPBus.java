package components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import components.interfaces.CEPBusManagementCI;
import components.interfaces.CEPBusManagementI;
import components.interfaces.EventEmissionCI;
import components.interfaces.EventEmissionI;
import components.interfaces.EventReceptionCI;
import connectors.EventReceptionConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.cps.smartcity.sim.vehicles.Vehicle;
import interfaces.EventI;
import ports.CEPBusManagementInboundPort;
import ports.EventEmissionInboundPort;
import ports.EventReceptionOutboundPort;

@OfferedInterfaces(offered={EventEmissionCI.class, CEPBusManagementCI.class})
@RequiredInterfaces(required={EventReceptionCI.class})
public class CEPBus extends AbstractComponent implements CEPBusManagementI, EventEmissionI{

	public static final String ManagementURI = "CEPBUS_MANAGEMENT_URI";
	
	private class EventReceptionOutboundConnection {
		public EventReceptionOutboundConnection(String inboundPortURI) throws Exception {
			receptionPort = new EventReceptionOutboundPort(CEPBus.this);
			receptionPort.localPublishPort();

			CEPBus.this.doPortConnection(receptionPort.getPortURI(), inboundPortURI, EventReceptionConnector.class.getCanonicalName());
		}
		public void destroy() throws Exception {
			CEPBus.this.doPortDisconnection(receptionPort.getPortURI());
			receptionPort.unpublishPort();
		}
		EventReceptionOutboundPort receptionPort;
	}
	
	private final HashMap<String, EventReceptionOutboundConnection> correlators = new HashMap<>();
	private final HashMap<String, ArrayList<String>> subscriptions = new HashMap<>();
	private final HashMap<String, String> executors = new HashMap<>();
	
	private final EventEmissionInboundPort emissionPort;
	private final CEPBusManagementInboundPort managementPort;
	
	private final ReadWriteLock rw = new ReentrantReadWriteLock();
	
	protected CEPBus() throws Exception {
		super(10, 10);
		
		emissionPort = new EventEmissionInboundPort(this);
		emissionPort.publishPort();
		
		managementPort = new CEPBusManagementInboundPort(ManagementURI, this);
		managementPort.publishPort();
		
		this.getTracer().setTitle("CEPBus");
		this.getTracer().setRelativePosition(0, 0);
		this.toggleTracing();
		
	}
	
	
	@Override
	public synchronized void finalise() throws Exception {
		
		Thread.sleep(2000);
		
		rw.writeLock().lock();
		try {
		
			for(String uri : correlators.keySet()) {
				EventReceptionOutboundConnection conn = correlators.get(uri);
				conn.destroy();
			}
			correlators.clear();
			
			super.finalise();
			
		} finally {
			rw.writeLock().unlock();
		}
	}

	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		
		
		rw.writeLock().lock();
		try {
		
			try {
				this.emissionPort.unpublishPort();
				this.managementPort.unpublishPort();
			} catch (Exception e) {
				throw new ComponentShutdownException(e) ;
			}
			super.shutdown();
			
		} finally {
			rw.writeLock().unlock();
		}
	}
	
	/*
	 	rw.readLock().lock();
	 	try {
			
		} finally {
			rw.readLock().unlock();
		}
	 */
	
	
	@Override
	public void sendEvent(String emitterURI, EventI event) throws Exception {
		rw.readLock().lock();
	 	try {
	 		this.traceMessage("Event received from " + emitterURI + " of name " + event.getPropertyValue("name") + "\n");
			
			if(subscriptions.containsKey(emitterURI)) {
				ArrayList<String> subscribers = subscriptions.get(emitterURI);
				for(String subscriber : subscribers) {
					EventReceptionOutboundConnection correlation = correlators.get(subscriber);
					correlation.receptionPort.receiveEvent(emitterURI, event);
				}
			}
		} finally {
			rw.readLock().unlock();
		}
	}

	@Override
	public void sendEvents(String emitterURI, EventI[] events) throws Exception {
		rw.readLock().lock();
	 	try {
	 		for(EventI event : events) {
				this.traceMessage("Events received from " + emitterURI + " of type " + event.getPropertyValue("name") + "\n");
			}
			
			if(subscriptions.containsKey(emitterURI)) {
				ArrayList<String> subscribers = subscriptions.get(emitterURI);
				for(String subscriber : subscribers) {
					EventReceptionOutboundConnection correlation = correlators.get(subscriber);
					correlation.receptionPort.receiveEvents(emitterURI, events);
				}
			}
		} finally {
			rw.readLock().unlock();
		}
	}
	
	

	@Override
	public String registerEmitter(String uri) throws Exception {
		rw.readLock().lock();
		try {
			return emissionPort.getPortURI();
		} finally {
			rw.readLock().unlock();
		}
	}

	@Override
	public void unregisterEmitter(String uri) throws Exception {
		
	}

	@Override
	public String registerCorrelator(String uri, String inboundPortURI) throws Exception {
		rw.writeLock().lock();
	 	try {
	 		correlators.put(uri, new EventReceptionOutboundConnection(inboundPortURI));
			return emissionPort.getPortURI();
		} finally {
			rw.writeLock().unlock();
		}
	}

	@Override
	public void unregisterCorrelator(String uri) throws Exception {
		rw.writeLock().lock();
	 	try {
	 		correlators.get(uri).destroy();
			correlators.remove(uri);
		} finally {
			rw.writeLock().unlock();
		}
	}

	@Override
	public void registerExecutor(String uri, String inboundPortURI) throws Exception {
		rw.writeLock().lock();
	 	try {
	 		executors.put(uri, inboundPortURI);
		} finally {
			rw.writeLock().unlock();
		}
	}

	@Override
	public String getExecutorInboundPortURI(String uri) {
		rw.readLock().lock();
		try {
			return executors.get(uri);
		} finally {
			rw.readLock().unlock();
		}
	}

	@Override
	public void unregisterExecutor(String uri) throws Exception {
		rw.writeLock().lock();
	 	try {
	 		executors.remove(uri);
		} finally {
			rw.writeLock().unlock();
		}
	}
	
	@Override
	public void subscribe(String subscriberURI, String emitterURI) {
		rw.writeLock().lock();
	 	try {
	 		ArrayList<String> subscribers = subscriptions.get(emitterURI);
			if(subscribers == null) {
				subscribers = new ArrayList<String>();
				subscriptions.put(emitterURI, subscribers);
			}
			subscribers.add(subscriberURI);
		} finally {
			rw.writeLock().unlock();
		}
	}

	@Override
	public void unsubscribe(String subscriberURI, String emitterURI) {
		rw.writeLock().lock();
	 	try {
	 		ArrayList<String> subscribers = subscriptions.get(emitterURI);
			assert subscribers != null;
			assert subscribers.contains(subscriberURI);
			subscribers.remove(subscriberURI);
		} finally {
			rw.writeLock().unlock();
		}
	}

}
