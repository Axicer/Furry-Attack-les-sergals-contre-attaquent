package fr.axicer.furryattack.util.collision;

import java.awt.Polygon;
import java.awt.geom.Area;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.render.shaders.BorderShader;

public class CollisionBoxM {
	
	protected Vector2f[] points;
	protected int vbo;
	protected BorderShader shader;
	
	public CollisionBoxM() {
		shader = new BorderShader();
		shader.bind();
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
		shader.setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
		shader.setUniformMat4f("modelMatrix", new Matrix4f());
		shader.unbind();
		vbo = GL15.glGenBuffers();
	}
	
	public CollisionBoxM(Vector2f... points) {
		this();
		this.points = points;
		this.updateRender();
	}
	
	public CollisionBoxM(float... coords) {
		this();
		updatePos(coords);
	}
	
	public void updatePos(Vector2f... points) {
		this.points = points;
		updateRender();
	}
	public void updatePos(float... coords) {
		int pointsNumber = (int) Math.floor(coords.length/2);
		this.points = new Vector2f[pointsNumber];
		for(int i = 0 ; i < this.points.length ; i++) {
			this.points[i] = new Vector2f(coords[i*2], coords[i*2+1]);
		}
		updateRender();
	}
	
	public void updateRender() {
		GL15.glDeleteBuffers(vbo);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(points.length*3);
		for(int  i = 0 ; i < points.length ; i++) {
			buffer.put(new float[] {points[i].x, points[i].y, -1f});
		}
		buffer.flip();
		
		vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public void render() {
		GL11.glLineWidth(2f);
		shader.bind();
		int vertexAttribLocation = GL20.glGetAttribLocation(shader.program, "position");
		GL20.glEnableVertexAttribArray(vertexAttribLocation);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL20.glVertexAttribPointer(vertexAttribLocation, 3, GL11.GL_FLOAT, false, 0, 0);
		
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL11.glDrawArrays(GL11.GL_POLYGON, 0, points.length);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		
		GL20.glDisableVertexAttribArray(vertexAttribLocation);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		shader.unbind();
		GL11.glLineWidth(1f);
	}
	
	public void destroy() {
		GL15.glDeleteBuffers(vbo);
	}
	
	public boolean isInside(float x, float y) {
		Vector2f P = new Vector2f(x, y);
		
		float Arec = calcPolygonSquare(points);

		float cumulatedTriangleArea = 0f;
		for(int i = 0 ; i < points.length ; i++) {
			if(i+1 == points.length)cumulatedTriangleArea += calcTriangle(points[i], points[0], P);
			else cumulatedTriangleArea += calcTriangle(points[i], points[i+1], P);
		}
		
		return Math.abs(Arec-cumulatedTriangleArea) <= 3f;
	}

	public float calcPolygonSquare(Vector2f... points) {
		float crossProduct = 0f;
		for(int i = 0 ; i < points.length ; i++) {
			if(i+1 == points.length)crossProduct += (points[i].x*points[0].y)-(points[i].y*points[0].x);
			else crossProduct += (points[i].x*points[i+1].y)-(points[i].y*points[i+1].x);
		}
		return (float)Math.abs(crossProduct/2f);
	}
	
	public float calcTriangle(Vector2f A, Vector2f B, Vector2f C) {
		//return 0.5f*Math.abs((A.x*(B.y-C.y)	+	B.x*(C.y-A.y)	+	C.x*(A.y-B.y)));
		return 0.5f * Math.abs((B.x - A.x)*(C.y - A.y) - (C.x - A.x)*(B.y - A.y));
	}
	
	public boolean intersect(CollisionBoxM boxA) {
		return CollisionBoxM.intersect(this, boxA);
	}
	
	public static boolean intersect(CollisionBoxM boxA, CollisionBoxM boxB) {
		Polygon polyA = new Polygon();
		Polygon polyB = new Polygon();
		for(int i = 0 ; i < boxA.points.length ; i++) {
			polyA.addPoint((int)boxA.points[i].x, (int)boxA.points[i].y);
		}
		for(int i = 0 ; i < boxB.points.length ; i++) {
			polyB.addPoint((int)boxB.points[i].x, (int)boxB.points[i].y);
		}
		Area area = new Area(polyA);
		area.intersect(new Area(polyB));
		return !area.isEmpty();
	}
	
	public void rotateFrom(float x, float y, float angle) {
		float s = (float)Math.sin(angle);
		float c = (float)Math.cos(angle);
		for(Vector2f vec : points) {
			// translate point back to origin:
			vec.x -= x;
			vec.y -= y;
			// rotate point
			float xnew = vec.x * c - vec.y * s;
			float ynew = vec.x * s + vec.y * c;
			// translate point back:
			vec.x = xnew + x;
			vec.y = ynew + y;
		}
		updateRender();
	}
	public void move(float dx, float dy) {
		for(Vector2f vec: points){
			vec.x += dx;
			vec.y += dy;
		}
		updateRender();
	}
	
	public CollisionBoxM clone() {
		return new CollisionBoxM(points);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[CollisionBoxM]:\n");
		builder.append("\tpoints: [\n");
		for(int i = 0 ; i < points.length ; i++) {
			builder.append("\t\tx : "+points[i].x+" ");
			builder.append("\t\ty : "+points[i].y+"\n");
		}
		builder.append("\t]");
		return builder.toString();
		
	}
}
