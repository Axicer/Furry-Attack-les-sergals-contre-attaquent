package fr.axicer.furryattack.entity.control;

import java.io.File;
import java.io.FileNotFoundException;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.entity.Character;
import fr.axicer.furryattack.generator.config.ControlConfigGenerator;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.util.config.Configuration;
import fr.axicer.furryattack.util.config.FileManager;
import fr.axicer.furryattack.util.control.KeyboardHandler;

public class Controller implements Updateable, Renderable{

	public static final float SPEED = 2f;
	private Character entity;
	private Configuration c;
	private Vector2f vec;
	private float glidingCoeff = 0.7f;
	private float airBrakingCoeff = 0.999f;
	
	public Controller(Character entity) {
		this.entity = entity;
		this.vec = new Vector2f();
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

	/**
	 * Set the gliding coefficient to apply to the acceleration vector
	 * @param glidingCoeff float gliding coefficient
	 */
	public void setGlidingCoeff(float glidingCoeff) {
		this.glidingCoeff = glidingCoeff;
	}
	
	/**
	 * Returns the actual gliding coefficient applied to the acceleration vector
	 * @return float gliding coefficient
	 */
	public float getGlidingCoeff() {
		return this.glidingCoeff;
	}
	
	/**
	 * Set the air Braking coefficient to apply to the acceleration vector
	 * @param airBrakingCoeff float gliding coefficient
	 */
	public void setAirBrakingCoeff(float airBrakingCoeff) {
		this.airBrakingCoeff = airBrakingCoeff;
	}
	
	/**
	 * Returns the actual air Braking coefficient applied to the acceleration vector
	 * @return float air Braking coefficient
	 */
	public float getairBrakingCoeff() {
		return this.airBrakingCoeff;
	}
	
	@Override
	public void update() {
		//add direction to the vector
		if(KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.UP_CONTROL_ID, GLFW.GLFW_KEY_Z))) {
			//do not move the character just make him look upward
			//vec.add(0f, SPEED);
		}
		if(KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.LEFT_CONTROL_ID, GLFW.GLFW_KEY_Q))) {
			vec.add(-SPEED,0f);
		}
		if(KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.DOWN_CONTROL_ID, GLFW.GLFW_KEY_S))) {
			//do not move the character just make him look downward
			//vec.add(0f, -SPEED);
		}
		if(KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.RIGHT_CONTROL_ID, GLFW.GLFW_KEY_D))) {
			vec.add(SPEED, 0f);
		}
		if(entity.onGround && KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.JUMP_CONTROL_ID, GLFW.GLFW_KEY_SPACE))) {
			vec.add(0f, 9*SPEED);
			entity.onGround = false;
		}
		if(KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.SHIFT_CONTROL_ID, GLFW.GLFW_KEY_LEFT_SHIFT))) {
			//do not move, just make him shift
			//vec.add(0f, -SPEED);
		}
		
		//apply gravity factor
		if(!entity.onGround)vec.set(vec.x, vec.y-FurryAttack.getInstance().getMapManager().getMap().getGravity());
		else vec.set(vec.x, 0);
			
		//move the entity
		entity.move(vec.x, vec.y);
		
		//aply coef
		vec.set(vec.x*glidingCoeff*airBrakingCoeff, vec.y*airBrakingCoeff);
		
		//apply any update to the entity
		entity.update();
	}

	@Override
	public void render() {
		entity.render();
	}
	
	
}
