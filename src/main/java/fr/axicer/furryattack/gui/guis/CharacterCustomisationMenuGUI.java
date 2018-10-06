package fr.axicer.furryattack.gui.guis;

import org.joml.Vector3f;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.entity.Species;
import fr.axicer.furryattack.entity.character.old.CharacterOld;
import fr.axicer.furryattack.gui.elements.ComponentFactory;
import fr.axicer.furryattack.gui.elements.GUIAlignement;
import fr.axicer.furryattack.gui.elements.GUIComponent;
import fr.axicer.furryattack.gui.elements.GUIText;
import fr.axicer.furryattack.gui.elements.selector.GUISelector;
import fr.axicer.furryattack.gui.elements.selector.GUISelectorItem;
import fr.axicer.furryattack.gui.render.GUIs;
import fr.axicer.furryattack.util.Color;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.font.FontType;

public class CharacterCustomisationMenuGUI extends GUI{

	public CharacterOld character;
	
	public CharacterCustomisationMenuGUI() {
		super("character-customisation-menu");
		init();
	}
	
	@SuppressWarnings("unchecked")
	private void init() {
		float ratio = (float)Constants.WIDTH/(float)Constants.HEIGHT;
		components.add(ComponentFactory.generateImage("/img/gui/background/menu-bg.png", //imgPath
									1, //width
									1, //height
									new Vector3f(0,0,-1f),//pos
									GUIAlignement.CENTER,
									GUIAlignement.CENTER)); //alignement
		components.add(new GUIText(FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.custom.character.title"), //text
									new Vector3f(0f, -0.1f, -1f), //pos
									0f, //rot
									FontType.CAPTAIN, //font
									new Color(50, 70, 120, 255), // color
									ratio*0.5f,
									GUIAlignement.TOP,
									GUIAlignement.TOP)); //scale
		components.add(ComponentFactory.generateImage("/img/gui/background/gray-back-bg.png",
									0.25f,
									0.5f,
									new Vector3f(0,0,-1f),
									GUIAlignement.CENTER,
									GUIAlignement.CENTER));
		components.add(ComponentFactory.generateButton(this,
				FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.custom.character.reset"),
				ratio*0.2f,
				0.125f,
				0.084f,
				ratio*0.5f,
				new Vector3f(-0.15f, 0.05f, -1f),
				0f,
				GUIAlignement.BOTTOM_RIGHT,
				GUIAlignement.BOTTOM_RIGHT,
				null));
		components.add(ComponentFactory.generateButton(this,
				FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.custom.character.apply"),
				ratio*0.2f,
				0.125f,
				0.084f,
				ratio*0.5f,
				new Vector3f(-0.025f, 0.05f, -1f),
				0f,
				GUIAlignement.BOTTOM_RIGHT,
				GUIAlignement.BOTTOM_RIGHT,
				null));
		components.add(ComponentFactory.generateButton(this,
				FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.custom.character.back"),
				ratio*0.2f,
				0.125f,
				0.084f,
				ratio*0.5f,
				new Vector3f(0.05f, 0.05f, -1f),
				0f,
				GUIAlignement.BOTTOM_LEFT,
				GUIAlignement.BOTTOM_LEFT,
				new Runnable() {
					public void run() {
						FurryAttack.getInstance().getRenderer().getGUIRenderer().setCurrentGUI(GUIs.MAIN_MENU);
					}
				}));
		components.add(ComponentFactory.generateButton(this,
				FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.custom.character.export"),
				ratio*0.2f,
				0.125f,
				0.084f,
				ratio*0.5f,
				new Vector3f(0.025f, 0.05f, -1f),
				0f,
				GUIAlignement.BOTTOM_LEFT,
				GUIAlignement.BOTTOM,
				null));
		components.add(ComponentFactory.generateButton(this,
				FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.custom.character.import"),
				ratio*0.2f,
				0.125f,
				0.084f,
				ratio*0.5f,
				new Vector3f(-0.025f, 0.05f, -1f),
				0f,
				GUIAlignement.BOTTOM_RIGHT,
				GUIAlignement.BOTTOM,
				null));
		components.add(new GUIText(FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.custom.character.race"), //text
				new Vector3f(0.025f,0.2f,-1f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f,//scale
				GUIAlignement.LEFT,
				GUIAlignement.LEFT)); 
		components.add(new GUISelector<Species>(
				this,
				new Vector3f(0.025f,0.1f,-1f),
				0.34f,
				0.1f,
				FontType.CAPTAIN,
				Color.WHITE,
				GUIAlignement.LEFT,
				GUIAlignement.LEFT,
				new GUISelectorItem<>(Species.FOX, "Renard"),
				new GUISelectorItem<>(Species.WOLF, "Loup"),
				new GUISelectorItem<>(Species.DUTCH_ANGEL_DRAGON, "Dutch Angel Dragon"),
				new GUISelectorItem<>(Species.HYENA, "Hyene")));
		components.add(new GUIText(FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.custom.character.color.primary"), //text
				new Vector3f(0.025f,0f,-1f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f,//scale
				GUIAlignement.LEFT,
				GUIAlignement.LEFT)); 
		components.add(new GUISelector<Color>(
				this,
				new Vector3f(0.025f,-0.1f,-1f),
				0.34f,
				0.1f,
				FontType.CAPTAIN,
				Color.WHITE,
				GUIAlignement.LEFT,
				GUIAlignement.LEFT,
				new GUISelectorItem<>(Color.WHITE, "Blanc"),
				new GUISelectorItem<>(Color.BLACK, "Noir")));
		components.add(new GUIText(FurryAttack.getInstance().getLangManager().getActualLanguage().getTranslation("menu.custom.character.color.secondary"), //text
				new Vector3f(0.025f,-0.2f,-1f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f, //scale
				GUIAlignement.LEFT,
				GUIAlignement.LEFT));
		components.add(new GUISelector<Color>(
				this,
				new Vector3f(0.025f,-0.3f,-1f),
				0.34f,
				0.1f,
				FontType.CAPTAIN,
				Color.WHITE,
				GUIAlignement.LEFT,
				GUIAlignement.LEFT,
				new GUISelectorItem<>(Color.WHITE, "Blanc"),
				new GUISelectorItem<>(Color.BLACK, "Noir")));
		character = new CharacterOld(Species.FOX, Color.WHITE, Color.BLACK, "");
	}

	@Override
	public void render() {
		for(GUIComponent comp : components)comp.render();
		character.render();
	}

	@Override
	public void update() {
		try {
			for(GUIComponent comp : components)comp.update();
			character.update();
		}catch(Exception e) {};
	}
	
	@Override
	public void destroy() {
		for(GUIComponent comp : components)comp.destroy();
		character.destroy();
	}
	
	@Override
	public void recreate(int width, int height) {
		for(GUIComponent comp : components)comp.recreate(width, height);
	}
}
