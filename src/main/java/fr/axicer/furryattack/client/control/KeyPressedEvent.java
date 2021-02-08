package fr.axicer.furryattack.client.control;

import fr.axicer.furryattack.common.events.AEvent;

/**
 * Event fired when a key is pressedd
 * @author Axicer
 *
 */
public class KeyPressedEvent extends AEvent {

	private final int key;
	
	public KeyPressedEvent(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}
}
