package fr.axicer.furryattack.util.control;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;

public class MouseHandler implements GLFWCursorPosCallbackI{
	
	private static int posX,posY;
	
	// Mouse movement
    private static int mouseX, mouseY;
    private static int mouseDX, mouseDY;
	
	@Override
    public void invoke(long window, double xpos, double ypos) {
		posX = (int)xpos;
		posY = (int)ypos;
        // Add delta of x and y mouse coordinates
        mouseDX += (int)xpos - mouseX;
        mouseDY += (int)ypos - mouseY;
    }

	public static int getPosX() {
		return posX;
	}
	
	public static int getPosY() {
		return posY;
	}
	
	public static int getDX(){
        // Return mouse delta x and set delta x to 0
        return mouseDX | (mouseDX = 0);
    }
 
    public static int getDY(){
        // Return mouse delta y and set delta y to 0
        return mouseDY | (mouseDY = 0);
    }
}
