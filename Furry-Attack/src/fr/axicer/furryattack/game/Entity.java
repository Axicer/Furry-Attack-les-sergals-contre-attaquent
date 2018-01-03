package fr.axicer.furryattack.game;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.render.Camera3D;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.util.KeyboardHandler;
import fr.axicer.furryattack.util.MouseHandler;

public class Entity implements Updateable{
	
	public Camera3D cam;
	
	public Entity(Vector3f pos, Vector3f rot) {
		cam = new Camera3D(pos, rot, 0.1f, 1000.0f, 70.0f);
	}
	
	@Override
	public void update() {
		float speed = 1;
		Vector3f vec = new Vector3f();
		
		if(KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_W)) {
			vec.add(getDirection().mul(speed));
		}
		if(KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_A)) {
			vec.add(getDirection().rotateY((float)Math.PI/2));
		}
		if(KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_S)) {
			vec.add(getDirection().mul(-speed));
		}
		if(KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_D)) {
			vec.add(getDirection().rotateY((float)-Math.PI/2));
		}
		if(KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
			vec.add(0, speed, 0);
		}
		if(KeyboardHandler.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			vec.add(0, -speed, 0);
		}
		
		cam.move(vec.x, vec.y, vec.z);

		if(FurryAttack.getInstance().MouseGrabbed) {
			float dy = MouseHandler.getDY();
			float dx = MouseHandler.getDX();
			
			float rotY = dx*0.0003f;
			float rotX = -dy*0.0003f;

			cam.rotate(rotX, rotY, 0);
		}
	}
	
	public Vector3f getDirection(){
		Vector3f r = new Vector3f();
		
		Vector3f rot = new Vector3f(cam.getRotation());
		
		float cosY = (float) Math.cos(rot.y - Math.PI/2);
		float sinY = (float) Math.sin(rot.y - Math.PI/2);
		float cosP = (float) Math.cos(-rot.x);
		float sinP = (float) Math.sin(-rot.x);
		
		r.x = cosY*cosP;
		r.y = sinP;
		r.z = sinY*cosP;
		
		r.normalize();
		
		return r;
	}
}
