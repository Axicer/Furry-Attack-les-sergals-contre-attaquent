package fr.axicer.furryattack.util.control.events;

import fr.axicer.furryattack.util.events.AbstractEvent;

public class MouseReleasedEvent extends AbstractEvent{

	private int mouseButton;
	
	public MouseReleasedEvent(int mouseButton) {
		this.mouseButton = mouseButton;
	}

	public int getMouseButton() {
		return mouseButton;
	}
}
