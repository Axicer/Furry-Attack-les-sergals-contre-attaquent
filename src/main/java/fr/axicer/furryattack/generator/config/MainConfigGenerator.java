package fr.axicer.furryattack.generator.config;

import java.io.File;

import fr.axicer.furryattack.generator.AbstractGenerator;
import fr.axicer.furryattack.util.Util;
import fr.axicer.furryattack.util.config.Configuration;

public class MainConfigGenerator extends AbstractGenerator{

	public static final String FULLSCREEN_PATH = "fullscreen";
	public static final String VSYNC_PATH = "vsync";
	public static final String WIDTH_PATH = "width";
	public static final String HEIGHT_PATH = "height";
	public static final String SCREENID_PATH = "screenid";
	public static final String LANG_PATH = "lang";
	
	public static Configuration generate(File f, String[] args) {
		Configuration config = new Configuration();
		config.setBoolean(FULLSCREEN_PATH, Util.contains(args, "-fullscreen"), true);
		config.setBoolean(VSYNC_PATH, Util.contains(args, "-vsync"), true);
		config.setInt(WIDTH_PATH, Integer.parseInt(System.getProperty("width", "800")), true);
		config.setInt(HEIGHT_PATH, Integer.parseInt(System.getProperty("height", "600")), true);
		config.setInt(SCREENID_PATH, Integer.valueOf(System.getProperty("fullscreenid", "0")), true);
		config.setString(LANG_PATH, "en_US", true);
		config.save(f);
		return config;
	}

}
