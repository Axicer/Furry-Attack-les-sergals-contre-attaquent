package fr.axicer.furryattack.entity;

import org.joml.Vector2f;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.map.Map;
import fr.axicer.furryattack.map.Tile;
import fr.axicer.furryattack.map.TileContainer;
import fr.axicer.furryattack.render.Destroyable;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;

public abstract class Entity implements Renderable, Updateable, Destroyable{
	
	//entity's race
	protected Species race;
	//position of the entity
	protected Vector2f pos;
	//entity's acceleration
	protected Vector2f acc;
	//default gliding movement coeff (higher is more gliding)
	private float glidingCoeff = 0.7f;
	//default air braking movement coeff (higher is less movement)
	private float airBrakingCoeff = 0.7f;
	//whether an entity is on ground or not
	protected boolean onGround;
	//is the entity reverted (look on the left)
	protected boolean revert;
	// if an entity is shifted
	protected boolean shifted;
	//gravity factor applied on position
	private float gravityFactor;
	
	//amount of step for each movement
	public static float STEP = 100.0f;
	
	/**
	 * Empty entity constructor
	 */
	public Entity(Species race) {
		this.race = race;
		this.shifted = false;
		this.pos = new Vector2f();
		this.acc = new Vector2f();
		this.revert = false;
		this.onGround = false;
	}
	
	/**
	 * Move the entity by acc vector
	 * If a collision is detected then the entity is stopped at the last available movement on the corresponding axis
	 */
	public void move() {
		//determine on ground state
		determineIsOnGround();
		
		//apply gravity depending on grounded status
		applyGravityToAccelerationVector();
		
		//if movement on x is not null
		if(acc.x != 0) {
			//if positive acc
			Tile t = null;
			if(acc.x > 0) {
				//move step by step
				for(float x = 0 ; x < acc.x ; x+= acc.x/STEP) {
					t = FurryAttack.getInstance().getMap().getTileContainer().getTile((int)((pos.x+x)/TileContainer.TILE_SIZE), (int)(pos.y/TileContainer.TILE_SIZE));
					
					//check for null or solid
					if(t == null)break;
					if(t.isSolid())break;
					
					//else increment
					pos.x += x;					
				}
			}else {
				//move step by step
				for(float x = 0 ; x > acc.x ; x += acc.x/STEP) {
					t = FurryAttack.getInstance().getMap().getTileContainer().getTile((int)((pos.x+x)/TileContainer.TILE_SIZE), (int)(pos.y/TileContainer.TILE_SIZE));
					
					//check for null or solid
					if(t == null)break;
					if(t.isSolid())break;
					
					//else increment
					pos.x += x;
				}
			}
		}
		//if acceleration on y axis is not null
		if(acc.y != 0) {
			Tile t = null;
			//if positive acc
			if(acc.y > 0) {
				//move step by step
				for(float y = 0 ; y < acc.y ; y+= acc.y/STEP) {
					t = FurryAttack.getInstance().getMap().getTileContainer().getTile((int)(pos.x/TileContainer.TILE_SIZE), (int)((pos.y+y)/TileContainer.TILE_SIZE));
					
					//check for null or solid
					if(t == null)break;
					if(t.isSolid())break;
					
					//else increment
					pos.y+= y;					
				}
			}else {
				//move step by step
				for(float y = 0 ; y > acc.y ; y += acc.y/STEP) {
					t = FurryAttack.getInstance().getMap().getTileContainer().getTile((int)(pos.x/TileContainer.TILE_SIZE), (int)((pos.y+y)/TileContainer.TILE_SIZE));
					
					//check for null or solid
					if(t == null) break;
					if(t.isSolid()) break;
					
					//else increment
					pos.y+= y;
				}
			}
		}
		
		//if acceleration is less then 1 pixels cancel
		if(Math.abs(acc.x) < 1f)acc.x = 0f;
		if(Math.abs(acc.y) < 1f)acc.y = 0f;
		
		//aply coef gliding coeff and air braking coeff
		acc.set(acc.x*glidingCoeff*airBrakingCoeff, acc.y*airBrakingCoeff);
		
	}
	
	/**
	 * Apply gravity on Y axis (depending on the actual map)
	 */
	private void applyGravityToAccelerationVector() {
		//TODO fix gravity 
		//if we're not on ground
		if(!onGround) {
			if(acc.y == 0f) {
				gravityFactor = 0.01f;
			}
			gravityFactor *= Map.getDefaultGravity();
			//subtract gravity from Y acceleration
			acc.sub(0f, gravityFactor);
		} else {
			gravityFactor = 0f;
		}
	}
	
	private void determineIsOnGround() {
		//get tile above entity
		Tile t = FurryAttack.getInstance().getMap().getTileContainer().getTile((int)(pos.x/TileContainer.TILE_SIZE), (int)((pos.y-0.05f)/TileContainer.TILE_SIZE));
		if(t == null || t.isSolid()){
			onGround = true;
			return;
		}
		onGround = false; 
	}

	/**
	 * Default entity render method which only shows the collision box
	 */
	public void render() {
		//TODO
	}
	
	public void update() {
		move();
	}
	
	public void destroy() {
		//TODO
	}
	
	public boolean isOnGround() {
		return onGround;
	}

	public Vector2f getAccelerationVector() {
		return this.acc;
	}
	
	public void setAccelerationVector(Vector2f acc) {
		this.acc = acc;
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
	
	/**
	 * Set the air Braking coefficient to apply to the acceleration vector
	 * @param airBrakingCoeff float gliding coefficient
	 */
	public void setAirBrakingCoeff(float airBrakingCoeff) {
		this.airBrakingCoeff = airBrakingCoeff;
	}
	
	/**
	 * Returns the actual air Braking coefficient applied to the acceleration vector
	 * @return float air Braking coefficient
	 */
	public float getairBrakingCoeff() {
		return this.airBrakingCoeff;
	}
	
	public Vector2f getPosition() {
		return this.pos;
	}
	
	public void setGrounded(boolean value) {
		this.onGround = value;
	}
	
	public void setShifted(boolean value) {
		this.shifted = value;
	}
	
	public boolean isReverted() {
		return revert;
	}
	
}
