package fr.axicer.furryattack.gui;

import org.joml.Vector3f;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.gui.elements.GUIButton;
import fr.axicer.furryattack.gui.elements.GUIComponent;
import fr.axicer.furryattack.gui.elements.GUIImage;
import fr.axicer.furryattack.gui.elements.GUIText;
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
		components.add(new GUIImage("/img/gui/background/menu-bg.png", //imgPath
				Constants.WIDTH, //width
				Constants.HEIGHT, //height
				new Vector3f(0,0,-1f))); //pos
		components.add(new GUIText("Options", //text
				new Vector3f(0f, (float)Constants.HEIGHT/2.5f, -1f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				new Color(50, 70, 120, 255), // color
				ratio*0.5f)); //scale
		components.add(new GUIButton("Retour",
				ratio*0.2f,
				Constants.WIDTH/6f,
				Constants.HEIGHT/10f,
				ratio*0.5f,
				FontType.CAPTAIN,
				Color.WHITE,
				new Vector3f((float)Constants.WIDTH/2.5f, (float)-Constants.HEIGHT/2.5f, -1f),
				0f,
				new Runnable() {
					public void run() {
						FurryAttack.getInstance().getGuiManager().setGUI(GUIManager.MENU);
					}
				}));
		components.add(new GUIButton("Vidéo",
				ratio*0.2f,
				Constants.WIDTH/3f,
				Constants.HEIGHT/10f,
				ratio*0.5f,
				FontType.CAPTAIN,
				Color.WHITE,
				new Vector3f(0, (float)Constants.HEIGHT/6.5f, -1f),
				0f,
				new Runnable() {
					@Override
					public void run() {
						FurryAttack.getInstance().getGuiManager().setGUI(GUIManager.VIDEO_OPTION_MENU);
					}
				}));
		components.add(new GUIButton("Controles",
				ratio*0.2f,
				Constants.WIDTH/3f,
				Constants.HEIGHT/10f,
				ratio*0.5f,
				FontType.CAPTAIN,
				Color.WHITE,
				new Vector3f(0, (float)Constants.HEIGHT/20f, -1f),
				0f,
				new Runnable() {
					@Override
					public void run() {
						FurryAttack.getInstance().getGuiManager().setGUI(GUIManager.CONTROL_MENU);
					}
				}));
		components.add(new GUIButton("Langue",
				ratio*0.2f,
				Constants.WIDTH/3f,
				Constants.HEIGHT/10f,
				ratio*0.5f,
				FontType.CAPTAIN,
				Color.WHITE,
				new Vector3f(0, (float)-Constants.HEIGHT/20f, -1f),
				0f,
				null));
		components.add(new GUIButton("Credits",
				ratio*0.2f,
				Constants.WIDTH/3f,
				Constants.HEIGHT/10f,
				ratio*0.5f,
				FontType.CAPTAIN,
				Color.WHITE,
				new Vector3f(0, (float)-Constants.HEIGHT/6.5f, -1f),
				0f,
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

	public void recreate() {
		destroy();
		init();
	}
}
