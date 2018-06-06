package fr.axicer.furryattack.map;

import org.lwjgl.opengl.GL11;

import fr.axicer.furryattack.render.textures.Texture;

public enum MapObstaclesTextures {
	BRICK("/img/map/brick.jpg");
	
	private Texture tex;
	
	private MapObstaclesTextures(String path) {
		this.tex = Texture.loadTexture(path, GL11.GL_REPEAT, GL11.GL_LINEAR);
	}
	
	public Texture getTexture() {
		return tex;
	}
	
	
	public static MapObstaclesTextures fromString(String input) {
		for(MapObstaclesTextures tex : values()) {
			if(tex.name().equalsIgnoreCase(input))return tex;
		}
		return MapObstaclesTextures.BRICK;
	}
}
