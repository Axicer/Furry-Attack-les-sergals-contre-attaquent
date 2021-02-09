package fr.axicer.furryattack.client.control.event;

import fr.axicer.furryattack.common.events.AEvent;

public class MouseReleasedEvent extends AEvent {

	private final int mouseButton;
	
	public MouseReleasedEvent(int mouseButton) {
		this.mouseButton = mouseButton;
	}

	public int getMouseButton() {
		return mouseButton;
	}
}
