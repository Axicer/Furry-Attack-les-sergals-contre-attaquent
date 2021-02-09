package fr.axicer.furryattack.client.control;

import fr.axicer.furryattack.client.FAClient;
import fr.axicer.furryattack.client.control.event.KeyPressedEvent;
import fr.axicer.furryattack.common.events.EventListener;
import org.lwjgl.glfw.GLFW;

/**
 * A listener that will close the program when escaped is pressed
 */
public class EscapeListener implements EventListener {

    FAClient client;

    public EscapeListener(FAClient client) {
        this.client = client;
    }

    public void keyPressed(KeyPressedEvent event){
        if(event.getKey() == GLFW.GLFW_KEY_ESCAPE){
            client.stop();
        }
    }
}
