package fr.axicer.furryattack.gui;

import java.io.File;
import java.io.FileNotFoundException;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.gui.elements.GUIButton;
import fr.axicer.furryattack.gui.elements.GUIComponent;
import fr.axicer.furryattack.gui.elements.GUIImage;
import fr.axicer.furryattack.gui.elements.GUIInputButton;
import fr.axicer.furryattack.gui.elements.GUIText;
import fr.axicer.furryattack.util.Color;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.config.Configuration;
import fr.axicer.furryattack.util.config.FileManager;
import fr.axicer.furryattack.util.font.FontType;

public class ControlGUI extends GUI{

	public ControlGUI() {
		super("control");
		File f = new File(FileManager.CONFIG_FOLDER_FILE, "control.json");
		Configuration c = null;
		if(!f.exists()) {
			c = new Configuration();
			//TODO add default keys
			c.setInt("forward", GLFW.GLFW_KEY_W, true);
		}else {
			try {
				c = new Configuration(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		c.save(f);
		float ratio = (float)Constants.WIDTH/(float)Constants.HEIGHT;
		components.add(new GUIImage("/img/gui/background/menu-bg.png", //imgPath
				Constants.WIDTH, //width
				Constants.HEIGHT, //height
				new Vector3f(0,0,-1f))); //pos
		components.add(new GUIText("Contrôles", //text
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
						FurryAttack.getInstance().getGuiManager().setGUI(GUIManager.OPTIONS_MENU);
					}
				}));
		components.add(new GUIText("Avancer:", //text
				new Vector3f((float)-Constants.WIDTH/2f+(float)Constants.WIDTH/5.5f,(float)Constants.HEIGHT/3.5f,-0.5f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f)); //scale
		components.add(new GUIInputButton(c,
				f,
				"forward",
				ratio*0.2f,
				Constants.WIDTH/8f,
				Constants.HEIGHT/12f,
				ratio*0.5f,
				Color.WHITE,
				new Vector3f((float)-Constants.WIDTH/2f+(float)Constants.WIDTH/3.5f,(float)Constants.HEIGHT/3.5f,-0.5f),
				0f));
	}

	@Override
	public void render() {
		for(GUIComponent comp : components)comp.render();
	}

	@Override
	public void update() {
		for(GUIComponent comp : components)comp.update();
	}
	
	@Override
	public void destroy() {
		for(GUIComponent comp : components)comp.destroy();
	}
}
