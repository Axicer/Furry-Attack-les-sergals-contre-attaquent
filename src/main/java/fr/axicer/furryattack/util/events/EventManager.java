package fr.axicer.furryattack.util.events;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EventManager {
	
	private Map<UUID, EventListener> listeners;
	
	public EventManager() {
		listeners = new HashMap<>();
	}
	
	public UUID addListener(EventListener listener) {
		UUID id = UUID.randomUUID();
		listeners.put(id, listener);
		return id;
	}
	
	public boolean removeListener(UUID id) {
		return listeners.remove(id) != null;
	}
	
	public void sendEvent(AbstractEvent e) {
		//creating a new map to avoid concurrent modification
		Map<UUID, EventListener> runningMap = new HashMap<>(listeners);
		for(EventListener l : runningMap.values()) {
			//iterating through all methods
			for(Method method : l.getClass().getMethods()){
				//keeping only methods with 1 argument (which should be the event type)
				if(method.getParameterTypes().length != 1)continue;
				//iterating through each parameter (only one parameter)
				for(Class<?> c : method.getParameterTypes()){
					//check if the type is the same
					if(c.isAssignableFrom(e.getClass())){
						//invoking the method
						try {
							method.invoke(l, e);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						break;
					}
				}
			}
		}
	}
}
