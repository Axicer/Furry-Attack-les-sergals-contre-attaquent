package fr.axicer.furryattack.gui.elements;

import java.io.File;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.util.Color;
import fr.axicer.furryattack.util.config.Configuration;
import fr.axicer.furryattack.util.control.events.KeyPressedEvent;
import fr.axicer.furryattack.util.events.EventListener;
import fr.axicer.furryattack.util.font.FontType;

public class GUIInputButton extends GUIButton implements EventListener{

	private static final int DEFAULT_KEY = 0;
	
	private Configuration config;
	private String path;
	private File configFile;
	
	public GUIInputButton(Configuration config, File configFile, String path, float textMul, float width, float height, float scale, Color color, Vector3f pos, float rot) {
		super(String.valueOf((char)config.getInt(path, DEFAULT_KEY)), textMul, width, height, "/img/gui/button/button.png", "/img/gui/button/button_hover.png", scale, FontType.CAPTAIN, color, pos, rot, null);
		GUIInputButton ib = this;
		setAction(new Runnable() {
			@Override
			public void run() {
				getTextGUI().setText("<entrez une valeur>");
				FurryAttack.getInstance().getEventManager().addListener(ib);
			}
		});
		this.config = config;
		this.path = path;
		this.configFile = configFile;
		setValue(config.getInt(path, DEFAULT_KEY));
	}
	
	public void setValue(int val) {
		if(val <= 0) {
			getTextGUI().setText("[unbind]");
		}else {
			if(val == GLFW.GLFW_KEY_SPACE) {
				getTextGUI().setText("SPACE");
			}else if(val == GLFW.GLFW_KEY_LEFT_SHIFT) {
				getTextGUI().setText("LSHIFT");
			}else if(val == GLFW.GLFW_KEY_RIGHT_SHIFT) {
				getTextGUI().setText("RSHIFT");
			}else if(val == GLFW.GLFW_KEY_LEFT_CONTROL) {
				getTextGUI().setText("LCONTROL");
			}else if(val == GLFW.GLFW_KEY_RIGHT_CONTROL) {
				getTextGUI().setText("RCONTROL");
			}else if(val == GLFW.GLFW_KEY_TAB) {
				getTextGUI().setText("TAB");
			}else if(val == GLFW.GLFW_KEY_LEFT_ALT) {
				getTextGUI().setText("LALT");
			}else if(val == GLFW.GLFW_KEY_RIGHT_ALT) {
				getTextGUI().setText("RALT");
			}else if(val == GLFW.GLFW_KEY_LEFT_SUPER) {
				getTextGUI().setText("LSUPER");
			}else if(val == GLFW.GLFW_KEY_RIGHT_SUPER) {
				getTextGUI().setText("RSUPER");
			}else if(val == GLFW.GLFW_KEY_DELETE) {
				getTextGUI().setText("DEL");
			}else if(val == GLFW.GLFW_KEY_ENTER) {
				getTextGUI().setText("ENTER");
			}else if(val == GLFW.GLFW_KEY_NUM_LOCK) {
				getTextGUI().setText("NUMLOCK");
			}else if(val == GLFW.GLFW_KEY_KP_ENTER) {
				getTextGUI().setText("KPENTER");
			}else {
				getTextGUI().setText(String.valueOf((char) val));
			}
		}
		config.setInt(path, val, true);
	}
	
	public void onKeyPressed(KeyPressedEvent ev) {
		if(ev.getKey() != GLFW.GLFW_KEY_ESCAPE) {
			setValue(ev.getKey());
		}else{
			setValue(DEFAULT_KEY);
		}
		config.save(configFile);
		FurryAttack.getInstance().getEventManager().addToDeletionList(this);
	}
}
