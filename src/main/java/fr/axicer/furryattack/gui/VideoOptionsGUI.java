package fr.axicer.furryattack.gui;

import static org.lwjgl.glfw.GLFW.*;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL11;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.gui.elements.GUIButton;
import fr.axicer.furryattack.gui.elements.GUIComponent;
import fr.axicer.furryattack.gui.elements.GUIImage;
import fr.axicer.furryattack.gui.elements.GUIText;
import fr.axicer.furryattack.gui.elements.items.GUIResolution;
import fr.axicer.furryattack.gui.elements.selector.GUISelector;
import fr.axicer.furryattack.util.Color;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.Resolution;
import fr.axicer.furryattack.util.font.FontType;

public class VideoOptionsGUI extends GUI{

	public VideoOptionsGUI() {
		super("video-options");
		init();
	}

	private void init() {
		float ratio = (float)Constants.WIDTH/(float)Constants.HEIGHT;
		components.add(new GUIImage("/img/gui/background/menu-bg.png", //imgPath
				Constants.WIDTH, //width
				Constants.HEIGHT, //height
				new Vector3f(0,0,-1f))); //pos
		components.add(new GUIText("Options vidéo", //text
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
		//resolution selector
			GUISelector<Resolution> selector = new GUISelector<Resolution>(new Vector3f((float)-Constants.WIDTH/12f, (float)Constants.HEIGHT/6.5f, -1f),
					Constants.WIDTH/6f,
					Constants.HEIGHT/10f,
					FontType.CAPTAIN,
					Color.WHITE,
					getAllResolutions());
			selector.setActualItem(getActual());
			components.add(selector);
			components.add(new GUIButton("Appliquer résolution",
					ratio*0.175f,
					Constants.WIDTH/6f,
					Constants.HEIGHT/10f,
					ratio*0.5f,
					FontType.CAPTAIN,
					Color.WHITE,
					new Vector3f((float)Constants.WIDTH/12f, (float)Constants.HEIGHT/6.5f, -1f),
					0f,
					new Runnable() {
						@Override
						public void run() {
							Constants.WIDTH = selector.getActalItem().getVal().getWidth();
							Constants.HEIGHT = selector.getActalItem().getVal().getHeight();
							Constants.MAIN_CONFIG.setInt("width", Constants.WIDTH, true);
							Constants.MAIN_CONFIG.setInt("height", Constants.HEIGHT, true);
							Constants.MAIN_CONFIG.save(Constants.MAIN_CONFIG_FILE);
							GLFW.glfwSetWindowSize(FurryAttack.getInstance().window, Constants.WIDTH, Constants.HEIGHT);
							GL11.glViewport(0, 0, Constants.WIDTH, Constants.HEIGHT);
							FurryAttack.getInstance().projectionMatrix = new Matrix4f().ortho(-Constants.WIDTH/2, Constants.WIDTH/2, -Constants.HEIGHT/2, Constants.HEIGHT/2, 0.1f, 1000.0f);
							FurryAttack.getInstance().getGuiManager().recreate();
							FurryAttack.getInstance();
							GLFWVidMode vidmode = glfwGetVideoMode(glfwGetMonitors().get(FurryAttack.screenid));
							GLFW.glfwSetWindowPos(
									FurryAttack.getInstance().window,
									(vidmode.width() - Constants.WIDTH) / 2,
									(vidmode.height() - Constants.HEIGHT) / 2
								);
						}
					}));
			
	}
	
	private GUIResolution getActual() {
		for(GUIResolution resolution : getAllResolutions()) {
			if(resolution.getVal().getWidth() == Constants.WIDTH && resolution.getVal().getHeight()==Constants.HEIGHT)return resolution;
		}
		return getAllResolutions()[0];
	}
	private GUIResolution[] getAllResolutions() {
		//getting all resolutions
		GraphicsDevice dev = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[FurryAttack.screenid];
		List<GUIResolution> resolutionList = new ArrayList<>(dev.getDisplayModes().length);
		for(int i = 0 ; i < dev.getDisplayModes().length ; i++) {
			DisplayMode mode = dev.getDisplayModes()[i];
			if(!containsResolution(resolutionList, mode.getWidth(), mode.getHeight())) {
				resolutionList.add(new GUIResolution(new Resolution(mode.getWidth(), mode.getHeight()), mode.getWidth()+" * "+mode.getHeight()));
			}
		}
		GUIResolution[] resolutions = (GUIResolution[]) resolutionList.toArray(new GUIResolution[resolutionList.size()]);
		return resolutions;
	}
	
	private boolean containsResolution(List<GUIResolution> resolutions, int width, int height) {
		for(GUIResolution resol : resolutions) {
			if(resol.getVal().getWidth()==width && resol.getVal().getHeight()==height)return true;
		}
		return false;
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
