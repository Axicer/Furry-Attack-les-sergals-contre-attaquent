package fr.axicer.furryattack.util.control.events;

import fr.axicer.furryattack.util.events.AbstractEvent;

public class KeyPressedEvent extends AbstractEvent{

	private int key;
	
	public KeyPressedEvent(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}

}
