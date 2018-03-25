package fr.axicer.furryattack.gui;

import org.joml.Vector3f;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.character.Character;
import fr.axicer.furryattack.character.Species;
import fr.axicer.furryattack.character.animation.CharacterAnimation;
import fr.axicer.furryattack.gui.elements.GUIButton;
import fr.axicer.furryattack.gui.elements.GUIComponent;
import fr.axicer.furryattack.gui.elements.GUIImage;
import fr.axicer.furryattack.gui.elements.GUIText;
import fr.axicer.furryattack.gui.elements.selector.GUISelector;
import fr.axicer.furryattack.gui.elements.selector.GUISelectorItem;
import fr.axicer.furryattack.util.Color;
import fr.axicer.furryattack.util.Constants;
import fr.axicer.furryattack.util.font.FontType;

public class CharacterCustomisationMenuGUI extends GUI{

	public Character character;
	
	@SuppressWarnings("unchecked")
	public CharacterCustomisationMenuGUI() {
		super("character-customisation-menu");
		float ratio = (float)Constants.WIDTH/(float)Constants.HEIGHT;
		components.add(new GUIImage("/img/gui/background/menu-bg.png", //imgPath
									Constants.WIDTH, //width
									Constants.HEIGHT, //height
									new Vector3f(0,0,-1f))); //pos
		components.add(new GUIText("Customisation du personnage", //text
									new Vector3f(0f, (float)Constants.HEIGHT/2.5f, -1f), //pos
									0f, //rot
									FontType.DK_KITSUNE_TAIL, //font
									new Color(50, 70, 120, 255), // color
									ratio*0.5f)); //scale
		components.add(new GUIImage("/img/gui/background/gray-back-bg.png",
									Constants.WIDTH/4,
									Constants.HEIGHT/2,
									new Vector3f(0,0,-1f)));
		components.add(new GUIButton("RESET",//text
				ratio*0.2f,// textscale
				Constants.WIDTH/6f, //width
				Constants.HEIGHT/10f, //height
				ratio*0.5f, //scale
				FontType.DK_KITSUNE_TAIL, //font
				Color.WHITE, //color
				new Vector3f(0f, (float)-Constants.HEIGHT/2.5f, -1f), //pos
				0f, //rot
				null));//action
		components.add(new GUIButton("Appliquer",
				ratio*0.2f,
				Constants.WIDTH/6f,
				Constants.HEIGHT/10f,
				ratio*0.5f,
				FontType.DK_KITSUNE_TAIL,
				new Color(50, 230, 50, 255),
				new Vector3f((float)Constants.WIDTH/5, (float)-Constants.HEIGHT/2.5f, -1f),
				0f,
				null));
		components.add(new GUIButton("Retour",
				ratio*0.2f,
				Constants.WIDTH/6f,
				Constants.HEIGHT/10f,
				ratio*0.5f,
				FontType.DK_KITSUNE_TAIL,
				Color.WHITE,
				new Vector3f((float)Constants.WIDTH/2.5f, (float)-Constants.HEIGHT/2.5f, -1f),
				0f,
				new Runnable() {
					public void run() {
						FurryAttack.getInstance().getGuiManager().setGUI(GUIManager.MENU);
					}
				}));
		components.add(new GUIButton("Exporter",
				ratio*0.2f,
				Constants.WIDTH/6f,
				Constants.HEIGHT/10f,
				ratio*0.5f,
				FontType.DK_KITSUNE_TAIL,
				Color.WHITE,
				new Vector3f((float)-Constants.WIDTH/5, (float)-Constants.HEIGHT/2.5f, -1f),
				0f,
				null));
		components.add(new GUIButton("Importer",
				ratio*0.2f,
				Constants.WIDTH/6f,
				Constants.HEIGHT/10f,
				ratio*0.5f,
				FontType.DK_KITSUNE_TAIL,
				Color.WHITE,
				new Vector3f((float)-Constants.WIDTH/2.5f, (float)-Constants.HEIGHT/2.5f, -1f),
				0f,
				null));
		components.add(new GUIText("Race", //text
				new Vector3f((float)-Constants.WIDTH/2f+(float)Constants.WIDTH/5.5f,(float)Constants.HEIGHT/3.5f,-0.5f), //pos
				0f, //rot
				FontType.DK_KITSUNE_TAIL, //font
				Color.WHITE, // color
				ratio*0.3f)); //scale
		components.add(new GUISelector<Species>(
				new Vector3f((float)-Constants.WIDTH/2f+(float)Constants.WIDTH/5.5f,(float)Constants.HEIGHT/5f,-0.5f),
				Constants.WIDTH/3f,
				Constants.HEIGHT/10f,
				FontType.DK_KITSUNE_TAIL,
				Color.WHITE,
				new GUISelectorItem<>(Species.FOX, "Renard"),
				new GUISelectorItem<>(Species.WOLF, "Loup"),
				new GUISelectorItem<>(Species.DUTCH_ANGEL_DRAGON, "Dutch Angel Dragon"),
				new GUISelectorItem<>(Species.HYENA, "Hyene")));
		components.add(new GUIText("Couleur primaire", //text
				new Vector3f((float)-Constants.WIDTH/2f+(float)Constants.WIDTH/5.5f,(float)Constants.HEIGHT/11f,-0.5f), //pos
				0f, //rot
				FontType.DK_KITSUNE_TAIL, //font
				Color.WHITE, // color
				ratio*0.3f)); //scale
		components.add(new GUISelector<Color>(
				new Vector3f((float)-Constants.WIDTH/2f+(float)Constants.WIDTH/5.5f,0f,-0.5f),
				Constants.WIDTH/3f,
				Constants.HEIGHT/10f,
				FontType.DK_KITSUNE_TAIL,
				Color.WHITE,
				new GUISelectorItem<>(Color.WHITE, "Blanc"),
				new GUISelectorItem<>(Color.BLACK, "Noir")));
		components.add(new GUIText("Couleur secondaire", //text
				new Vector3f((float)-Constants.WIDTH/2f+(float)Constants.WIDTH/5.5f,(float)-Constants.HEIGHT/9f,-0.5f), //pos
				0f, //rot
				FontType.DK_KITSUNE_TAIL, //font
				Color.WHITE, // color
				ratio*0.3f)); //scale
		components.add(new GUISelector<Color>(
				new Vector3f((float)-Constants.WIDTH/2f+(float)Constants.WIDTH/5.5f,(float)-Constants.HEIGHT/5f,-0.5f),
				Constants.WIDTH/3f,
				Constants.HEIGHT/10f,
				FontType.DK_KITSUNE_TAIL,
				Color.WHITE,
				new GUISelectorItem<>(Color.WHITE, "Blanc"),
				new GUISelectorItem<>(Color.BLACK, "Noir")));
		components.add(new GUIText("//TODO", //text
				new Vector3f((float)Constants.WIDTH/2f-(float)Constants.WIDTH/5.5f,0f,-0.5f), //pos
				0f, //rot
				FontType.DK_KITSUNE_TAIL, //font
				Color.WHITE, // color
				ratio*0.3f)); //scale
		components.add(new GUIText("//TODO", //text
				new Vector3f((float)Constants.WIDTH/2f-(float)Constants.WIDTH/5.5f,(float)Constants.HEIGHT/5.5f,-0.5f), //pos
				0f, //rot
				FontType.DK_KITSUNE_TAIL, //font
				Color.WHITE, // color
				ratio*0.3f)); //scale
		components.add(new GUIText("//TODO", //text
				new Vector3f((float)Constants.WIDTH/2f-(float)Constants.WIDTH/5.5f,(float)-Constants.HEIGHT/5.5f,-0.5f), //pos
				0f, //rot
				FontType.DK_KITSUNE_TAIL, //font
				Color.WHITE, // color
				ratio*0.3f)); //scale
		character = new Character(Species.FOX, Color.WHITE, Color.BLACK, "", new CharacterAnimation("/anim/wolf_head_normal.anim", "/img/human_walk_texture.png"), ratio);
	}

	@Override
	public void render() {
		for(GUIComponent comp : components)comp.render();
		character.render();
	}

	@Override
	public void update() {
		for(GUIComponent comp : components)comp.update();
		character.update();
	}
	
	@Override
	public void destroy() {
		for(GUIComponent comp : components)comp.destroy();
		character.destroy();
	}
	
	
}
