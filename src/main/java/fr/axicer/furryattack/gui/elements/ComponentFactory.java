package fr.axicer.furryattack.gui.elements;

import java.io.File;

import org.joml.Vector2f;
import org.joml.Vector3f;

import fr.axicer.furryattack.gui.guis.GUI;
import fr.axicer.furryattack.util.Color;
import fr.axicer.furryattack.util.config.Configuration;
import fr.axicer.furryattack.util.font.FontType;

/**
 * Main component factory to simplify components creation
 * @author Axicer
 *
 */
public class ComponentFactory {
	
	/**
	 * default base button texture path
	 */
	public static final String DEFAULT_BUTTON_IMG = "/img/gui/button/button.png";
	/**
	 * default hovered button texture path
	 */
	public static final String DEFAULT_BUTTON_HOVER_IMG = "/img/gui/button/button_hover.png";
	/**
	 * default clicked button texture path
	 */
	public static final String DEFAULT_BUTTON_CLICK_IMG = "/img/gui/button/button_click.png";
	/**
	 * default font used
	 */
	public static final FontType DEFAULT_FONT = FontType.CAPTAIN;
	/**
	 * default color used
	 */
	public static final Color DEFAULT_COLOR = Color.WHITE;
	
	/**
	 * Create a {@link GUIButton}
	 * @param gui {@link GUI} gui
	 * @param text {@link String} text of the button
	 * @param textScale float text scale
	 * @param width int width
	 * @param height int height
	 * @param scale float button's scale
	 * @param pos {@link Vector3f} position relative to alignements
	 * @param rot float rot
	 * @param alignement {@link GUIAlignement} component alignement
	 * @param guialignement {@link GUIAlignement} gui alignement
	 * @param action {@link Runnable} action to do
	 * @return a new {@link GUIButton} instance
	 */
	public static GUIButton generateButton(GUI gui, String text, float textScale, float width, float height, float scale, Vector3f pos, float rot, GUIAlignement alignement, GUIAlignement guialignement, Runnable action){
		return new GUIButton(gui, text, textScale, width, height, DEFAULT_BUTTON_IMG, DEFAULT_BUTTON_HOVER_IMG, DEFAULT_BUTTON_CLICK_IMG, scale, DEFAULT_FONT, DEFAULT_COLOR, pos, rot, alignement, guialignement, action);
	}
	
	/**
	 * Create a new {@link GUIInputButton}
	 * @param gui {@link GUI} parent
	 * @param config {@link Configuration} to modify
	 * @param configFile {@link File} configuration file
	 * @param path {@link String} path of the value
	 * @param textScale float text scale
	 * @param width int width
	 * @param height int height
	 * @param scale float button's scale
	 * @param pos {@link Vector3f} position relative to alignements
	 * @param rot float rotation
	 * @param alignement {@link GUIAlignement} component alignement
	 * @param guialignement {@link GUIAlignement} gui alignement
	 * @return a new {@link GUIInputButton} instance
	 */
	public static GUIInputButton generateInputButton(GUI gui, Configuration config, File configFile, String path, float textScale, float width, float height, float scale, Vector3f pos, float rot, GUIAlignement alignement, GUIAlignement guialignement) {
		return new GUIInputButton(gui, config, configFile, path, textScale, DEFAULT_FONT, width, height, scale, DEFAULT_COLOR, pos, rot, DEFAULT_BUTTON_IMG, DEFAULT_BUTTON_HOVER_IMG, DEFAULT_BUTTON_CLICK_IMG, alignement, guialignement);
	}
	
	/**
	 * Create a new {@link GUIImage}
	 * @param imgPath {@link String} path of the image
	 * @param width int width
	 * @param height int height
	 * @param pos {@link Vector3f} position relative to alignements
	 * @param alignement {@link GUIAlignement} component alignement
	 * @param guialignement {@link GUIAlignement} gui alignement
	 * @return a new {@link GUIImage} instance
	 */
	public static GUIImage generateImage(String imgPath, float width, float height, Vector3f pos, GUIAlignement alignement, GUIAlignement guialignement) {
		return new GUIImage(imgPath, false, new Vector2f(), width, height, pos, 0f, 1f, alignement, guialignement);
	}
}
