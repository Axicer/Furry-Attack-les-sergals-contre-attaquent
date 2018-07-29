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

public class MenuGUI extends GUI{

	public MenuGUI() {
		super("menu");
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
		components.add(new GUIText("FURRY-ATTACK", //text
									new Vector3f(0f, -0.1f, 0f), //pos
									0f, //rot
									FontType.CAPTAIN, //font
									new Color(255, 50, 50, 255), //color
									ratio*0.5f, //scale
									GUIAlignement.TOP,
									GUIAlignement.TOP));
		components.add(new GUIText("Les sergals contre attaquent",
									new Vector3f(0f, -0.2f, 0f),
									0f,
									FontType.CAPTAIN,
									new Color(50, 255, 255, 255),
									ratio*0.5f,
									GUIAlignement.TOP,
									GUIAlignement.TOP));
		components.add(ComponentFactory.generateButton(this,
									FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.main.party.new"),//text
									ratio*0.2f,// textscale
									0.25f, //width
									0.084f, //height
									ratio*0.5f, //scale
									new Vector3f(0f, 0.1f, -1f), //pos
									0f, //rot
									GUIAlignement.CENTER,
									GUIAlignement.CENTER,
									null));//action
		components.add(ComponentFactory.generateButton(this,
									FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.main.party.load"),
									ratio*0.2f,
									0.25f,
									0.084f,
									ratio*0.5f,
									new Vector3f(0f, 0f, -1f),
									0f,
									GUIAlignement.CENTER,
									GUIAlignement.CENTER,
									null));
		components.add(ComponentFactory.generateButton(this,
				FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.main.characterCustom"),
									ratio*0.15f,
									0.25f,
									0.084f,
									ratio*0.5f,
									new Vector3f(0f, -0.1f, -1f),
									0f,
									GUIAlignement.CENTER,
									GUIAlignement.CENTER,
									new Runnable() {
										public void run() {
											FurryAttack.getInstance().getRenderer().getGUIRenderer().setCurrentGUI(GUIs.CHARACTER_CHOICE_MENU);
										}
									}));
		components.add(ComponentFactory.generateButton(this,
				FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.main.gunCustom"),
									ratio*0.2f,
									0.25f,
									0.084f,
									ratio*0.5f,
									new Vector3f(0f, -0.2f, -1f),
									0f,
									GUIAlignement.CENTER,
									GUIAlignement.CENTER,
									null));
		components.add(ComponentFactory.generateButton(this,
				FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.main.quit"),
									ratio*0.2f,
									0.25f,
									0.084f,
									ratio*0.5f,
									new Vector3f(0f, -0.3f, -1f),
									0f,
									GUIAlignement.CENTER,
									GUIAlignement.CENTER,
									new Runnable() {
										@Override
										public void run() {
											FurryAttack.getInstance().running = false;
										}
									}));
		components.add(ComponentFactory.generateButton(this,
				FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.main.options"),
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
											FurryAttack.getInstance().getRenderer().getGUIRenderer().setCurrentGUI(GUIs.OPTION_MENU);
										}
									}));
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
		for(GUIComponent comp : components)comp.destroy();
	}

	@Override
	public void recreate(int width, int height) {
		for(GUIComponent comp : components)comp.recreate(width, height);
	}
}
