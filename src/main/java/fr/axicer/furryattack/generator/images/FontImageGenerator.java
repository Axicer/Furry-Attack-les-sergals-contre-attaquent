package fr.axicer.furryattack.generator.images;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Map;

import fr.axicer.furryattack.util.font.CharInfo;

public class FontImageGenerator extends ImageGenerator{

	private String representedString;
	private int width, height;
	private Color color;
	private Map<Integer, CharInfo> charMap;
	private Font f;
	
	public FontImageGenerator(String allChars, int width, int height, Color color, Map<Integer, CharInfo> charMap, Font f) {
		this.representedString = allChars;
		this.width = width;
		this.height = height;
		this.color = color;
		this.charMap = charMap;
		this.f = f;
	}
	
	@Override
	public BufferedImage generate() {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2D = img.createGraphics();
	    g2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2D.setFont(f);
	    FontMetrics fontMetrics = g2D.getFontMetrics();
	    g2D.setColor(color);
	    for(char c : representedString.toCharArray()) {
	    	g2D.drawString(String.valueOf(c), charMap.get((int)c).startX, fontMetrics.getAscent());
	    }
	    g2D.dispose();
		return img;
	}

}
