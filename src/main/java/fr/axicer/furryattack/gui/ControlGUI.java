package fr.axicer.furryattack.gui;

import java.io.File;
import java.io.FileNotFoundException;

import org.joml.Vector3f;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.generator.config.ControlConfigGenerator;
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
			c = new ControlConfigGenerator().generate();
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
		components.add(new GUIText("Haut:", //text
				new Vector3f(-75f,(float)Constants.HEIGHT/4.25f,-0.5f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f)); //scale
		components.add(new GUIInputButton(c,
				f,
				ControlConfigGenerator.UP_CONTROL_ID,
				ratio*0.2f,
				Constants.WIDTH/8f,
				Constants.HEIGHT/12f,
				ratio*0.5f,
				Color.WHITE,
				new Vector3f(75f,(float)Constants.HEIGHT/4.25f,-0.5f),
				0f));
		components.add(new GUIText("Gauche:", //text
				new Vector3f(-75f,(float)Constants.HEIGHT/7f,-0.5f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f)); //scale
		components.add(new GUIInputButton(c,
				f,
				ControlConfigGenerator.LEFT_CONTROL_ID,
				ratio*0.2f,
				Constants.WIDTH/8f,
				Constants.HEIGHT/12f,
				ratio*0.5f,
				Color.WHITE,
				new Vector3f(75f,(float)Constants.HEIGHT/7f,-0.5f),
				0f));
		components.add(new GUIText("Bas:", //text
				new Vector3f(-75f,(float)Constants.HEIGHT/20f,-0.5f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f)); //scale
		components.add(new GUIInputButton(c,
				f,
				ControlConfigGenerator.DOWN_CONTROL_ID,
				ratio*0.2f,
				Constants.WIDTH/8f,
				Constants.HEIGHT/12f,
				ratio*0.5f,
				Color.WHITE,
				new Vector3f(75f,(float)Constants.HEIGHT/20f,-0.5f),
				0f));
		components.add(new GUIText("Droite:", //text
				new Vector3f(-75f,(float)-Constants.HEIGHT/20f,-0.5f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f)); //scale
		components.add(new GUIInputButton(c,
				f,
				ControlConfigGenerator.RIGHT_CONTROL_ID,
				ratio*0.2f,
				Constants.WIDTH/8f,
				Constants.HEIGHT/12f,
				ratio*0.5f,
				Color.WHITE,
				new Vector3f(75f,(float)-Constants.HEIGHT/20f,-0.5f),
				0f));
		components.add(new GUIText("Sauter:", //text
				new Vector3f(-75f,(float)-Constants.HEIGHT/7f,-0.5f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f)); //scale
		components.add(new GUIInputButton(c,
				f,
				ControlConfigGenerator.JUMP_CONTROL_ID,
				ratio*0.2f,
				Constants.WIDTH/8f,
				Constants.HEIGHT/12f,
				ratio*0.5f,
				Color.WHITE,
				new Vector3f(75f,(float)-Constants.HEIGHT/7f,-0.5f),
				0f));
		components.add(new GUIText("S'accroupir:", //text
				new Vector3f(-100f,(float)-Constants.HEIGHT/4.25f,-0.5f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f)); //scale
		components.add(new GUIInputButton(c,
				f,
				ControlConfigGenerator.SHIFT_CONTROL_ID,
				ratio*0.2f,
				Constants.WIDTH/8f,
				Constants.HEIGHT/12f,
				ratio*0.5f,
				Color.WHITE,
				new Vector3f(75f,(float)-Constants.HEIGHT/4.25f,-0.5f),
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
