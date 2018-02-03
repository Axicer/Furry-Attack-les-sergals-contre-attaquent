package fr.axicer.furryattack.gui;

import fr.axicer.furryattack.render.Destroyable;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;

public class GUIManager implements Renderable,Updateable,Destroyable{
	
	public static GUI MENU;
	
	private GUI actualGUI;
	
	public GUIManager() {
		MENU = new MenuGUI();
		//TODO add GUIs
		this.actualGUI = MENU;
	}
	
	public boolean setGUI(GUI gui) {
		if(!gui.equals(actualGUI)) {
			actualGUI = gui;
			return true;
		}
		return false;
	}

	@Override
	public void update() {
		actualGUI.update();
	}

	@Override
	public void render() {
		actualGUI.render();
	}
	
	@Override
	public void destroy() {
		MENU.destroy();
	}
	
}
