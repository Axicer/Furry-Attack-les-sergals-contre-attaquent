package fr.axicer.furryattack.gui.render;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import fr.axicer.furryattack.gui.guis.CharacterCustomisationMenuGUI;
import fr.axicer.furryattack.gui.guis.ControlGUI;
import fr.axicer.furryattack.gui.guis.GUI;
import fr.axicer.furryattack.gui.guis.MenuGUI;
import fr.axicer.furryattack.gui.guis.OptionsMenu;
import fr.axicer.furryattack.gui.guis.VideoOptionsGUI;

/**
 * Enum of all available GUIs
 * @author Axicer
 *
 */
public enum GUIs {
	/**
	 * The main menu
	 */
	MAIN_MENU(MenuGUI.class),
	/**
	 * The character customization menu
	 */
	CHARACTER_CHOICE_MENU(CharacterCustomisationMenuGUI.class),
	/**
	 * The main options menu
	 */
	OPTION_MENU(OptionsMenu.class),
	/**
	 * The control menu
	 */
	CONTROL_MENU(ControlGUI.class),
	/**
	 * The video options menu
	 */
	VIDEO_OPTIONS(VideoOptionsGUI.class);
	
	private Class<? extends GUI> clazz;
	private GUI gui;
	
	private GUIs(Class<? extends GUI> clazz) {
		this.clazz = clazz;
		this.gui = getNewInstance();
	}

	/**
	 * Create a new instance of the menu
	 * @return the {@link GUI} instance created
	 */
	private GUI getNewInstance() {
		try {
			Constructor<? extends GUI> constructor = clazz.getConstructor();
			return constructor.newInstance();
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public GUI getGUI() {
		return this.gui;
	}
	
	public static void recreate() {
		for(GUIs elem : GUIs.values()) {
			elem.gui.destroy();
			elem.gui = elem.getNewInstance();
		}
	}
}
