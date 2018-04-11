package fr.axicer.furryattack.util;

import java.io.File;
import java.io.FileNotFoundException;

import fr.axicer.furryattack.util.config.Configuration;
import fr.axicer.furryattack.util.config.FileManager;

public class Constants {
	
	public static int WIDTH;
	public static int HEIGHT;
	public static boolean FULLSCREEN;
	public static boolean V_SYNC;
	public static Configuration MAIN_CONFIG;
	
	public static double FPS = 9999999.0;
	public static String TITLE = "Furry-Attack : Les sergals contre-attaquent !";
	public static final String ENCODING="ISO-8859-1";
	
	public static void initialize(String[] args) {
		File maincfg = new File(FileManager.CONFIG_FOLDER_FILE, "main.json");
		if(maincfg.exists()) {
			try {
				MAIN_CONFIG = new Configuration(maincfg);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}else {
			MAIN_CONFIG = new Configuration();
			MAIN_CONFIG.setBoolean("fullscreen", Util.contains(args, "-fullscreen"), true);
			MAIN_CONFIG.setBoolean("vsync", Util.contains(args, "-vsync"), true);
			MAIN_CONFIG.setInt("width", Integer.parseInt(System.getProperty("width", "800")), true);
			MAIN_CONFIG.setInt("height", Integer.parseInt(System.getProperty("height", "600")), true);
			MAIN_CONFIG.setInt("screenid", Integer.valueOf(System.getProperty("fullscreenid", "0")), true);
			MAIN_CONFIG.save(new File(FileManager.CONFIG_FOLDER_FILE, "main.json"));
		}
		Constants.FULLSCREEN = MAIN_CONFIG.getBoolean("fullscreen",false);
		Constants.V_SYNC = MAIN_CONFIG.getBoolean("vsync",false);
		Constants.WIDTH = MAIN_CONFIG.getInt("width", 800);
		Constants.HEIGHT = MAIN_CONFIG.getInt("height", 600);
	}
	
}
