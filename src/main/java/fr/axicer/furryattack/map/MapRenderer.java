package fr.axicer.furryattack.map;

import fr.axicer.furryattack.render.Destroyable;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;

/**
 * Map rendering system
 * @author Axicer
 *
 */
public class MapRenderer implements Renderable,Updateable,Destroyable{
	
	/**
	 * the map frame buffer
	 */
	private MapFrameBuffer mapFBO;
	
	/**
	 * the abstract map being rendered (can be null)
	 */
	private AbstractMap map;
	/**
	 * whether to render the map or not
	 */
	private boolean activated = true;
	
	/**
	 * Create a new {@link MapRenderer} with no map (null value)
	 */
	public MapRenderer() {
		mapFBO = new MapFrameBuffer();
		map = null;
	}

	@Override
	public void destroy() {
		mapFBO.cleanUp();
		if(map != null) map.destroy();
	}

	@Override
	public void update() {
		if(activated && map != null)map.update();
	}
	
	/**
	 * Recreate the {@link MapFrameBuffer}
	 */
	public void recreate() {
		mapFBO.cleanUp();
		mapFBO = new MapFrameBuffer();
	}

	@Override
	public void render() {
		if(!activated)return;
		mapFBO.bindMapFrameBuffer();
		if(map != null)map.render();
		mapFBO.unbindCurrentFrameBuffer();
	}
	
	/**
	 * Set whether to activate the map renderer
	 * if not then the buffer is cleared
	 * @param activated
	 */
	public void setActivated(boolean activated) {
		this.activated = activated;
		if(!activated)mapFBO.clearBuffer();
	}
	/**
	 * Get whether the map renderer is activated
	 * @return boolean
	 */
	public boolean isActivated() {
		return this.activated;
	}
	
	/**
	 * Get the {@link AbstractMap} map being rendered
	 * @return {@link AbstractMap} being rendered or null if nothing is rendered from this renderer
	 */
	public AbstractMap getMap() {
		return this.map;
	}
	
	/**
	 * Set the map to render
	 * @param map {@link AbstractMap} map to render or null to render nothing
	 */
	public void setMap(AbstractMap map) {
		this.map = map;
	}
	
	/**
	 * Get the {@link MapFrameBuffer}
	 * @return {@link MapFrameBuffer}
	 */
	public MapFrameBuffer getMapFBO() {
		return this.mapFBO;
	}
}
