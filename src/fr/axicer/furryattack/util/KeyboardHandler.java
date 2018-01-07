package fr.axicer.furryattack.util;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyboardHandler extends GLFWKeyCallback{
	
	public static boolean[] keys = new boolean[65536];
	public static boolean[] newKeys = new boolean[65536];

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		newKeys[key] = (action != GLFW.GLFW_RELEASE) && !keys[key]; 
		keys[key] = action != GLFW.GLFW_RELEASE;
	}

	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}
	
	public static boolean isKeyDownOnce(int keycode) {
		return newKeys[keycode];
	}
}
