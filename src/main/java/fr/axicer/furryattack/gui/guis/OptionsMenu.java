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
				Constants.WIDTH, //width
				Constants.HEIGHT, //height
				new Vector3f(0,0,-1f), //pos
				GUIAlignement.CENTER)); 
		components.add(new GUIText("Options", //text
				new Vector3f(0f, (float)Constants.HEIGHT/2.5f, -1f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				new Color(50, 70, 120, 255), // color
				ratio*0.5f, //scale
				GUIAlignement.CENTER));
		components.add(ComponentFactory.generateButton(this,
				"Retour",
				ratio*0.2f,
				(int)(Constants.WIDTH/6f),
				(int)(Constants.HEIGHT/10f),
				ratio*0.5f,
				new Vector3f((float)Constants.WIDTH/2.5f, (float)-Constants.HEIGHT/2.5f, -1f),
				0f,
				GUIAlignement.CENTER,
				new Runnable() {
					public void run() {
						FurryAttack.getInstance().getRenderer().getGUIRenderer().setCurrentGUI(GUIs.MAIN_MENU);
					}
				}));
		components.add(ComponentFactory.generateButton(this,
				"Vidéo",
				ratio*0.2f,
				(int)(Constants.WIDTH/3f),
				(int)(Constants.HEIGHT/10f),
				ratio*0.5f,
				new Vector3f(0, (float)Constants.HEIGHT/6.5f, -1f),
				0f,
				GUIAlignement.CENTER,
				new Runnable() {
					@Override
					public void run() {
						FurryAttack.getInstance().getRenderer().getGUIRenderer().setCurrentGUI(GUIs.VIDEO_OPTIONS);
					}
				}));
		components.add(ComponentFactory.generateButton(this,
				"Controles",
				ratio*0.2f,
				(int)(Constants.WIDTH/3f),
				(int)(Constants.HEIGHT/10f),
				ratio*0.5f,
				new Vector3f(0, (float)Constants.HEIGHT/20f, -1f),
				0f,
				GUIAlignement.CENTER,
				new Runnable() {
					@Override
					public void run() {
						FurryAttack.getInstance().getRenderer().getGUIRenderer().setCurrentGUI(GUIs.CONTROL_MENU);
					}
				}));
		components.add(ComponentFactory.generateButton(this,
				"Langue",
				ratio*0.2f,
				(int)(Constants.WIDTH/3f),
				(int)(Constants.HEIGHT/10f),
				ratio*0.5f,
				new Vector3f(0, (float)-Constants.HEIGHT/20f, -1f),
				0f,
				GUIAlignement.CENTER,
				null));
		components.add(ComponentFactory.generateButton(this,
				"Credits",
				ratio*0.2f,
				(int)(Constants.WIDTH/3f),
				(int)(Constants.HEIGHT/10f),
				ratio*0.5f,
				new Vector3f(0, (float)-Constants.HEIGHT/6.5f, -1f),
				0f,
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

	public void recreate() {
		destroy();
		init();
	}
}
