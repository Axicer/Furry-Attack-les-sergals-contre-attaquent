package fr.axicer.furryattack.render;

import fr.axicer.furryattack.gui.render.GuiRenderer;
import fr.axicer.furryattack.util.Constants;

/**
 * Main Game Renderer
 * @author Axicer
 *
 */
public class Renderer implements Renderable,Updateable,Destroyable{

	//The GUI system renderer
	private GuiRenderer GUIrenderer;
	private GUIFrameDrawer GUIdrawer;

	/**
	 * Construct the main renderer at initialization
	 */
	public Renderer() {
		//create the new GUI renderer
		this.GUIrenderer = new GuiRenderer();
		this.GUIdrawer = new GUIFrameDrawer(GUIrenderer.getGuiFBO().getGuiTextureId(), Constants.WIDTH, Constants.HEIGHT);
	}
	
	public GuiRenderer getGUIRenderer() {
		return this.GUIrenderer;
	}
	
	public void recreate() {
		this.GUIrenderer.recreate();
		this.GUIdrawer = new GUIFrameDrawer(GUIrenderer.getGuiFBO().getGuiTextureId(), Constants.WIDTH, Constants.HEIGHT);
	}
	
	@Override
	public void render() {
		//first render the GUIrenderer (which will update the current GUIFrameBufferObject)
		this.GUIrenderer.render();
		
		//then show the GUIFrameBufferObject's image to the screen
		this.GUIdrawer.render();
	}

	@Override
	public void update() {
		this.GUIrenderer.update();
		this.GUIdrawer.update();
	}

	@Override
	public void destroy() {
		this.GUIrenderer.destroy();
		this.GUIdrawer.destroy();
	}
}
