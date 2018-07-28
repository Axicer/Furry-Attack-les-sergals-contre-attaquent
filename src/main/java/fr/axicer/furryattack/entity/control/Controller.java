package fr.axicer.furryattack.entity.control;

import java.io.File;
import java.io.FileNotFoundException;

import org.lwjgl.glfw.GLFW;

import fr.axicer.furryattack.entity.Entity;
import fr.axicer.furryattack.generator.config.ControlConfigGenerator;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.util.config.Configuration;
import fr.axicer.furryattack.util.config.FileManager;
import fr.axicer.furryattack.util.control.KeyboardHandler;

/**
 * Main entity controller
 * @author Axicer
 *
 */
public class Controller implements Updateable, Renderable{

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
	}
	
	@Override
	public void update() {
		//add direction to the vector
		if(KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.UP_CONTROL_ID, GLFW.GLFW_KEY_Z))) {
			//do not move the character just make him look upward
			//vec.add(0f, SPEED);
		}
		if(KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.LEFT_CONTROL_ID, GLFW.GLFW_KEY_Q))) {
			entity.getAccelerationVector().add(-SPEED,0f);
		}
		if(KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.DOWN_CONTROL_ID, GLFW.GLFW_KEY_S))) {
			//do not move the character just make him look downward
			//vec.add(0f, -SPEED);
		}
		if(KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.RIGHT_CONTROL_ID, GLFW.GLFW_KEY_D))) {
			entity.getAccelerationVector().add(SPEED, 0f);
		}
		if(entity.isOnGround() && KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.JUMP_CONTROL_ID, GLFW.GLFW_KEY_SPACE))) {
			entity.getAccelerationVector().add(0f, 9*SPEED);
			entity.setOnGround(false);
		}
		if(KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.SHIFT_CONTROL_ID, GLFW.GLFW_KEY_LEFT_SHIFT))) {
			//do not move, just make him shift
			//vec.add(0f, -SPEED);
		}
		
		//move the entity
		entity.move();
		
		//apply any update to the entity
		entity.update();
	}

	@Override
	public void render() {
		entity.render();
	}
	
}