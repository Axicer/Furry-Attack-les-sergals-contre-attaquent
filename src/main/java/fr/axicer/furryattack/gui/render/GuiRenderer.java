package fr.axicer.furryattack.gui.render;

import fr.axicer.furryattack.gui.elements.GUIButton;
import fr.axicer.furryattack.render.Destroyable;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.util.DelayableTask;

/**
 * Contains the main GUI rendering system
 * @author Axicer
 */
public class GuiRenderer implements Renderable,Updateable,Destroyable{

	private GUIFrameBufferObject guiFBO;
	private GUIs currentGUI;
	private boolean activated = true;
	
	/**
	 * Create the renderer class and initialize the FBO
	 */
	public GuiRenderer() {
		guiFBO = new GUIFrameBufferObject();
		currentGUI = GUIs.MAIN_MENU;
	}
	
	/**
	 * Get the Gui's FBO
	 * @return {@link GUIFrameBufferObject}
	 */
	public GUIFrameBufferObject getGuiFBO() {
		return this.guiFBO;
	}
	
	/**
	 * Set whether to activate the GUI renderer
	 * if not then the buffer is cleared
	 * @param activated
	 */
	public void setActivated(boolean activated) {
		this.activated = activated;
		if(!activated)guiFBO.clearBuffer();
	}
	/**
	 * Get whether the GUI renderer is activated
	 * @return int
	 */
	public boolean isActivated() {
		return this.activated;
	}
	
	/**
	 * Set the actual GUI to render
	 * put a 500ms delay to each buttons
	 * @param gui
	 */
	public void setCurrentGUI(GUIs gui) {
		this.currentGUI = gui;
		this.currentGUI.getGUI().getComponents().forEach(c -> {
			if(c instanceof GUIButton) {
				((GUIButton)c).setClickable(false);
			}
		});
		new DelayableTask(new Runnable() {
			@Override
			public void run() {
				currentGUI.getGUI().getComponents().forEach(c -> {
					if(c instanceof GUIButton) {
						((GUIButton)c).setClickable(true);
					}
				});
			}
		}, 250).start();
	}
	
	public GUIs getCurrentGUI() {
		return currentGUI;
	}

	@Override
	public void destroy() {
		guiFBO.cleanUp();
		currentGUI.getGUI().destroy();
	}

	@Override
	public void update() {
		if(activated)currentGUI.getGUI().update();
	}

	public void recreate() {
		guiFBO.cleanUp();
		guiFBO = new GUIFrameBufferObject();
		GUIs.recreate();
	}
	
	@Override
	public void render() {
		if(!activated)return;
		guiFBO.bindGuiFrameBuffer();
		currentGUI.getGUI().render();
		guiFBO.unbindCurrentFrameBuffer();
	}
	
}
