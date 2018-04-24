package fr.axicer.furryattack.util.control;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.util.control.events.KeyPressedEvent;
import fr.axicer.furryattack.util.control.events.KeyReleasedEvent;
import fr.axicer.furryattack.util.control.events.KeyTypedEvent;

public class KeyboardHandler extends GLFWKeyCallback{
	
	public static boolean[] keys = new boolean[65536];
	public static boolean[] newKeys = new boolean[65536];

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		newKeys[key] = (action != GLFW.GLFW_RELEASE) && !keys[key];
		keys[key] = action != GLFW.GLFW_RELEASE;
		if(newKeys[key]) {
			FurryAttack.getInstance().getEventManager().sendEvent(new KeyTypedEvent(key));
		}
		if(action == GLFW.GLFW_PRESS) {
			FurryAttack.getInstance().getEventManager().sendEvent(new KeyPressedEvent(key));
		}
		if(action == GLFW.GLFW_RELEASE) {
			FurryAttack.getInstance().getEventManager().sendEvent(new KeyReleasedEvent(key));
		}
	}

	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}
	
	public static boolean isKeyDownOnce(int keycode) {
		return newKeys[keycode];
	}
	
	public static int getFirstKeyDown() {
		for(int i = 0 ; i < keys.length ; i++) {
			if(keys[i])return i;
		}
		return -1;
	}
}
