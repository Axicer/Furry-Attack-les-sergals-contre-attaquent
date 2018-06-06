package fr.axicer.furryattack.util.collision;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.render.shader.CollisionBoxShader;

public class CollisionBoxM {
	
	public Vector2f[] points;
	
	protected int vbo;
	protected CollisionBoxShader shader;
	
	public CollisionBoxM() {
		this(new Vector2f(),new Vector2f(),new Vector2f(),new Vector2f());
	}
	
	public CollisionBoxM(Vector2f... points) {
		this.points = points;
		shader = new CollisionBoxShader();
		shader.bind();
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
	    shader.setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
	    shader.setUniformMat4f("modelMatrix", new Matrix4f());
		shader.unbind();
		vbo = GL15.glGenBuffers();
		this.updateRender();
	}
	
	public void updatePos(Vector2f... points) {
		this.points = points;
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
		
		GL11.glDrawArrays(GL11.GL_POLYGON, 0, points.length);
		
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
