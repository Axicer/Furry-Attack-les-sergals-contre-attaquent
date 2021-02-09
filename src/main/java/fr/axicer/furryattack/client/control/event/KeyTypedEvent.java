package fr.axicer.furryattack.client.control.event;

import fr.axicer.furryattack.common.events.AEvent;

/**
 * Triggered by the event system when a key is pressed then released
 * @author Axicer
 *
 */
public class KeyTypedEvent extends AEvent {

	private final int key;
	
	public KeyTypedEvent(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}

}
