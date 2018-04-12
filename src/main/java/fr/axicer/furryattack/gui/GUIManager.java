package fr.axicer.furryattack.gui;

import fr.axicer.furryattack.gui.elements.GUIButton;
import fr.axicer.furryattack.render.Destroyable;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;
import fr.axicer.furryattack.util.DelayableTask;

public class GUIManager implements Renderable,Updateable,Destroyable{
	
	public static GUI MENU;
	public static GUI CHARACTER_CUSTOMISATION_MENU;
	public static GUI OPTIONS_MENU;
	public static GUI CONTROL_MENU;
	
	private GUI actualGUI;
	
	public GUIManager() {
		MENU = new MenuGUI();
		CHARACTER_CUSTOMISATION_MENU = new CharacterCustomisationMenuGUI();
		OPTIONS_MENU = new OptionsMenu();
		CONTROL_MENU = new ControlGUI();
		//TODO add GUIs
		this.actualGUI = MENU;
	}
	
	public boolean setGUI(GUI gui) {
		if(!gui.equals(actualGUI)) {
			actualGUI = gui;
			actualGUI.components.forEach(c -> {
				if(c instanceof GUIButton) {
					((GUIButton)c).setClickable(false);
				}
			});
			new DelayableTask(new Runnable() {
				@Override
				public void run() {
					actualGUI.components.forEach(c -> {
						if(c instanceof GUIButton) {
							((GUIButton)c).setClickable(true);
						}
					});
				}
			}, 500L).start();
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
		CHARACTER_CUSTOMISATION_MENU.destroy();
		OPTIONS_MENU.destroy();
		CONTROL_MENU.destroy();
	}
	
}
