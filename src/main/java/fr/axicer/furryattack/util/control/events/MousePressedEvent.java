package fr.axicer.furryattack.util.control.events;

import fr.axicer.furryattack.util.events.AbstractEvent;

public class MousePressedEvent extends AbstractEvent{

	private int mouseButton;
	
	public MousePressedEvent(int mouseButton) {
		this.mouseButton = mouseButton;
	}

	public int getMouseButton() {
		return mouseButton;
	}
}
