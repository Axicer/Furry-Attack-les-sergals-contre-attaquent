package fr.axicer.furryattack.map;

import fr.axicer.furryattack.render.Destroyable;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.debug.Debugger;

public class TileContainer implements Renderable, Updateable, Destroyable{

	//tile size in pixels
	public static int TILE_SIZE;
	//tiles array
	private Tile[][] tiles;
	
	/**
	 * Constructor of a tileContainer<br>
	 * TILE_SIZE is set to current frame with divided by amount of tile on X
	 * @param tile_amount_x amount of tile on X axis
	 * @param tile_amount_y amount of tile on Y axis
	 */
	public TileContainer(int tile_amount_x) {
		int tile_amount_y = (int) (((float)Constants.HEIGHT/(float)Constants.WIDTH)*tile_amount_x) +1 ;//add because of non-round resolution
		tiles = new Tile[tile_amount_y][tile_amount_x];
		resizeTile();
	}

	/**
	 * Set the tile size to current frame with divided by amount of tile on X
	 */
	public void resizeTile() {
		TILE_SIZE = Constants.WIDTH/getTilesAmountX();
	}
	/**
	 * Get a tile at a given place
	 * @param x pos
	 * @param y pos
	 * @return tile to get
	 */
	public Tile getTile(int x, int y) {
		if(y >= 0 && y < tiles.length) {
			if(x >= 0 && x < tiles[y].length) {
				return tiles[y][x];
			}
		}
		Debugger.debug("trying to get a tile at unknown pos: "+x+":"+y);
		return null;
	}
	
	/**
	 * Set a tile at a given place
	 * @param x pos
	 * @param y pos
	 * @param t tile to set
	 */
	public void setTile(int x, int y, Tile t) {
		if(y >= 0 && y < tiles.length) {
			if(x >= 0 && x < tiles[y].length) {
				tiles[y][x] = t;
				return;
			}
		}
		Debugger.debug("trying to set a tile at unknown pos: "+x+":"+y);
	}
	
	/**
	 * @return amount of tiles on X axis
	 */
	public int getTilesAmountX() {
		return tiles[0].length;
	}
	
	/**
	 * @return amount of tiles on Y axis
	 */
	public int getTilesAmountY() {
		return tiles.length;
	}
	
	@Override
	public void destroy() {
		for(int y = 0 ; y < tiles.length ; y++) {
			for(int x = 0 ; x < tiles[y].length ; x++) {
				if(tiles[y][x] != null) {
					tiles[y][x].destroy();					
				}
			}
		}
	}

	@Override
	public void update() {
		for(int y = 0 ; y < tiles.length ; y++) {
			for(int x = 0 ; x < tiles[y].length ; x++) {
				if(tiles[y][x] != null) {
					tiles[y][x].update();
				}
			}
		}
	}

	@Override
	public void render() {
		for(int y = 0 ; y < tiles.length ; y++) {
			for(int x = 0 ; x < tiles[y].length ; x++) {
				if(tiles[y][x] != null) {
					tiles[y][x].render();					
				}
			}
		}
	}
	
}
