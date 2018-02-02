package fr.axicer.furryattack.util;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseButtonHandler extends GLFWMouseButtonCallback{

	private static boolean pressedL,pressedR;
	
	@Override
	public void invoke(long window, int button, int action, int mods) {
		if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
			pressedL = true;
		}else if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_RELEASE) {
			pressedL = false;
		}
		
		if(button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && action == GLFW.GLFW_PRESS) {
			pressedR = true;
		}else if(button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && action == GLFW.GLFW_RELEASE) {
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
