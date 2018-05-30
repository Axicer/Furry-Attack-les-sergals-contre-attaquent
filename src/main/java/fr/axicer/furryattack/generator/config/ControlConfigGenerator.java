package fr.axicer.furryattack.generator.config;

import org.lwjgl.glfw.GLFW;

import fr.axicer.furryattack.generator.AbstractGenerator;
import fr.axicer.furryattack.util.config.Configuration;

public class ControlConfigGenerator extends AbstractGenerator{

	public static final String CONFIG_NAME = "config.json";
	public static final String UP_CONTROL_ID = "up";
	public static final String LEFT_CONTROL_ID = "left";
	public static final String DOWN_CONTROL_ID = "down";
	public static final String RIGHT_CONTROL_ID = "right";
	public static final String JUMP_CONTROL_ID = "jump";
	public static final String SHIFT_CONTROL_ID = "shift";
	
	public ControlConfigGenerator() {}

	public Configuration generate() {
		Configuration c = new Configuration();
		c.setInt(UP_CONTROL_ID, GLFW.GLFW_KEY_Z, true);
		c.setInt(LEFT_CONTROL_ID, GLFW.GLFW_KEY_Q, true);
		c.setInt(DOWN_CONTROL_ID, GLFW.GLFW_KEY_S, true);
		c.setInt(RIGHT_CONTROL_ID, GLFW.GLFW_KEY_D, true);
		c.setInt(JUMP_CONTROL_ID, GLFW.GLFW_KEY_SPACE, true);
		c.setInt(SHIFT_CONTROL_ID, GLFW.GLFW_KEY_LEFT_SHIFT, true);
		return c;
	}
	
}
