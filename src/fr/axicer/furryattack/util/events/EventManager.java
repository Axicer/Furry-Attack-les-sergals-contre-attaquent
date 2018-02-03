package fr.axicer.furryattack.util.events;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class EventManager {
	
	private List<EventListener> listeners;
	
	public EventManager() {
		listeners = new LinkedList<EventListener>();
	}
	
	public boolean addListener(EventListener listener) {
		if(!listeners.contains(listener)) {
			listeners.add(listener);
			return true;
		}
		return false;
	}
	
	public boolean removeListener(EventListener listener) {
		if(listeners.contains(listener)) {
			listeners.remove(listener);
			return true;
		}
		return false;
	}
	
	public void sendEvent(AbstractEvent e) {
		listeners.forEach(l -> {
			for(Method method : l.getClass().getMethods()){
				if(method.getParameterTypes().length != 1)continue;
				boolean found = false;
				for(Class<?> c : method.getParameterTypes()){
					if(c.isAssignableFrom(e.getClass())){
						found = true;
						break;
					}
				}
				if(found){
					try {
						method.invoke(l, e);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
	}
}
