package fr.axicer.furryattack.map;

import java.util.Random;

import fr.axicer.furryattack.render.Destroyable;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.util.Color;
import fr.axicer.furryattack.util.noise.Noise;

public class Map implements Renderable,Updateable,Destroyable{
	
	private TileContainer tileContainer;
	
	public Map(int tile_amount_x) {
		tileContainer = new TileContainer(tile_amount_x);
		generate();
	}

	public TileContainer getTileContainer() {
		return tileContainer;
	}
	
	private void generate() {
		Noise noise = new Noise(new Random().nextLong(), 8, 0.5f);
		for(int x = 0 ; x < tileContainer.getTilesAmountX() ; x++) {
			float n = (float) (noise.getNoise(x, 0));
			for(int y = 0 ; y < tileContainer.getTilesAmountY() ; y++) {
				if(tileContainer.getTile(x, y) == null) {
					Tile t = new Tile(x, y, 0L);
					if(y < n*tileContainer.getTilesAmountY()) {
						t = t.setColor(Color.toInt(new Color(0, 255, 0, 255)));
					}else {
						t = t.setColor(Color.toInt(new Color(0, 0, 255, 255)));
					}
					tileContainer.setTile(x, y, t);
				}
			}
		}
	}

	@Override
	public void destroy() {
		tileContainer.destroy();
	}

	@Override
	public void update() {
		tileContainer.update();
	}

	@Override
	public void render() {
		tileContainer.render();
	}
}
