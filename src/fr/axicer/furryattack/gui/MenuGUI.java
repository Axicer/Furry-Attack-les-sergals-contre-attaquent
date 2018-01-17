package fr.axicer.furryattack.gui;

import org.joml.Vector3f;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.gui.elements.GUIButton;
import fr.axicer.furryattack.gui.elements.GUIComponent;
import fr.axicer.furryattack.gui.elements.GUIText;
import fr.axicer.furryattack.util.Color;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.font.FontType;

public class MenuGUI extends GUI{

	public MenuGUI() {
		super("menu");
		components.add(new GUIText("FURRY-ATTACK", new Vector3f(0f,  4f*Constants.HEIGHT/10f, -1f), 0f, FontType.ARIAL, new Color(255, 0, 0, 255), 1f));
		components.add(new GUIText("Les sergals contre attaquent", new Vector3f(0f, 3f*Constants.HEIGHT/10f, -1f),  0f, FontType.CONSOLAS, new Color(0, 255, 255, 255), 1f));
		components.add(new GUIButton("Nouvelle Partie", 0.3f, Constants.WIDTH/4f, Constants.HEIGHT/12f, 1f, FontType.CONSOLAS, new Color(255, 255, 255, 255), new Vector3f(0f, Constants.HEIGHT/10f, -1f), 0f, null));
		components.add(new GUIButton("Charger partie", 0.3f, Constants.WIDTH/4f, Constants.HEIGHT/12f, 1f, FontType.CONSOLAS, new Color(255, 255, 255, 255), new Vector3f(0f, 0f, -1f), 0f, null));
		components.add(new GUIButton("Personnalisation du personnage", 0.25f, Constants.WIDTH/4f, Constants.HEIGHT/12f, 1f, FontType.CONSOLAS, new Color(255, 255, 255, 255), new Vector3f(0f, -Constants.HEIGHT/10f, -1f), 0f, null));
		components.add(new GUIButton("Personnalisation de l'arme", 0.3f, Constants.WIDTH/4f, Constants.HEIGHT/12f, 1f, FontType.CONSOLAS, new Color(255, 255, 255, 255), new Vector3f(0f, -2*Constants.HEIGHT/10f, -1f), 0f, null));
		components.add(new GUIButton("Quitter", 0.3f, Constants.WIDTH/4f, Constants.HEIGHT/12f, 1f, FontType.CONSOLAS, new Color(255, 255, 255, 255), new Vector3f(0f, -3*Constants.HEIGHT/10f, -1f), 0f, new Runnable() {
			@Override
			public void run() {
				FurryAttack.getInstance().running = false;
			}
		}));
		components.add(new GUIButton("Options", 0.3f, Constants.WIDTH/8f, Constants.HEIGHT/12f, 1f, FontType.CONSOLAS, new Color(255, 255, 255, 255), new Vector3f(-Constants.WIDTH/2f+150f, -Constants.HEIGHT/2+100f, -1f), 0f, null));
	}

	@Override
	public void render() {
		for(GUIComponent comp : components)comp.render();
	}

	@Override
	public void update() {
		for(GUIComponent comp : components)comp.update();
	}
	
	public void destroy() {
		for(GUIComponent comp : components)comp.destroy();
	}

}
