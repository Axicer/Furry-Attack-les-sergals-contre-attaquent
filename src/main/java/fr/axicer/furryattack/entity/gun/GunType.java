package fr.axicer.furryattack.entity.gun;

import fr.axicer.furryattack.entity.Species;
import fr.axicer.furryattack.entity.gun.bullet.BulletType;

public enum GunType {
	BERETTA(BulletType.BASIC, 15, 3.0f, 0.005f);
	
	private int chargerBulletAmount;
	private float bulletsDamage, bulletsStrength;
	private String imgPath;
	private BulletType bulletType;
	
	private GunType(BulletType bulletType, int chargerBulletAmount, float bulletsDamage, float bulletsStrength) {
		this.chargerBulletAmount = chargerBulletAmount;
		this.bulletsDamage = bulletsDamage;
		this.bulletsStrength = bulletsStrength;
		this.bulletType = bulletType;
		this.imgPath = "/img/gun/"+toString().toLowerCase(); 
	}
	
	public int getChargerBulletsAmount() {
		return this.chargerBulletAmount;
	}
	
	public float getBulletsDamage() {
		return this.bulletsDamage;
	}
	
	public float getBulletsStrength() {
		return this.bulletsStrength;
	}
	
	public BulletType getBulletType() {
		return this.bulletType;
	}
	
	public String getImgPath(Species race) {
		return this.imgPath+"_"+race.toString().toLowerCase()+".png";
	}
}
