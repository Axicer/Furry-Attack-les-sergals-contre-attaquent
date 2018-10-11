package fr.axicer.furryattack.util;

import java.io.File;
import java.io.FileNotFoundException;

import fr.axicer.furryattack.generator.config.MainConfigGenerator;
import fr.axicer.furryattack.util.config.Configuration;
import fr.axicer.furryattack.util.config.FileManager;

/**
 * TODO rename this Environment
 * @author Axicer
 *
 */
public class Constants {
	
	public static int WIDTH;
	public static int HEIGHT;
	public static boolean FULLSCREEN;
	public static boolean V_SYNC;
	public static Configuration MAIN_CONFIG;
	public static File MAIN_CONFIG_FILE;
	
	public static double FPS = 9999999.0;
	public static String TITLE = "Furry-Attack : Les sergals contre-attaquent !";
	public static final String ENCODING="ISO-8859-1";
	
	public static void initialize(String[] args) {
		MAIN_CONFIG_FILE = new File(FileManager.CONFIG_FOLDER_FILE, "main.json");
		if(MAIN_CONFIG_FILE.exists()) {
			try {
				MAIN_CONFIG = new Configuration(MAIN_CONFIG_FILE);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}else {
			MAIN_CONFIG = MainConfigGenerator.generate(MAIN_CONFIG_FILE, args);
		}
		Constants.FULLSCREEN = MAIN_CONFIG.getBoolean(MainConfigGenerator.FULLSCREEN_PATH,false);
		Constants.V_SYNC = MAIN_CONFIG.getBoolean(MainConfigGenerator.VSYNC_PATH,false);
		Constants.WIDTH = MAIN_CONFIG.getInt(MainConfigGenerator.WIDTH_PATH, 800);
		Constants.HEIGHT = MAIN_CONFIG.getInt(MainConfigGenerator.HEIGHT_PATH, 600);
	}
	
}
