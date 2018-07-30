package fr.axicer.furryattack.entity.animation;

public enum AnimationsType {
	
	STAY("_stay"),
	WALK("_walk"),
	JUMP("_jump"),
	SHIFT("_shift");
	
	private String pathName;
	
	private AnimationsType(String pathName) {
		this.pathName = pathName;
	}
	
	public String getPathName() {
		return this.pathName;
	}
}
