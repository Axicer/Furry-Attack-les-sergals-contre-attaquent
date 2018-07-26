package fr.axicer.furryattack.entity.control;

import java.io.File;
import java.io.FileNotFoundException;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.generator.config.ControlConfigGenerator;
import fr.axicer.furryattack.map.MapObstacle;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.util.config.Configuration;
import fr.axicer.furryattack.util.config.FileManager;
import fr.axicer.furryattack.util.control.KeyboardHandler;

public class Controller implements Updateable, Renderable{

	public static final float SPEED = 2f;
	private Entity entity;
	private Configuration c;
	private Vector2f vec;
	private float glidingCoeff = 0.7f;
	
	public Controller(Entity entity) {
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
	
	@Override
	public void update() {
		//add direction to the vector
		if(KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.UP_CONTROL_ID, GLFW.GLFW_KEY_Z))) {
			//do not move the character just make him look upward
			vec.add(0f, SPEED);
		}
		if(KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.LEFT_CONTROL_ID, GLFW.GLFW_KEY_Q))) {
			System.out.println("left");
			vec.add(-SPEED,0f);
		}
		if(KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.DOWN_CONTROL_ID, GLFW.GLFW_KEY_S))) {
			//do not move the character just make him look downward
			vec.add(0f, -SPEED);
		}
		if(KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.RIGHT_CONTROL_ID, GLFW.GLFW_KEY_D))) {
			System.out.println("right");
			vec.add(SPEED, 0f);
		}
		if(KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.JUMP_CONTROL_ID, GLFW.GLFW_KEY_SPACE))) {
			System.out.println("jump");
			vec.add(0f, SPEED);
		}
		if(KeyboardHandler.isKeyDown(c.getInt(ControlConfigGenerator.SHIFT_CONTROL_ID, GLFW.GLFW_KEY_LEFT_SHIFT))) {
			//do not move, just make him shift
			vec.add(0f, -SPEED);
		}
		
		//apply gravity factor
		vec.set(vec.x, vec.y-FurryAttack.getInstance().getMapManager().getMap().getGravity());

		//for each obstacles
		for(MapObstacle obstacle : FurryAttack.getInstance().getMapManager().getMap().getObstacles()) {
			//if we will collide on y axis
			if(obstacle.isInside(entity.pos.x, entity.pos.y+vec.y)) {
				vec.y = 0;
			}
			//if we will collide on x axis
			if(obstacle.isInside(entity.pos.x+vec.x, entity.pos.y)) {
				vec.x = 0;
			}
		}
		
		//move the entity
		entity.move(vec.x, vec.y);
		
		//set the gliding coeff and stop the y axis
		vec.set(vec.x*glidingCoeff, vec.y);
		
		//apply any update to the entity
		entity.update();
	}

	@Override
	public void render() {
		entity.render();
	}
	
	
}
