package fr.axicer.furryattack.util.events;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class EventManager {
	
	public List<EventListener> listeners;
	private List<EventListener> toDel;
	
	public EventManager() {
		listeners = new LinkedList<EventListener>();
		toDel = new LinkedList<>();
	}
	
	public boolean addListener(EventListener listener) {
		if(listeners.contains(listener))return false;
		return listeners.add(listener);
	}
	
	public boolean removeListener(EventListener listener) {
		if(!listeners.contains(listener))return false;
		return listeners.remove(listener);
	}
	
	public boolean addToDeletionList(EventListener listener) {
		if(toDel.contains(listener))return false;
		return toDel.add(listener);
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
		listeners.removeAll(toDel);
		toDel.clear();
	}
}
