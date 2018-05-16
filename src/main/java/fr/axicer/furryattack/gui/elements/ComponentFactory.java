package fr.axicer.furryattack.gui.elements;

import java.io.File;

import org.joml.Vector2f;
import org.joml.Vector3f;

import fr.axicer.furryattack.gui.guis.GUI;
import fr.axicer.furryattack.util.Color;
import fr.axicer.furryattack.util.config.Configuration;
import fr.axicer.furryattack.util.font.FontType;

public class ComponentFactory {
	
	public static final String DEFAULT_BUTTON_IMG = "/img/gui/button/button.png";
	public static final String DEFAULT_BUTTON_HOVER_IMG = "/img/gui/button/button_hover.png";
	public static final String DEFAULT_BUTTON_CLICK_IMG = "/img/gui/button/button_click.png";
	
	public static final FontType DEFAULT_FONT = FontType.CAPTAIN;
	public static final Color DEFAULT_COLOR = Color.WHITE;
	
	public static GUIButton generateButton(GUI gui, String text, float textScale, int width, int height, float scale, Vector3f pos, float rot, GUIAlignement alignement, GUIAlignement guialignement, Runnable action){
		return new GUIButton(gui, text, textScale, width, height, DEFAULT_BUTTON_IMG, DEFAULT_BUTTON_HOVER_IMG, DEFAULT_BUTTON_CLICK_IMG, scale, DEFAULT_FONT, DEFAULT_COLOR, pos, rot, alignement, guialignement, action);
	}
	public static GUIInputButton generateInputButton(GUI gui, Configuration config, File configFile, String path, float textScale, int width, int height, float scale, Vector3f pos, float rot, GUIAlignement alignement, GUIAlignement guialignement) {
		return new GUIInputButton(gui, config, configFile, path, textScale, DEFAULT_FONT, width, height, scale, DEFAULT_COLOR, pos, rot, DEFAULT_BUTTON_IMG, DEFAULT_BUTTON_HOVER_IMG, DEFAULT_BUTTON_CLICK_IMG, alignement, guialignement);
	}
	public static GUIImage generateImage(String imgPath, int width, int height, Vector3f pos, GUIAlignement alignement, GUIAlignement guialignement) {
		return new GUIImage(imgPath, false, new Vector2f(), width, height, pos, 0f, 1f, alignement, guialignement);
	}
}
