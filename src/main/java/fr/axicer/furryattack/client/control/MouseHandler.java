package fr.axicer.furryattack.client.control;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;

public class MouseHandler implements GLFWCursorPosCallbackI {

    // Mouse movement
    private static int posX, posY;
    private static int dX, dY;

    @Override
    public void invoke(long window, double xpos, double ypos) {
        dX = (int) (xpos - posX);
        dY = (int) (ypos - posY);

        posX = (int) xpos;
        posY = (int) ypos;
    }

    public static int getPosX() {
        return posX;
    }

    public static int getPosY() {
        return posY;
    }

    public static int getDX() {
        return dX;
    }

    public static int getDY() {
        return dY;
    }
}
