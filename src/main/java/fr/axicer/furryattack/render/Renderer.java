package fr.axicer.furryattack.render;

import fr.axicer.furryattack.gui.render.GuiRenderer;
import fr.axicer.furryattack.map.old.MapRenderer;
import fr.axicer.furryattack.util.Constants;

/**
 * Main Game Renderer
 * @author Axicer
 *
 */
public class Renderer implements Renderable,Updateable,Destroyable{

	//The GUI system renderer
	private GuiRenderer GUIrenderer;
	private FrameDrawer GUIdrawer;
	
	//the Map system renderer
	private MapRenderer mapRenderer;
	private FrameDrawer mapDrawer;

	/**
	 * Construct the main renderer at initialization
	 */
	public Renderer() {
		//create the new GUI renderer
		this.GUIrenderer = new GuiRenderer();
		this.GUIdrawer = new FrameDrawer(GUIrenderer.getGuiFBO().getTextureId(), Constants.WIDTH, Constants.HEIGHT);
		
		//reate the new map renderer
		this.mapRenderer = new MapRenderer();
		this.mapDrawer = new FrameDrawer(mapRenderer.getMapFBO().getTextureId(), Constants.WIDTH, Constants.HEIGHT);
		
	}
	
	public GuiRenderer getGUIRenderer() {
		return this.GUIrenderer;
	}
	public MapRenderer getMapRenderer() {
		return this.mapRenderer;
	}
	
	public void recreate(int width, int height) {
		this.GUIrenderer.recreate(width, height);
		this.GUIdrawer = new FrameDrawer(GUIrenderer.getGuiFBO().getTextureId(), Constants.WIDTH, Constants.HEIGHT);
		
		this.mapRenderer.recreate();
		this.mapDrawer = new FrameDrawer(mapRenderer.getMapFBO().getTextureId(), Constants.WIDTH, Constants.HEIGHT);
	}
	
	@Override
	public void render() {
		//first render the GUIrenderer (which will update the current GUI Frame buffer)
		this.GUIrenderer.render();
		
		//then show the GUIFrameBufferObject's image to the screen
		if(GUIrenderer.isActivated())this.GUIdrawer.render();
	
		//render the map renderer (which will update the current map frame buffer)
		this.mapRenderer.render();

		//show the map frame drawer
		if(mapRenderer.isActivated())this.mapDrawer.render();
	}

	@Override
	public void update() {
		this.GUIrenderer.update();
		this.GUIdrawer.update();
		this.mapRenderer.update();
		this.mapDrawer.update();
	}

	@Override
	public void destroy() {
		this.GUIrenderer.destroy();
		this.GUIdrawer.destroy();
		this.mapRenderer.destroy();
		this.mapDrawer.destroy();
	}
	
	public void clearBuffers() {
		GUIrenderer.getGuiFBO().clearBuffer();
		mapRenderer.getMapFBO().clearBuffer();
	}
}
