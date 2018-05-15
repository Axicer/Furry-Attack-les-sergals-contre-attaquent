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
									Constants.WIDTH, //width
									Constants.HEIGHT, //height
									new Vector3f(0,0,-1f), //pos
									GUIAlignement.CENTER));
		components.add(new GUIText("FURRY-ATTACK", //text
									new Vector3f(0f,  4f*Constants.HEIGHT/10f, -1f), //pos
									0f, //rot
									FontType.CAPTAIN, //font
									new Color(255, 50, 50, 255), //color
									ratio*0.5f, //scale
									GUIAlignement.CENTER));
		
		components.add(new GUIText("Les sergals contre attaquent",
									new Vector3f(0f, 3f*Constants.HEIGHT/10f, -1f),
									0f,
									FontType.CAPTAIN,
									new Color(50, 255, 255, 255),
									ratio*0.5f,
									GUIAlignement.CENTER));
		
		components.add(ComponentFactory.generateButton(this,
									"Nouvelle Partie",//text
									ratio*0.2f,// textscale
									(int)(Constants.WIDTH/4f), //width
									(int)(Constants.HEIGHT/12f), //height
									ratio*0.5f, //scale
									new Vector3f(0f, Constants.HEIGHT/10f, -1f), //pos
									0f, //rot
									GUIAlignement.CENTER,
									null));//action
		components.add(ComponentFactory.generateButton(this,
									"Charger partie",
									ratio*0.2f,
									(int)(Constants.WIDTH/4f),
									(int)(Constants.HEIGHT/12f),
									ratio*0.5f,
									new Vector3f(0f, 0f, -1f),
									0f,
									GUIAlignement.CENTER,
									null));
		components.add(ComponentFactory.generateButton(this,
									"Personnalisation du personnage",
									ratio*0.15f,
									(int)(Constants.WIDTH/4f),
									(int)(Constants.HEIGHT/12f),
									ratio*0.5f,
									new Vector3f(0f, -Constants.HEIGHT/10f, -1f),
									0f,
									GUIAlignement.CENTER,
									new Runnable() {
										public void run() {
											FurryAttack.getInstance().getRenderer().getGUIRenderer().setCurrentGUI(GUIs.CHARACTER_CHOICE_MENU);
										}
									}));
		components.add(ComponentFactory.generateButton(this,
									"Personnalisation de l'arme",
									ratio*0.2f,
									(int)(Constants.WIDTH/4f),
									(int)(Constants.HEIGHT/12f),
									ratio*0.5f,
									new Vector3f(0f, -2*Constants.HEIGHT/10f, -1f),
									0f,
									GUIAlignement.CENTER,
									null));
		components.add(ComponentFactory.generateButton(this,
									"Quitter",
									ratio*0.2f,
									(int)(Constants.WIDTH/4f),
									(int)(Constants.HEIGHT/12f),
									ratio*0.5f,
									new Vector3f(0f, -3*Constants.HEIGHT/10f, -1f),
									0f,
									GUIAlignement.CENTER,
									new Runnable() {
										@Override
										public void run() {
											FurryAttack.getInstance().running = false;
										}
									}));
		components.add(ComponentFactory.generateButton(this,
									"Options",
									ratio*0.2f,
									(int)(Constants.WIDTH/8f),
									(int)(Constants.HEIGHT/12f),
									ratio*0.5f,
									new Vector3f(-Constants.WIDTH/2f+150f, -Constants.HEIGHT/2+100f, -1f),
									0f,
									GUIAlignement.CENTER,
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

	
	public void recreate() {
		destroy();
		init();
	}
}
