package fr.axicer.furryattack.gui.guis;

import org.joml.Vector3f;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.character.Character;
import fr.axicer.furryattack.character.Species;
import fr.axicer.furryattack.character.animation.CharacterAnimation;
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

	public Character character;
	
	public CharacterCustomisationMenuGUI() {
		super("character-customisation-menu");
		init();
	}
	
	@SuppressWarnings("unchecked")
	private void init() {
		float ratio = (float)Constants.WIDTH/(float)Constants.HEIGHT;
		components.add(ComponentFactory.generateImage("/img/gui/background/menu-bg.png", //imgPath
									Constants.WIDTH, //width
									Constants.HEIGHT, //height
									new Vector3f(0,0,-1f),//pos
									GUIAlignement.CENTER)); //alignement
		components.add(new GUIText("Customisation du personnage", //text
									new Vector3f(0f, (float)Constants.HEIGHT/2.5f, -1f), //pos
									0f, //rot
									FontType.CAPTAIN, //font
									new Color(50, 70, 120, 255), // color
									ratio*0.5f,
									GUIAlignement.CENTER)); //scale
		components.add(ComponentFactory.generateImage("/img/gui/background/gray-back-bg.png",
									Constants.WIDTH/4,
									Constants.HEIGHT/2,
									new Vector3f(0,0,-1f),
									GUIAlignement.CENTER));
		components.add(ComponentFactory.generateButton(this,
				"RESET",
				ratio*0.2f,
				(int)(Constants.WIDTH/6f),
				(int)(Constants.HEIGHT/10f),
				ratio*0.5f,
				new Vector3f(0f, (float)-Constants.HEIGHT/2.5f, -1f),
				0f,
				GUIAlignement.CENTER,
				null));
		components.add(ComponentFactory.generateButton(this,
				"Appliquer",
				ratio*0.2f,
				(int)(Constants.WIDTH/6f),
				(int)(Constants.HEIGHT/10f),
				ratio*0.5f,
				new Vector3f((float)Constants.WIDTH/5, (float)-Constants.HEIGHT/2.5f, -1f),
				0f,
				GUIAlignement.CENTER,
				null));
		components.add(ComponentFactory.generateButton(this,
				"Retour",
				ratio*0.2f,
				(int)(Constants.WIDTH/6f),
				(int)(Constants.HEIGHT/10f),
				ratio*0.5f,
				new Vector3f((float)Constants.WIDTH/2.5f, (float)-Constants.HEIGHT/2.5f, -1f),
				0f,
				GUIAlignement.CENTER,
				new Runnable() {
					public void run() {
						FurryAttack.getInstance().getRenderer().getGUIRenderer().setCurrentGUI(GUIs.MAIN_MENU);
					}
				}));
		components.add(ComponentFactory.generateButton(this,
				"Exporter",
				ratio*0.2f,
				(int)(Constants.WIDTH/6f),
				(int)(Constants.HEIGHT/10f),
				ratio*0.5f,
				new Vector3f((float)-Constants.WIDTH/5, (float)-Constants.HEIGHT/2.5f, -1f),
				0f,
				GUIAlignement.CENTER,
				null));
		components.add(ComponentFactory.generateButton(this,
				"Importer",
				ratio*0.2f,
				(int)(Constants.WIDTH/6f),
				(int)(Constants.HEIGHT/10f),
				ratio*0.5f,
				new Vector3f((float)-Constants.WIDTH/2.5f, (float)-Constants.HEIGHT/2.5f, -1f),
				0f,
				GUIAlignement.CENTER,
				null));
		components.add(new GUIText("Race", //text
				new Vector3f((float)-Constants.WIDTH/2f+(float)Constants.WIDTH/5.5f,(float)Constants.HEIGHT/3.5f,-0.5f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f,//scale
				GUIAlignement.CENTER)); 
		components.add(new GUISelector<Species>(
				this,
				new Vector3f((float)-Constants.WIDTH/2f+(float)Constants.WIDTH/5.5f,(float)Constants.HEIGHT/5f,-0.5f),
				(int)(Constants.WIDTH/3f),
				(int)(Constants.HEIGHT/10f),
				FontType.CAPTAIN,
				Color.WHITE,
				GUIAlignement.CENTER,
				new GUISelectorItem<>(Species.FOX, "Renard"),
				new GUISelectorItem<>(Species.WOLF, "Loup"),
				new GUISelectorItem<>(Species.DUTCH_ANGEL_DRAGON, "Dutch Angel Dragon"),
				new GUISelectorItem<>(Species.HYENA, "Hyene")));
		components.add(new GUIText("Couleur primaire", //text
				new Vector3f((float)-Constants.WIDTH/2f+(float)Constants.WIDTH/5.5f,(float)Constants.HEIGHT/11f,-0.5f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f,//scale
				GUIAlignement.CENTER)); 
		components.add(new GUISelector<Color>(
				this,
				new Vector3f((float)-Constants.WIDTH/2f+(float)Constants.WIDTH/5.5f,0f,-0.5f),
				(int)(Constants.WIDTH/3f),
				(int)(Constants.HEIGHT/10f),
				FontType.CAPTAIN,
				Color.WHITE,
				GUIAlignement.CENTER,
				new GUISelectorItem<>(Color.WHITE, "Blanc"),
				new GUISelectorItem<>(Color.BLACK, "Noir")));
		components.add(new GUIText("Couleur secondaire", //text
				new Vector3f((float)-Constants.WIDTH/2f+(float)Constants.WIDTH/5.5f,(float)-Constants.HEIGHT/9f,-0.5f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f, //scale
				GUIAlignement.CENTER));
		components.add(new GUISelector<Color>(
				this,
				new Vector3f((float)-Constants.WIDTH/2f+(float)Constants.WIDTH/5.5f,(float)-Constants.HEIGHT/5f,-0.5f),
				(int)(Constants.WIDTH/3f),
				(int)(Constants.HEIGHT/10f),
				FontType.CAPTAIN,
				Color.WHITE,
				GUIAlignement.CENTER,
				new GUISelectorItem<>(Color.WHITE, "Blanc"),
				new GUISelectorItem<>(Color.BLACK, "Noir")));
		components.add(new GUIText("//TODO", //text
				new Vector3f((float)Constants.WIDTH/2f-(float)Constants.WIDTH/5.5f,0f,-0.5f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f, //scale
				GUIAlignement.CENTER)); 
		components.add(new GUIText("//TODO", //text
				new Vector3f((float)Constants.WIDTH/2f-(float)Constants.WIDTH/5.5f,(float)Constants.HEIGHT/5.5f,-0.5f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f, //scale
				GUIAlignement.CENTER));
		components.add(new GUIText("//TODO", //text
				new Vector3f((float)Constants.WIDTH/2f-(float)Constants.WIDTH/5.5f,(float)-Constants.HEIGHT/5.5f,-0.5f), //pos
				0f, //rot
				FontType.CAPTAIN, //font
				Color.WHITE, // color
				ratio*0.3f, //scale
				GUIAlignement.CENTER));
		character = new Character(Species.FOX, Color.WHITE, Color.BLACK, "", new CharacterAnimation("/anim/wolf_head_normal.anim", "/img/human_walk_texture.png"), ratio);
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
	
	public void recreate() {
		destroy();
		init();
	}
}
