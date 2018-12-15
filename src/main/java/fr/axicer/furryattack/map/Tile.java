package fr.axicer.furryattack.map;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.render.Destroyable;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.render.shaders.TileShader;
import fr.axicer.furryattack.util.Color;

public class Tile implements Renderable, Updateable, Destroyable, TileEncoder, TileDecoder{
	
	//data of the tile
	private long data;
	//VBO id
	private int VBO;
	//shader to use
	private TileShader shader;
	//our model matrix
	private Matrix4f model;
	
	public Tile(int x, int y, long data) {
		this.data = data;
		
		//initiate VBO
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(3);
		vertexBuffer.put(new float[] {x,y,0f});
		vertexBuffer.flip();
		this.VBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.VBO);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	
		//create shader
		shader = new TileShader();
		
		//get color from data and parse as a color class
		Color color = Color.fromInt(getColor());
		//set model to our translation
		model = new Matrix4f().translate(x*TileContainer.TILE_SIZE, y*TileContainer.TILE_SIZE, 0);
		//set color as tile color in shader and set tile width and height
		shader.fillShader(color.x, color.y, color.z, color.w, TileContainer.TILE_SIZE, model);
	}
	
	/*--------------------- GETTERS ----------------------*/
	
	public boolean isSolid() {
		//solid is pos 0
		return getBit(data, 0);
	}
	
	public int getHeal() {
		//return the 3 bits of heal at pos 35
		return getNbits(data, 3, 35);
	}
	
	public int getColor() {
		//return the first 4 octets
		return getNbits(data, 4*8, 0);
	}
	
	/* -------------------- SETTERS -----------------*/
	
	public Tile setSolid(boolean value) {
		//solid is pos 0
		this.data = setBit(data, (value? 1 : 0), 0);
		return this;
	}
	
	public Tile setColor(int color) {
		//set the 4 first octets to the color starting from 0
		this.data = setNbits(data, color, 4*8, 0);
		//get color from data and parse as a color class
		Color ccolor = Color.fromInt(color);
		//set color as tile color in shader and set tile width and height
		shader.fillShader(ccolor.x, ccolor.y, ccolor.z, ccolor.w, TileContainer.TILE_SIZE, model);
		return this;
	}
	
	public Tile setHeal(int heal) {
		//set the 3 bits value of heal inside the tile at pos 35
		this.data = setNbits(data, heal, 3, 35);
		return this;
	}

	/* --------------------------------------------------*/
	
	@Override
	public void destroy() {
		GL15.glDeleteBuffers(VBO);
	}

	@Override
	public void update() {
		//TODO
	}

	@Override
	public void render() {
		//enable blending
		GL11.glEnable(GL11.GL_BLEND);
		//defines blend functions
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//bind shader
		shader.bind();
		
		//get the vertex attrib location ID from the shader
		int vertexAttribLocation = GL20.glGetAttribLocation(shader.program, "position");
		//bind the vertex attrib location for the shader
		GL20.glEnableVertexAttribArray(vertexAttribLocation);
		//bind the buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
		
		//push buffer's data into the vertex attrib location in the shader
		GL20.glVertexAttribPointer(vertexAttribLocation, 3, GL11.GL_FLOAT, false, 0, 0);
		//draw a point
		GL11.glDrawArrays(GL11.GL_POINTS, 0, 1);
		//unbind vertex attrib location for the shader
		GL20.glDisableVertexAttribArray(vertexAttribLocation);
		
		//unbind buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		//unbind the shader
		shader.unbind();
		
		//disable blending
		GL11.glDisable(GL11.GL_BLEND);
	}
}
