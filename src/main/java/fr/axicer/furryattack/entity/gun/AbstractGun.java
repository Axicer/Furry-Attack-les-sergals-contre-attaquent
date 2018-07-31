package fr.axicer.furryattack.entity.gun;

import org.joml.Vector2f;

import fr.axicer.furryattack.render.Destroyable;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;

/**
 * A simple gun 
 * @author Axicer
 *
 */
public abstract class AbstractGun implements Updateable,Renderable,Destroyable{

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
	
	public Vector2f getPos() {
		return pos;
	}

	public AbstractGun(GunType type, int bulletAmount) {
		this.type = type;
		this.bulletsDamage = type.getBulletsDamage();
		this.bulletsStrength = type.getBulletsStrength();
		this.bulletsAmount = bulletAmount;
		this.rot = 0f;
		this.pos = new Vector2f();
	}

	public abstract float getGunWidth();
	public abstract float getGunHeight();
	
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
}
