package fr.axicer.furryattack.client.control;

import fr.axicer.furryattack.common.events.AEvent;

public class KeyReleasedEvent extends AEvent {

	private final int key;
	
	public KeyReleasedEvent(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}	
}
