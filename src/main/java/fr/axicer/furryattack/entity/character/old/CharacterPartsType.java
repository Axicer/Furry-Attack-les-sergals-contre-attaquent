package fr.axicer.furryattack.entity.character.old;

public enum CharacterPartsType {
	CHEST(0.015625f, 0.03473f),
	LEG(0.0078125f, 0.028f),
	ARM(0.0078125f, 0.03473f),
	HEAD(0.0234375f, 0.03473f);
	
	private float width,height;
	
	private CharacterPartsType(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	public float getWidth() {
		return this.width;
	}
	public float getHeight() {
		return this.height;
	}
}
