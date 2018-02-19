package fr.axicer.furryattack.util.collision;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.render.shader.CollisionBoxShader;

public class CollisionBox {
	
	public FPoint A,B,C,D;
	
	private int vbo;
	private CollisionBoxShader shader;
	
	public CollisionBox() {
		this(new FPoint(0, 0),new FPoint(0, 0),new FPoint(0, 0), new FPoint(0, 0));
	}
	
	public CollisionBox(FPoint A, FPoint B, FPoint C, FPoint D) {
		this.A = A;
		this.B = B;
		this.C = C;
		this.D = D;
		shader = new CollisionBoxShader();
		shader.bind();
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
	    shader.setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
	    shader.setUniformMat4f("modelMatrix", new Matrix4f());
		shader.unbind();
		vbo = GL15.glGenBuffers();
		updateRender();
	}
	
	public void updatePos(FPoint A, FPoint B, FPoint C, FPoint D) {
		this.A = A;
		this.B = B;
		this.C = C;
		this.D = D;
		updateRender();
	}
	
	public void updateRender() {
		GL15.glDeleteBuffers(vbo);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(8*3);
		buffer.put(new float[] {A.x, A.y, -1f,
								B.x, B.y, -1f,
								B.x, B.y, -1f,
								C.x, C.y, -1f,
								C.x, C.y, -1f,
								D.x, D.y, -1f,
								D.x, D.y, -1f,
								A.x, A.y, -1f});
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
		
		GL11.glDrawArrays(GL11.GL_LINES, 0, 8);
		
		GL20.glDisableVertexAttribArray(vertexAttribLocation);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		shader.unbind();
		GL11.glLineWidth(1f);
	}
	
	public void destroy() {
		GL15.glDeleteBuffers(vbo);
	}
	
	public boolean isInside(float x, float y) {
		FPoint P = new FPoint(x, y);
		
		float Arec = calcSquare(A, B, C, D);

		float ABP = calcTriangle(A, B, P);
		float BCP = calcTriangle(B, C, P);
		float CDP = calcTriangle(C, D, P);
		float DAP = calcTriangle(D, A, P);
		
		return Math.abs(Arec-(ABP+BCP+CDP+DAP)) <= 3f;
	}

	public float calcSquare(FPoint A, FPoint B, FPoint C, FPoint D) {
		float t1 = calcTriangle(A, B, C);
		float t2 = calcTriangle(A, C, D);
		return t1+t2;
	}
	
	public float calcTriangle(FPoint A, FPoint B, FPoint C) {
		//return 0.5f*Math.abs((A.x*(B.y-C.y)	+	B.x*(C.y-A.y)	+	C.x*(A.y-B.y)));
		return 0.5f * Math.abs((B.x - A.x)*(C.y - A.y) - (C.x - A.x)*(B.y - A.y));
	}
}
