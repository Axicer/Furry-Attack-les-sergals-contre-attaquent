package fr.axicer.furryattack.gui.elements.selector;

import org.joml.Vector3f;

import fr.axicer.furryattack.gui.elements.ComponentFactory;
import fr.axicer.furryattack.gui.elements.GUIButton;
import fr.axicer.furryattack.gui.elements.GUIComponent;
import fr.axicer.furryattack.gui.elements.GUIImage;
import fr.axicer.furryattack.gui.elements.GUIText;
import fr.axicer.furryattack.gui.guis.GUI;
import fr.axicer.furryattack.util.Color;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.DelayableTask;
import fr.axicer.furryattack.util.font.FontType;

public class GUISelector<T> extends GUIComponent{

	private GUISelectorItem<T>[] items;
	private int selectedIndex;
	
	private GUIButton left;
	private GUIButton right;
	private GUIText text;
	private GUIImage bg;
	
	@SuppressWarnings("unchecked")
	public GUISelector(GUI gui, Vector3f pos, int width, int height, FontType type, Color textColor, GUISelectorItem<T>... items) {
		this.items = items;
		this.selectedIndex = 0;
		float ratio = (float)Constants.WIDTH/(float)Constants.HEIGHT;
		
		this.text = new GUIText(items[selectedIndex].getName(), pos, 0f, type, textColor, ratio*0.2f);
		int buttonsWidth = (int) ((1f/5f)*width);
		int buttonsHeight = (int) ((2f/3f)*height);
		
		Vector3f leftPos = new Vector3f();
		pos.sub(width/2-buttonsWidth/2, 0, 0, leftPos);
		this.left = ComponentFactory.generateButton(gui, "<", ratio*0.2f, buttonsWidth, buttonsHeight, ratio*0.5f, leftPos, 0f, new Runnable() {
			public void run() {
				getPreviousItem();
			}
		});
		
		Vector3f rightPos = new Vector3f();
		pos.add(width/2-buttonsWidth/2, 0, 0, rightPos);
		this.right = ComponentFactory.generateButton(gui, ">", ratio*0.2f, buttonsWidth, buttonsHeight, ratio*0.5f, rightPos, 0f, new Runnable() {
			public void run() {
				getNextItem();
			}
		});
		this.bg = ComponentFactory.generateImage("/img/gui/background/gray-back-bg.png", width, height, pos);
	}
	
	public GUISelectorItem<T> getActalItem() {
		return items[selectedIndex];
	}
	
	public void setActualItem(GUISelectorItem<T> item) {
		for(int i = 0 ; i < items.length ; i++) {
			if(items[i].equals(item)) {
				selectedIndex = i;
				updateText();
				break;
			}
		}
	}
	
	public void getNextItem() {
		selectedIndex++;
		if(selectedIndex == this.items.length)selectedIndex = 0;
		updateText();
		this.right.setClickable(false);
		new DelayableTask(new Runnable() {
			public void run() {
				right.setClickable(true);
			}
		}, 250).start();
	}
	
	public void getPreviousItem() {
		selectedIndex--;
		if(selectedIndex == -1)selectedIndex = this.items.length-1;
		updateText();
		this.left.setClickable(false);
		new DelayableTask(new Runnable() {
			public void run() {
				left.setClickable(true);
			}
		}, 250).start();
	}
	
	private void updateText() {
		this.text.setText(items[selectedIndex].getName());
	}
	
	@Override
	public void render() {
		//bg.render();
		left.render();
		text.render();
		right.render();
	}

	@Override
	public void update() {
		bg.update();
		left.update();
		text.update();
		right.update();
	}

	@Override
	public void destroy() {
		bg.destroy();
		left.destroy();
		text.destroy();
		right.destroy();
	}

}
