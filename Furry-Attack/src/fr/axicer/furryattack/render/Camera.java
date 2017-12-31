package fr.axicer.furryattack.render;

import org.joml.Matrix4f;

import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.math.Vector3F;

public class Camera {
	private Vector3F pos;
	private Vector3F rot;
	private float znear, zfar, FOV;
	
	public Camera(Vector3F pos, Vector3F rot, float znear, float zfar, float FOV) {
		this.pos = pos;
		this.rot = rot;
		this.znear = znear;
		this.zfar = zfar;
		this.FOV = FOV;
	}
	
	public Matrix4f getProjectionMatrix() {
		return new Matrix4f().perspective(FOV, Constants.WIDTH/Constants.HEIGHT, znear, zfar);
	}
	
	public Matrix4f getViewMatrix() {
		Matrix4f mat = new Matrix4f();
		mat.rotate(rot.x, 1, 0, 0);
		mat.rotate(rot.y, 0, 1, 0);
		mat.rotate(rot.z, 0, 0, 1);
		mat.translate(pos.x, pos.y, pos.z);
		return mat;
	}
	
	public void move(float x, float y, float z) {
		pos.x += x;
		pos.y += y;
		pos.z += z;
	}
	
	public void rotate(float x, float y, float z) {
		rot.x += x;
		rot.y += y;
		rot.z += z;
	}
}
