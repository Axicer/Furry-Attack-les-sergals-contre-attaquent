package fr.axicer.furryattack.gui.elements.selector;

import org.joml.Vector3f;

import fr.axicer.furryattack.gui.elements.ComponentFactory;
import fr.axicer.furryattack.gui.elements.GUIAlignement;
import fr.axicer.furryattack.gui.elements.GUIButton;
import fr.axicer.furryattack.gui.elements.GUIComponent;
import fr.axicer.furryattack.gui.elements.GUIImage;
import fr.axicer.furryattack.gui.elements.GUIText;
import fr.axicer.furryattack.gui.guis.GUI;
import fr.axicer.furryattack.util.Color;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.DelayableTask;
import fr.axicer.furryattack.util.font.FontType;

/**
 * A selector component
 * @author Axicer
 *
 * @param <T>
 */
public class GUISelector<T> extends GUIComponent{

	/**
	 * array of all items
	 */
	private GUISelectorItem<T>[] items;
	/**
	 * int selecting the good index
	 */
	private int selectedIndex;
	/**
	 * the left button component
	 */
	private GUIButton left;
	/**
	 * the right button component
	 */
	private GUIButton right;
	/**
	 * the text component
	 */
	private GUIText text;
	/**
	 * the background component
	 */
	private GUIImage bg;
	/**
	 * the selector's width and height
	 */
	private int width, height;
	
	@SuppressWarnings("unchecked")
	public GUISelector(GUI gui, Vector3f pos, int width, int height, FontType type, Color textColor, GUIAlignement alignement, GUIAlignement guialignement, GUISelectorItem<T>... items) {
		this.items = items;
		this.selectedIndex = 0;
		float ratio = (float)Constants.WIDTH/(float)Constants.HEIGHT;
		this.alignement = alignement;
		this.guialignement = guialignement;
		this.width = width;
		this.height = height;
		
		this.text = new GUIText(items[selectedIndex].getName(), new Vector3f(pos.x+alignement.getOffsetXfromCenter(width), pos.y+alignement.getOffsetYfromCenter(height),pos.z), 0f, type, textColor, ratio*0.2f, GUIAlignement.CENTER, guialignement);
		int buttonsWidth = (int) ((1f/5f)*width);
		int buttonsHeight = (int) ((2f/3f)*height);
		
		Vector3f leftPos = new Vector3f();
		pos.sub(width/2-buttonsWidth/2, 0, 0, leftPos);
		leftPos.add(alignement.getOffsetXfromCenter(width), alignement.getOffsetYfromCenter(height), 0, leftPos);
		this.left = ComponentFactory.generateButton(gui, "<", ratio*0.2f, buttonsWidth, buttonsHeight, ratio*0.5f, leftPos, 0f, GUIAlignement.CENTER, guialignement, new Runnable() {
			public void run() {
				getPreviousItem();
			}
		});
		
		Vector3f rightPos = new Vector3f();
		pos.add(width/2-buttonsWidth/2, 0, 0, rightPos);
		rightPos.add(alignement.getOffsetXfromCenter(width), alignement.getOffsetYfromCenter(height), 0, rightPos);
		this.right = ComponentFactory.generateButton(gui, ">", ratio*0.2f, buttonsWidth, buttonsHeight, ratio*0.5f, rightPos, 0f, GUIAlignement.CENTER, guialignement, new Runnable() {
			public void run() {
				getNextItem();
			}
		});
		this.bg = ComponentFactory.generateImage("/img/gui/background/gray-back-bg.png", width, height, new Vector3f(pos.x+alignement.getOffsetXfromCenter(width), pos.y+alignement.getOffsetYfromCenter(height),pos.z), GUIAlignement.CENTER, guialignement);
	}
	
	/**
	 * Get the actual item selected
	 * @return {@link GUISelectorItem} selected
	 */
	public GUISelectorItem<T> getActalItem() {
		return items[selectedIndex];
	}
	/**
	 * set the actual selected
	 * @param item {@link GUISelectorItem} item
	 */
	public void setActualItem(GUISelectorItem<T> item) {
		for(int i = 0 ; i < items.length ; i++) {
			if(items[i].equals(item)) {
				selectedIndex = i;
				updateText();
				break;
			}
		}
	}
	/**
	 * set the cursor to the next item
	 */
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
	/**
	 * set the cursor to the previous item
	 */
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
	
	/**
	 * update the text
	 */
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
		//bg.update();
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

	/**
	 * Get the selector's total width
	 * @return int width
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * get the selector's total height
	 * @return int height
	 */
	public int getHeight() {
		return height;
	}
	
	@Override
	public void setComponentAlignement(GUIAlignement alignement) {
		this.alignement = alignement;
	}

}
