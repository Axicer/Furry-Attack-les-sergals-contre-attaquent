package fr.axicer.furryattack.client.control.event;

import fr.axicer.furryattack.common.events.AEvent;

public class MousePressedEvent extends AEvent {

	private final int mouseButton;
	
	public MousePressedEvent(int mouseButton) {
		this.mouseButton = mouseButton;
	}

	public int getMouseButton() {
		return mouseButton;
	}
}
