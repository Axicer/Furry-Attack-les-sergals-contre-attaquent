package fr.axicer.furryattack.util.control.events;

import fr.axicer.furryattack.util.events.AbstractEvent;

public class KeyReleasedEvent extends AbstractEvent{

	private int key;
	
	public KeyReleasedEvent(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}	
}
