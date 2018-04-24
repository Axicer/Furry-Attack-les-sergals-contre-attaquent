package fr.axicer.furryattack.util.control.events;

import fr.axicer.furryattack.util.events.AbstractEvent;

/**
 * Triggered by the event system when a key is pressed then released
 * @author Axicer
 *
 */
public class KeyTypedEvent extends AbstractEvent{

	private int key;
	
	public KeyTypedEvent(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}

}
