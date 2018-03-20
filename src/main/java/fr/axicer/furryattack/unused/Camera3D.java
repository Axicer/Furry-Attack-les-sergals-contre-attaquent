package fr.axicer.furryattack.unused;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import fr.axicer.furryattack.util.Constants;

public class Camera3D {
	private Vector3f pos;
	private Vector3f rot;
	private float znear, zfar, FOV;
	
	public Camera3D(Vector3f pos, Vector3f rot, float znear, float zfar, float FOV) {
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
		mat.rotate(-rot.x, 1, 0, 0);
		mat.rotate(rot.y, 0, 1, 0);
		mat.rotate(rot.z, 0, 0, 1);
		mat.translate(-pos.x, -pos.y, -pos.z);
		return mat;
	}
	
	public void rotate(float x, float y, float z) {
		if(rot.x+x > Math.PI/2) {
			rot.x = (float)Math.PI/2f;
		}else if(rot.x+x < -Math.PI/2){
			rot.x = (float)-Math.PI/2f;
		}else {
			rot.x += x;
		}
		
		if(rot.y+y > 2*Math.PI) {
			rot.y = (float)((rot.y+y)-2*Math.PI);
		}else if(rot.y+y < 0){
			rot.y = (float)(2*Math.PI+(rot.y+y));
		}else {
			rot.y += y;
		}
		
		rot.z += z;
	}
	
	public void move(float x, float y, float z) {
		pos.x += x;
		pos.y += y;
		pos.z += z;
	}
	
	public Vector3f getRotation() {
		return this.rot;
	}
	
	public Vector3f getPosition() {
		return this.pos;
	}
	
}
