package fr.axicer.furryattack.gui.elements;

import java.io.File;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.gui.guis.GUI;
import fr.axicer.furryattack.util.Color;
import fr.axicer.furryattack.util.Util;
import fr.axicer.furryattack.util.config.Configuration;
import fr.axicer.furryattack.util.control.events.KeyTypedEvent;
import fr.axicer.furryattack.util.events.EventListener;
import fr.axicer.furryattack.util.font.FontType;

//TODO remake the whole class
public class GUIInputButton extends GUIButton implements EventListener{

	private static final int DEFAULT_KEY = 0;
	
	private Configuration config;
	private String path;
	private File configFile;
	
	public GUIInputButton(GUI gui, Configuration config, File configFile, String path, float textMul, FontType type, int width, int height, float scale, Color color, Vector3f pos, float rot, String texturePath, String textureHoverPath, String textureClickPath) {
		super(gui, Util.getKeyRepresentation(config.getInt(path, DEFAULT_KEY)), textMul, width, height, texturePath, textureHoverPath, textureClickPath, scale, type, color, pos, rot, null);
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
	}
	
	public void setValue(int val) {
		if(val <= 0) {
			getTextGUI().setText("[unbind]");
		}else {
			getTextGUI().setText(Util.getKeyRepresentation(val));
		}
		config.setInt(path, val, true);
	}
	
	public void onKeyPressed(KeyTypedEvent ev) {
		if(ev.getKey() != GLFW.GLFW_KEY_ESCAPE) {
			setValue(ev.getKey());
		}else{
			setValue(DEFAULT_KEY);
		}
		config.save(configFile);
//		FurryAttack.getInstance().getEventManager().addToDeletionList(this);
	}
}
