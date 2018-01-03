package fr.axicer.furryattack.render;

import org.joml.Math;
import org.joml.Matrix4f;

import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.math.Vector3F;

public class Camera3D {
	private Vector3F pos;
	private Vector3F rot;
	private float znear, zfar, FOV;
	
	public Camera3D(Vector3F pos, Vector3F rot, float znear, float zfar, float FOV) {
		this.pos = pos;
		this.rot = rot;
		this.znear = znear;
		this.zfar = zfar;
		this.FOV = FOV;
	}
	
	public Matrix4f getProjectionMatrix() {
		return new Matrix4f().perspective((float)Math.toRadians(FOV), (float)Constants.WIDTH/(float)Constants.HEIGHT, znear, zfar);
	}
	
	public Matrix4f getViewMatrix() {
		Matrix4f mat = new Matrix4f();
		mat.rotate((float)Math.toRadians(rot.x), 1, 0, 0);
		mat.rotate((float)Math.toRadians(rot.y), 0, 1, 0);
		mat.rotate((float)Math.toRadians(rot.z), 0, 0, 1);
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
	
	public Vector3F getRotation() {
		return this.rot;
	}
	
	public Vector3F getPosition() {
		return this.pos;
	}
}
