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

public class MenuGUI extends GUI{

	public MenuGUI() {
		super("menu");
		init();
	}
	
	private void init() {
		float ratio = (float)Constants.WIDTH/(float)Constants.HEIGHT;
		
		components.add(new GUIImage("/img/gui/background/menu-bg.png", //imgPath
									Constants.WIDTH, //width
									Constants.HEIGHT, //height
									new Vector3f(0,0,-1f))); //pos
		components.add(new GUIText("FURRY-ATTACK", //text
									new Vector3f(0f,  4f*Constants.HEIGHT/10f, -1f), //pos
									0f, //rot
									FontType.CAPTAIN, //font
									new Color(255, 50, 50, 255), //color
									ratio*0.5f)); //scale
		
		components.add(new GUIText("Les sergals contre attaquent",
									new Vector3f(0f, 3f*Constants.HEIGHT/10f, -1f),
									0f,
									FontType.CAPTAIN,
									new Color(50, 255, 255, 255),
									ratio*0.5f));
		
		components.add(new GUIButton("Nouvelle Partie",//text
									ratio*0.2f,// textscale
									Constants.WIDTH/4f, //width
									Constants.HEIGHT/12f, //height
									ratio*0.5f, //scale
									FontType.CAPTAIN, //font
									Color.WHITE, //color
									new Vector3f(0f, Constants.HEIGHT/10f, -1f), //pos
									0f, //rot
									null));//action
		components.add(new GUIButton("Charger partie",
									ratio*0.2f,
									Constants.WIDTH/4f,
									Constants.HEIGHT/12f,
									ratio*0.5f,
									FontType.CAPTAIN,
									Color.WHITE,
									new Vector3f(0f, 0f, -1f),
									0f,
									null));
		components.add(new GUIButton("Personnalisation du personnage",
									ratio*0.15f,
									Constants.WIDTH/4f,
									Constants.HEIGHT/12f,
									ratio*0.5f,
									FontType.CAPTAIN,
									Color.WHITE,
									new Vector3f(0f, -Constants.HEIGHT/10f, -1f),
									0f,
									new Runnable() {
										public void run() {
											FurryAttack.getInstance().getGuiManager().setGUI(GUIManager.CHARACTER_CUSTOMISATION_MENU);
										}
									}));
		components.add(new GUIButton("Personnalisation de l'arme",
									ratio*0.2f,
									Constants.WIDTH/4f,
									Constants.HEIGHT/12f,
									ratio*0.5f,
									FontType.CAPTAIN,
									Color.WHITE,
									new Vector3f(0f, -2*Constants.HEIGHT/10f, -1f),
									0f,
									null));
		components.add(new GUIButton("Quitter",
									ratio*0.2f,
									Constants.WIDTH/4f,
									Constants.HEIGHT/12f,
									ratio*0.5f,
									FontType.CAPTAIN,
									Color.WHITE,
									new Vector3f(0f, -3*Constants.HEIGHT/10f, -1f),
									0f,
									new Runnable() {
										@Override
										public void run() {
											FurryAttack.getInstance().running = false;
										}
									}));
		components.add(new GUIButton("Options",
									ratio*0.2f,
									Constants.WIDTH/8f,
									Constants.HEIGHT/12f,
									ratio*0.5f,
									FontType.CAPTAIN,
									Color.WHITE,
									new Vector3f(-Constants.WIDTH/2f+150f, -Constants.HEIGHT/2+100f, -1f),
									0f,
									new Runnable() {
										public void run() {
											FurryAttack.getInstance().getGuiManager().setGUI(GUIManager.OPTIONS_MENU);
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
