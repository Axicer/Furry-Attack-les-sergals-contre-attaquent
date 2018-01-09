package fr.axicer.furryattack.character.animation;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import fr.axicer.furryattack.render.textures.Texture;

public class CharacterAnimation {
	
	private Texture texture;
	private KeyFrame[] keyframes;
	private long start;
	private long actual;
	private long end;
	
	public CharacterAnimation(String AnimPath, String texturePath) {
		this.keyframes= loadFramesFromFile(AnimPath);
		this.start = 0;
		this.actual = 0;
		this.end = keyframes[keyframes.length-1].millis; //end is at the last keyframe millis
		
		BufferedImage bimg;
		try {
			bimg = ImageIO.read(CharacterAnimation.class.getResourceAsStream(texturePath));
			this.texture = Texture.loadTexture(bimg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Texture getTexture() {
		return this.texture;
	}
	
	public void updateState() {
		actual++;
		if(actual == end)reset();
	}
	
	public void reset() {
		actual = start;
	}
	
	public KeyFrame getBeforeKeyFrame() {
		long time = actual;
		KeyFrame kf = null;
		while(kf == null && time > 0) {
			kf = get(time);
			time--;
		}
		if(kf == null)return keyframes[0];
		return kf;
	}
	
	private KeyFrame get(long millis) {
		KeyFrame kf = null;
		for(int i = 0 ; i < keyframes.length ; i++) {
			if(keyframes[i].millis == millis) {
				kf = keyframes[i];
				break;
			}
		}
		return kf;
	}
	
	public KeyFrame[] getKeyframes() {
		return keyframes;
	}

	private KeyFrame[] loadFramesFromFile(String path) {
		List<KeyFrame> list = new ArrayList<KeyFrame>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(CharacterAnimation.class.getResourceAsStream(path), "UTF-8"));
		    String line = br.readLine();
		    while (line != null) {
		        String[] parts = line.split(",");
		        long t = Long.parseLong(parts[0]);
		        int diffX = Integer.parseInt(parts[1]);
		        KeyFrame kf = new KeyFrame(t, diffX);
		        list.add(kf);
		        line = br.readLine();
		    }
		    br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		KeyFrame[] kfs = new KeyFrame[list.size()];
		return list.toArray(kfs);
	}
}
