package fr.axicer.furryattack.gui.guis;

import java.io.File;
import java.io.FileNotFoundException;

import org.joml.Vector3f;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.generator.config.ControlConfigGenerator;
import fr.axicer.furryattack.gui.elements.ComponentFactory;
import fr.axicer.furryattack.gui.elements.GUIAlignement;
import fr.axicer.furryattack.gui.elements.GUIComponent;
import fr.axicer.furryattack.gui.elements.GUIText;
import fr.axicer.furryattack.gui.render.GUIs;
import fr.axicer.furryattack.util.Color;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.config.Configuration;
import fr.axicer.furryattack.util.config.FileManager;
import fr.axicer.furryattack.util.font.FontType;

public class ControlGUI extends GUI{

	public ControlGUI() {
		super("control");
		init();
	}
	
	private void init() {
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
		components.add(ComponentFactory.generateImage("/img/gui/background/menu-bg.png", //imgPath
				Constants.WIDTH, //width
				Constants.HEIGHT, //height
				new Vector3f(0,0,-1f), //pos
				GUIAlignement.CENTER,
				GUIAlignement.CENTER)); 
		components.add(new GUIText(FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.control.title"), //text
				new Vector3f(0f, -Constants.HEIGHT/10f, -1f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				new Color(50, 70, 120, 255), // color
				ratio*0.5f, //scale
				GUIAlignement.TOP,
				GUIAlignement.TOP));
		components.add(ComponentFactory.generateButton(this,
				FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.control.back"),
				ratio*0.2f,
				(int)(Constants.WIDTH/8f),
				(int)(Constants.HEIGHT/12f),
				ratio*0.5f,
				new Vector3f(50f, 50f, -1f),
				0f,
				GUIAlignement.BOTTOM_LEFT,
				GUIAlignement.BOTTOM_LEFT,
				new Runnable() {
					public void run() {
						FurryAttack.getInstance().getRenderer().getGUIRenderer().setCurrentGUI(GUIs.OPTION_MENU);
					}
				}));
		components.add(new GUIText(FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.control.up")+":", //text
				new Vector3f(-20f, 2*Constants.HEIGHT/10f,-0.5f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f, //scale
				GUIAlignement.RIGHT,
				GUIAlignement.CENTER));
		components.add(ComponentFactory.generateInputButton(this,
				c,
				f,
				ControlConfigGenerator.UP_CONTROL_ID,
				ratio*0.2f,
				(int)(Constants.WIDTH/8f),
				(int)(Constants.HEIGHT/12f),
				ratio*0.5f,
				new Vector3f(20f,2*Constants.HEIGHT/10f,-0.5f),
				0f,
				GUIAlignement.LEFT,
				GUIAlignement.CENTER));
		components.add(new GUIText(FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.control.left")+":", //text
				new Vector3f(-20f,Constants.HEIGHT/10f,-0.5f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f, //scale
				GUIAlignement.RIGHT,
				GUIAlignement.CENTER));
		components.add(ComponentFactory.generateInputButton(this,
				c,
				f,
				ControlConfigGenerator.LEFT_CONTROL_ID,
				ratio*0.2f,
				(int)(Constants.WIDTH/8f),
				(int)(Constants.HEIGHT/12f),
				ratio*0.5f,
				new Vector3f(20f,Constants.HEIGHT/10f,-0.5f),
				0f,
				GUIAlignement.LEFT,
				GUIAlignement.CENTER));
		components.add(new GUIText(FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.control.down")+":", //text
				new Vector3f(-20f, 0f,-0.5f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f, //scale
				GUIAlignement.RIGHT,
				GUIAlignement.CENTER));
		components.add(ComponentFactory.generateInputButton(this,
				c,
				f,
				ControlConfigGenerator.DOWN_CONTROL_ID,
				ratio*0.2f,
				(int)(Constants.WIDTH/8f),
				(int)(Constants.HEIGHT/12f),
				ratio*0.5f,
				new Vector3f(20f,0f,-0.5f),
				0f,
				GUIAlignement.LEFT,
				GUIAlignement.CENTER));
		components.add(new GUIText(FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.control.right")+":", //text
				new Vector3f(-20f,-Constants.HEIGHT/10f,-0.5f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f, //scale
				GUIAlignement.RIGHT,
				GUIAlignement.CENTER));
		components.add(ComponentFactory.generateInputButton(this,
				c,
				f,
				ControlConfigGenerator.RIGHT_CONTROL_ID,
				ratio*0.2f,
				(int)(Constants.WIDTH/8f),
				(int)(Constants.HEIGHT/12f),
				ratio*0.5f,
				new Vector3f(20f,-Constants.HEIGHT/10f,-0.5f),
				0f,
				GUIAlignement.LEFT,
				GUIAlignement.CENTER));
		components.add(new GUIText(FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.control.jump")+":", //text
				new Vector3f(-20f,2*-Constants.HEIGHT/10f,-0.5f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f, //scale
				GUIAlignement.RIGHT,
				GUIAlignement.CENTER));
		components.add(ComponentFactory.generateInputButton(this,
				c,
				f,
				ControlConfigGenerator.JUMP_CONTROL_ID,
				ratio*0.2f,
				(int)(Constants.WIDTH/8f),
				(int)(Constants.HEIGHT/12f),
				ratio*0.5f,
				new Vector3f(20f,2*-Constants.HEIGHT/10f,-0.5f),
				0f,
				GUIAlignement.LEFT,
				GUIAlignement.CENTER));
		components.add(new GUIText(FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.control.shift")+":", //text
				new Vector3f(-20f,3*-Constants.HEIGHT/10f,-0.5f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f, //scale
				GUIAlignement.RIGHT,
				GUIAlignement.CENTER));
		components.add(ComponentFactory.generateInputButton(this,
				c,
				f,
				ControlConfigGenerator.SHIFT_CONTROL_ID,
				ratio*0.2f,
				(int)(Constants.WIDTH/8f),
				(int)(Constants.HEIGHT/12f),
				ratio*0.5f,
				new Vector3f(20f,3*-Constants.HEIGHT/10f,-0.5f),
				0f,
				GUIAlignement.LEFT,
				GUIAlignement.CENTER));
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
