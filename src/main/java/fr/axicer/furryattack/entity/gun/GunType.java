package fr.axicer.furryattack.entity.gun;

import fr.axicer.furryattack.entity.Species;

public enum GunType {
	BERETTA(15, 3.0f, 5.0f);
	
	private int chargerBulletAmount;
	private float bulletsDamage, bulletsStrength;
	private String imgPath;
	
	private GunType(int chargerBulletAmount, float bulletsDamage, float bulletsStrength) {
		this.chargerBulletAmount = chargerBulletAmount;
		this.bulletsDamage = bulletsDamage;
		this.bulletsStrength = bulletsStrength;
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
	
	public String getImgPath(Species race) {
		return this.imgPath+"_"+race.toString().toLowerCase()+".png";
	}
}
