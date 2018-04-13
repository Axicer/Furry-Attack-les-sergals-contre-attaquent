package fr.axicer.furryattack.generator.config;

import java.awt.FontMetrics;

import fr.axicer.furryattack.util.config.Configuration;
import fr.axicer.furryattack.util.font.CharInfo;

public class FontConfigGenerator extends ConfigGenerator{

	private String representedString;
	private FontMetrics metrics;
	
	public FontConfigGenerator(String allChars, FontMetrics fontMetrics) {
		this.representedString = allChars;
		this.metrics = fontMetrics;
	}
	
	@Override
	public Configuration generate() {
		Configuration config = new Configuration();
		int width = 0, height = 0;
		for (char c : representedString.toCharArray()) {
	        // Get the size for each character and update global image size
	        CharInfo charInfo = new CharInfo(width, metrics.charWidth(c));
	        int charval = (int)c;
	        //System.out.println(c+"("+charval+") > "+charInfo.width+" - "+charInfo.startX);
	        config.setInt("chars."+charval+".startX", charInfo.startX, true);
	        config.setInt("chars."+charval+".width", charInfo.width, true);
	        width += charInfo.width;
	        height = Math.max(height, metrics.getHeight());
	    }
		config.setInt("global.width", width, true);
	    config.setInt("global.height", height, true);
		return config;
	}

}
