package fr.axicer.furryattack.entity.gun.bullet;

public enum BulletType {
	BASIC(0.01f, 0.005f);
	
	private float width, height;
	
	private BulletType(float w, float h) {
		this.width = w;
		this.height = h;
	}
	
	public float getBulletsWidth() {
		return this.width;
	}
	public float getBulletsHeight() {
		return this.height;
	}
}
