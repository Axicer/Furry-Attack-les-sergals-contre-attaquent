package fr.axicer.furryattack.util.config;

import java.io.File;
import java.util.Locale;

public class FileManager {

	private static OSType os;
	public static final String GAME_FOLDER_NAME = ".FurryAttack";
	public static String GAME_FOLDER;
	public static File GAME_FOLDER_FILE;
	public static final String CONFIG_FOLDER_NAME = "config";
	public static String CONFIG_FOLDER;
	public static File CONFIG_FOLDER_FILE;
	public static final String ANIMATION_FOLDER_NAME = "anim";
	public static String ANIMATION_FOLDER;
	public static File ANIMATION_FOLDER_FILE;
	public static final String MAPS_FOLDER_NAME = "maps";
	public static String MAPS_FOLDER;
	public static File MAPS_FOLDER_FILE;
	public static final String IMAGE_FOLDER_NAME = "img";
	public static String IMAGE_FOLDER;
	public static File IMAGE_FOLDER_FILE;
	
	public static void initialize() {
		String OSname = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
		if ((OSname.indexOf("mac") >= 0) || (OSname.indexOf("darwin") >= 0)) {
			os = OSType.MAC;
		} else if (OSname.indexOf("win") >= 0) {
			os = OSType.WINDOWS;
		} else if (OSname.indexOf("nux") >= 0) {
			os = OSType.LINUX;
		} else {
			os = OSType.OTHER;
		}

		switch(os) {
		case WINDOWS:
			GAME_FOLDER = System.getenv("APPDATA")+File.separator+GAME_FOLDER_NAME+File.separator;
			break;
		case MAC:
			GAME_FOLDER = System.getProperty("user.home") +File.separator+ "Library"+File.separator+"Application Support"+File.separator+GAME_FOLDER_NAME+File.separator;
			break;
		case LINUX:
			GAME_FOLDER = System.getProperty("user.home")+File.separator+GAME_FOLDER_NAME+File.separator;
			break;
		case OTHER:
			GAME_FOLDER = System.getProperty("user.home")+File.separator+GAME_FOLDER_NAME+File.separator;
			break;
		}
		
		CONFIG_FOLDER = GAME_FOLDER+CONFIG_FOLDER_NAME+File.separator;
		ANIMATION_FOLDER = GAME_FOLDER+ANIMATION_FOLDER_NAME+File.separator;
		MAPS_FOLDER = GAME_FOLDER+MAPS_FOLDER_NAME+File.separator;
		IMAGE_FOLDER = GAME_FOLDER+IMAGE_FOLDER_NAME+File.separator;

		GAME_FOLDER_FILE = new File(GAME_FOLDER);
		GAME_FOLDER_FILE.mkdirs();
		CONFIG_FOLDER_FILE = new File(CONFIG_FOLDER);
		CONFIG_FOLDER_FILE.mkdirs();
		ANIMATION_FOLDER_FILE = new File(ANIMATION_FOLDER);
		ANIMATION_FOLDER_FILE.mkdirs();
		MAPS_FOLDER_FILE = new File(MAPS_FOLDER);
		MAPS_FOLDER_FILE.mkdirs();
		IMAGE_FOLDER_FILE = new File(IMAGE_FOLDER);
		IMAGE_FOLDER_FILE.mkdirs();
	}

	public static OSType getOS() {
		return os;
	}
	
	
}
