package fr.axicer.furryattack.entity.modelised;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import fr.axicer.furryattack.entity.Entity;
import fr.axicer.furryattack.entity.Species;
import fr.axicer.furryattack.entity.modelised.animation.Animation;
import fr.axicer.furryattack.entity.modelised.animation.AnimationReader;
import fr.axicer.furryattack.entity.modelised.model.ModelPart;
import fr.axicer.furryattack.entity.modelised.model.ModelPartReader;
import fr.axicer.furryattack.render.textures.Texture;
import fr.axicer.furryattack.util.Util;

/**
 * Modelised entity class
 * @author Axicer
 *
 */
public class ModelisedEntity extends Entity{

	private Animation animation;
    private ModelPart rootPart;
    private Texture texture;

    public ModelisedEntity(String partPath, String modelPath, String animPath) {
        super(Species.FOX);
        try {
        	//parse the json file
    		JSONObject modelJson = (JSONObject)new JSONParser().parse(Util.getStringFromInputStream(ModelPartReader.class.getResourceAsStream(modelPath)));
    		this.setTexture(Texture.loadTexture((String) modelJson.get("texture"), GL12.GL_CLAMP_TO_EDGE, GL11.GL_NEAREST));
    		this.animation = AnimationReader.create(this, animPath);
    		this.rootPart = ModelPartReader.create(this, modelJson);
        }catch(Exception e) {
        	e.printStackTrace();
        }
    }

    public ModelPart getSpecificModelPart(int id) {
    	return rootPart.getPart(id);
    }
    
    public void update() {
    	//update model state and other data
    	this.animation.update();
    	//then recalculate all the parts data
    	this.rootPart.updateData();
    	//get root calculate data from null parent because it's root obviously
    	this.rootPart.updateRecursive(null);
    }
    
    public void render() {
    	//simply render each parts
    	this.rootPart.render();
    }

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
}