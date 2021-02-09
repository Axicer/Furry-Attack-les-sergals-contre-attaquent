package fr.axicer.furryattack.client.control.handler;

import fr.axicer.furryattack.client.FAClient;
import fr.axicer.furryattack.client.control.event.KeyPressedEvent;
import fr.axicer.furryattack.client.control.event.KeyReleasedEvent;
import fr.axicer.furryattack.client.control.event.KeyTypedEvent;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyboardHandler extends GLFWKeyCallback{
	
	public static boolean[] keys = new boolean[65536];
	public static boolean[] newKeys = new boolean[65536];

	private final FAClient client;

	public KeyboardHandler(FAClient client) {
		this.client = client;
	}

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		newKeys[key] = (action != GLFW.GLFW_RELEASE) && !keys[key];
		keys[key] = action != GLFW.GLFW_RELEASE;
		if(newKeys[key]) {
			client.getEventManager().sendEvent(new KeyTypedEvent(key));
		}
		if(action == GLFW.GLFW_PRESS) {
			client.getEventManager().sendEvent(new KeyPressedEvent(key));
		}
		if(action == GLFW.GLFW_RELEASE) {
			client.getEventManager().sendEvent(new KeyReleasedEvent(key));
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
