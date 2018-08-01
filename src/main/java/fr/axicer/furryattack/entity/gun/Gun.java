package fr.axicer.furryattack.entity.gun;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.entity.Species;
import fr.axicer.furryattack.entity.gun.bullet.Bullet;
import fr.axicer.furryattack.map.MapObstacle;
import fr.axicer.furryattack.render.Destroyable;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.render.shaders.GunShader;
import fr.axicer.furryattack.render.textures.Texture;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.control.MouseHandler;

/**
 * A simple gun 
 * @author Axicer
 *
 */
public class Gun implements Updateable,Renderable,Destroyable{

	//gun position
	protected Vector2f pos;
	//gun type
	protected GunType type;
	//damage and strength
	private float bulletsStrength, bulletsDamage;
	//amount of bullets in this gun
	private int bulletsAmount;
	//gun rotation
	protected float rot;
	//if the gun should be reverted (looking on the left)
	protected boolean revert;
	//gun texture
	protected Texture gunTexture;
	//gun shader
	private GunShader shader;
	//model matrix
	private Matrix4f modelMatrix;
	//list of all bullets launched
	private List<Bullet> launchedBullets;
	
	private int VBO_ID;
	
	public Vector2f getPos() {
		return pos;
	}

	public Gun(GunType type, int bulletAmount, Species race) {
		this.type = type;
		this.bulletsDamage = type.getBulletsDamage();
		this.bulletsStrength = type.getBulletsStrength();
		this.bulletsAmount = bulletAmount;
		this.rot = 0f;
		this.pos = new Vector2f();
		this.gunTexture = Texture.loadTexture(type.getImgPath(race), GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST);
		this.shader = new GunShader();
		this.modelMatrix = new Matrix4f().identity()
				.translate(pos.x*Constants.WIDTH, pos.y*Constants.HEIGHT, 0f)
				.rotateZ(rot);
		this.launchedBullets = new ArrayList<>();
		shader.bind();
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
		shader.setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		shader.setUniformi("tex", 0);
	    shader.setUniformf("width", getGunWidth()*Constants.WIDTH);
	    shader.setUniformf("height", getGunHeight()*Constants.HEIGHT);
	    shader.setUniformi("revert", revert ? 1 : 0);
		shader.unbind();
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(3);
		vertexBuffer.put(new float[] {0f,0f,0f});
		vertexBuffer.flip();
		
		VBO_ID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO_ID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	public float getBulletsStrength() {
		return bulletsStrength;
	}

	public void setBulletsStrength(float bulletsStrength) {
		this.bulletsStrength = bulletsStrength;
	}

	public float getBulletsDamage() {
		return bulletsDamage;
	}

	public void setBulletsDamage(float bulletsDamage) {
		this.bulletsDamage = bulletsDamage;
	}

	public int getBulletsAmount() {
		return bulletsAmount;
	}

	public void setBulletsAmount(int bulletsAmount) {
		this.bulletsAmount = bulletsAmount;
	}

	public GunType getType() {
		return type;
	}

	public float getRot() {
		return rot;
	}

	public void setRot(float rot) {
		this.rot = rot;
	}
	
	public void setReverted(boolean revert) {
		this.revert = revert;
	}
	
	public boolean isReverted() {
		return this.revert;
	}
	
	@Override
	public void update() {
		Vector2f mousePos = new Vector2f((((float)MouseHandler.getPosX()/Constants.WIDTH)-1/2f-pos.x),
										 (((float)MouseHandler.getPosY()/Constants.HEIGHT)-1/2f+pos.y)).normalize();
		float angle = (float) Math.atan2(pos.x - mousePos.x, pos.y - mousePos.y);
	    if(angle < 0){
	        angle += 2*Math.PI;
	    }
	    this.rot = angle+(float)Math.PI/2f;
	    this.modelMatrix.identity()
			.translate(pos.x*Constants.WIDTH, pos.y*Constants.HEIGHT, 0f)
			.rotateZ(rot);
		shader.bind();
		shader.setUniformMat4f("modelMatrix", modelMatrix);
	    shader.setUniformf("width", getGunWidth()*Constants.WIDTH);
	    shader.setUniformf("height", getGunHeight()*Constants.HEIGHT);
	    shader.setUniformi("revert", revert ? 1 : 0);
		shader.unbind();
		
		try {
			launchedBullets.forEach(b -> b.update());
		}catch(ConcurrentModificationException exception) {}
	}
	
	public void render() {
		//enable blending
		GL11.glEnable(GL11.GL_BLEND);
		//defines blend functions
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		gunTexture.bind(0);
		//bind the shader
		shader.bind();
		//get the vertex attrib location ID from the shader
		int vertexAttribLocation = GL20.glGetAttribLocation(shader.program, "vertices");
		//bind the vertex attrib location for the shader
		GL20.glEnableVertexAttribArray(vertexAttribLocation);
		//bind the buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO_ID);
		//push buffer's data into the vertex attrib location in the shader
		GL20.glVertexAttribPointer(vertexAttribLocation, 3, GL11.GL_FLOAT, false, 0, 0);
		//draw shapes
		GL11.glDrawArrays(GL11.GL_POINTS, 0, 1);
		//unbind vertex attrib location for the shader
		GL20.glDisableVertexAttribArray(vertexAttribLocation);
		//unbind buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		//unbind the shader
		shader.unbind();
		
		//disable blending
		GL11.glDisable(GL11.GL_BLEND);
		
		try {
			launchedBullets.forEach(b -> b.render());
		}catch(ConcurrentModificationException exception) {}
	}
	
	@Override
	public void destroy() {
		GL15.glDeleteBuffers(VBO_ID);
		this.gunTexture.delete();
		this.shader.destroy();
	}

	public float getGunWidth() {
		return 0.05859f;
	}

	public float getGunHeight() {
		return 0.06806f;
	}
	
	/**
	 * Launch a bullet from the gun
	 */
	public void fire() {
		//create a bullet
		Bullet b = new Bullet(this, type);
		//add the bullet to the launched bullets list
		launchedBullets.add(b);
		//run a thread
		Thread t = new Thread(new Runnable() {
			//means "should the bullet move again"
			boolean shouldrun = true;
			//movement vector to apply to the bullet
			Vector2f move = new Vector2f((float)Math.cos(rot), (float)Math.sin(rot)).mul(type.getBulletsStrength());
			@Override
			public void run() {
				//while we can move the bullet
				while(shouldrun) {
					//check for obstacles
					for(MapObstacle obstacle : FurryAttack.getInstance().getMapManager().getMap().getObstacles()) {
						shouldrun = !obstacle.isInside(b.pos.x*Constants.WIDTH, b.pos.y*Constants.HEIGHT);
						if(!shouldrun) break;
					}
					//move the bullets
					b.pos.add(move);
					//if the bullet should move again, check for screen border
					if(shouldrun) {
						shouldrun = !(b.pos.x > 0.5f || b.pos.x < -0.5f ||
							  b.pos.y > 0.5f || b.pos.y < -0.5f);
					}
					//if the bullet should move again, check if we've touched an entity
					if(shouldrun)shouldrun = !b.checkEntityIsTouched();
					//doing a sleep job to slow down CPU resources needed
					try {
						Thread.sleep(1L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				//delete the bullet from launched bullet (and let's the garbage collector delete this bullet)
				launchedBullets.remove(b);
			}
		});
		t.start();
	}
}
