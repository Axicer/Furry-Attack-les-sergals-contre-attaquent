package fr.axicer.furryattack.util.control;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.util.control.events.MousePressedEvent;
import fr.axicer.furryattack.util.control.events.MouseReleasedEvent;

public class MouseButtonHandler extends GLFWMouseButtonCallback{

	private static boolean pressedL,pressedR;
	
	@Override
	public void invoke(long window, int button, int action, int mods) {
		if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
			FurryAttack.getInstance().getEventManager().sendEvent(new MousePressedEvent(button));
			pressedL = true;
		}else if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
			FurryAttack.getInstance().getEventManager().sendEvent(new MouseReleasedEvent(button));
			pressedL = false;
		}
		
		if(button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && action == GLFW.GLFW_PRESS) {
			FurryAttack.getInstance().getEventManager().sendEvent(new MousePressedEvent(button));
			pressedR = true;
		}else if(button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && action == GLFW.GLFW_RELEASE) {
			FurryAttack.getInstance().getEventManager().sendEvent(new MouseReleasedEvent(button));
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
