package fr.axicer.furryattack.entity.control;

import java.io.File;
import java.io.FileNotFoundException;

import org.lwjgl.glfw.GLFW;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.entity.Entity;
import fr.axicer.furryattack.generator.config.ControlConfigGenerator;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.util.config.Configuration;
import fr.axicer.furryattack.util.config.FileManager;
import fr.axicer.furryattack.util.control.KeyboardHandler;
import fr.axicer.furryattack.util.control.events.MousePressedEvent;
import fr.axicer.furryattack.util.events.EventListener;

/**
 * Main entity controller
 * @author Axicer
 *
 */
public class Controller implements Updateable, Renderable, EventListener{

	//default entity speed
	public static final float SPEED = 2f;
	//enrolled entity to move
	private Entity entity;
	
	//control configuration
	private Configuration c;
	
	/**
	 * Main constructor of a {@link Controller}
	 * @param entity {@link Entity} to control
	 */
	public Controller(Entity entity) {
		this.entity = entity;
		File f = new File(FileManager.CONFIG_FOLDER_FILE, "control.json");
		if(!f.exists()) {
			c = new ControlConfigGenerator().generate();
		}else {
			try {
				c = new Configuration(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		c.save(f);
		FurryAttack.getInstance().getEventManager().addListener(this);
	}
	
	@Override
	public void update() {
		if(entity == null)return;
		
		//add direction to the vector
		if(KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.UP_CONTROL_ID, GLFW.GLFW_KEY_Z))) {
			//TODO remove when collision will be done
			//do not move the character just make him look upward
			entity.getAccelerationVector().add(0f, SPEED);
		}
		if(KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.LEFT_CONTROL_ID, GLFW.GLFW_KEY_Q))) {
			entity.getAccelerationVector().add(-SPEED,0f);
		}
		if(KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.DOWN_CONTROL_ID, GLFW.GLFW_KEY_S))) {
			//TODO remove movement when collision will be done
			//do not move the character just make him look downward
			entity.getAccelerationVector().add(0f, -SPEED);
		}
		if(KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.RIGHT_CONTROL_ID, GLFW.GLFW_KEY_D))) {
			entity.getAccelerationVector().add(SPEED, 0f);
		}
		if(entity.isOnGround() && KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.JUMP_CONTROL_ID, GLFW.GLFW_KEY_SPACE))) {
			entity.getAccelerationVector().add(0f, SPEED);
			//TODO apply gravity when collision will be done
//			entity.setGrounded(false);
		}
		if(KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.SHIFT_CONTROL_ID, GLFW.GLFW_KEY_LEFT_SHIFT))) {
			//TODO delete movement just change shift status
			entity.getAccelerationVector().add(0f, -SPEED);
			//do not move, just make him shift
//			entity.setShifted(true);
		}else {
			//unshift entity
//			entity.setShifted(false);
		}
		
		
		//move the entity
		entity.move();
		
		//apply any update to the entity
		entity.update();
	}

	public void onMousePressed(MousePressedEvent ev) {
		//TODO gun management
	}
	
	@Override
	public void render() {
		if(entity == null)return;
		entity.render();
	}
	
}
