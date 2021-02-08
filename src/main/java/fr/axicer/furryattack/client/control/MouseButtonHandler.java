package fr.axicer.furryattack.client.control;

import fr.axicer.furryattack.client.FAClient;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseButtonHandler extends GLFWMouseButtonCallback{

	private static boolean pressedL,pressedR;

	private final FAClient client;

	public MouseButtonHandler(FAClient client) {
		this.client = client;
	}

	@Override
	public void invoke(long window, int button, int action, int mods) {
		if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
			client.getEventManager().sendEvent(new MousePressedEvent(button));
			pressedL = true;
		}else if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
			client.getEventManager().sendEvent(new MouseReleasedEvent(button));
			pressedL = false;
		}
		
		if(button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && action == GLFW.GLFW_PRESS) {
			client.getEventManager().sendEvent(new MousePressedEvent(button));
			pressedR = true;
		}else if(button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && action == GLFW.GLFW_RELEASE) {
			client.getEventManager().sendEvent(new MouseReleasedEvent(button));
			pressedR = false;
		}
	}

	
	public static boolean isPressedL() {
		return pressedL;
	}
	
	public static boolean isPressedR() {
		return pressedR;
	}
}
