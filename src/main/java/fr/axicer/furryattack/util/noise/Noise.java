package fr.axicer.furryattack.util.noise;

import java.util.Random;

public class Noise {
	
	private long seed;
	private int octave;
	private float amplitude;
	
	private Random rand;
	
	public Noise(long seed, int octave, float amplitude) {
		this.seed = seed;
		this.octave = octave;
		this.amplitude = amplitude;
		
		rand = new Random();
	}
	
	public float getNoise(int x, int y) {
		int x0 = (int) ((float) x / octave);
		int x1 = x0 + 1;
		int y0 = (int) ((float) y / octave);
		int y1 = y0 + 1;
		
		float a = noise(x0, y0);
		float b = noise(x1, y0);
		float c = noise(x0, y1);
		float d = noise(x1, y1);
		
		float h = interpolate(a, b, c, d,
				(float) (x - x0 * octave) / octave, 
				(float) (y - y0 * octave) / octave);
		
		return h * amplitude;
	}
	
	private float noise(int x, int y) {
		float nseed = 10000 * (cos(x) + sin(y) + tan(seed));
		rand.setSeed((long) nseed);
		return rand.nextFloat();
	}
	
	private float lerp(float s, float e, float t) {
		float t2 = (1 - cos(t * (float)Math.PI)) / 2;
		return (s * (1 - t2) + e * t2);
	}
	
	private float interpolate(float x00, float x10, float x01, float x11, float tx, float ty) {
		return lerp(lerp(x00, x10, tx), lerp(x01, x11, tx), ty);
	}
	
	private float cos(float v) { return (float) Math.cos(v); }
	private float sin(float v) { return (float) Math.sin(v); }
	private float tan(float v) { return (float) Math.tan(v); }
}