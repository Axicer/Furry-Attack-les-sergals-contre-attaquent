package fr.axicer.furryattack.gui.guis;

import org.joml.Vector3f;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.gui.elements.ComponentFactory;
import fr.axicer.furryattack.gui.elements.GUIAlignement;
import fr.axicer.furryattack.gui.elements.GUIComponent;
import fr.axicer.furryattack.gui.elements.GUIText;
import fr.axicer.furryattack.gui.render.GUIs;
import fr.axicer.furryattack.util.Color;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.font.FontType;

public class OptionsMenu extends GUI{

	public OptionsMenu() {
		super("options");
		init();
	}
	
	private void init() {
		float ratio = (float)Constants.WIDTH/(float)Constants.HEIGHT;
		components.add(ComponentFactory.generateImage("/img/gui/background/menu-bg.png", //imgPath
				1, //width
				1, //height
				new Vector3f(0,0,-2f), //pos
				GUIAlignement.CENTER,
				GUIAlignement.CENTER));
		components.add(new GUIText(FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.options.title"), //text
				new Vector3f(0f, -0.1f, -1f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				new Color(50, 70, 120, 255), // color
				ratio*0.5f, //scale
				GUIAlignement.TOP,
				GUIAlignement.TOP));
		components.add(ComponentFactory.generateButton(this,
				FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.options.back"),
				ratio*0.2f,
				0.125f,
				0.084f,
				ratio*0.5f,
				new Vector3f(0.05f, 0.05f, -1f),
				0f,
				GUIAlignement.BOTTOM_LEFT,
				GUIAlignement.BOTTOM_LEFT,
				new Runnable() {
					public void run() {
						FurryAttack.getInstance().getRenderer().getGUIRenderer().setCurrentGUI(GUIs.MAIN_MENU);
					}
				}));
		components.add(ComponentFactory.generateButton(this,
				FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.options.video"),
				ratio*0.2f,
				0.25f,
				0.084f,
				ratio*0.5f,
				new Vector3f(0, 0.1f, -1f),
				0f,
				GUIAlignement.CENTER,
				GUIAlignement.CENTER,
				new Runnable() {
					@Override
					public void run() {
						FurryAttack.getInstance().getRenderer().getGUIRenderer().setCurrentGUI(GUIs.VIDEO_OPTIONS);
					}
				}));
		components.add(ComponentFactory.generateButton(this,
				FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.options.control"),
				ratio*0.2f,
				0.25f,
				0.084f,
				ratio*0.5f,
				new Vector3f(0f, 0f, -1f),
				0f,
				GUIAlignement.CENTER,
				GUIAlignement.CENTER,
				new Runnable() {
					@Override
					public void run() {
						FurryAttack.getInstance().getRenderer().getGUIRenderer().setCurrentGUI(GUIs.CONTROL_MENU);
					}
				}));
		components.add(ComponentFactory.generateButton(this,
				FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.options.language"),
				ratio*0.2f,
				0.25f,
				0.084f,
				ratio*0.5f,
				new Vector3f(0, -0.1f, -1f),
				0f,
				GUIAlignement.CENTER,
				GUIAlignement.CENTER,
				null));
		components.add(ComponentFactory.generateButton(this,
				FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.options.credits"),
				ratio*0.2f,
				0.25f,
				0.084f,
				ratio*0.5f,
				new Vector3f(0, -0.2f, -1f),
				0f,
				GUIAlignement.CENTER,
				GUIAlignement.CENTER,
				null));
	}

	@Override
	public void render() {
		for(GUIComponent comp : components)comp.render();
	}

	@Override
	public void update() {
		try {
			for(GUIComponent comp : components)comp.update();
		}catch(Exception e) {};
	}
	
	@Override
	public void destroy() {
		for(GUIComponent comp : components) {
			comp.destroy();
		}
		components.clear();
	}

	@Override
	public void recreate(int width, int height) {
		for(GUIComponent comp : components)comp.recreate(width, height);
	}
}
