package fr.axicer.furryattack.entity.modelised;
import fr.axicer.furryattack.entity.Entity;
import fr.axicer.furryattack.entity.Species;
import fr.axicer.furryattack.entity.modelised.animation.Animation;
import fr.axicer.furryattack.entity.modelised.animation.AnimationReader;
import fr.axicer.furryattack.entity.modelised.model.ModelPart;
import fr.axicer.furryattack.entity.modelised.model.ModelPartReader;
import fr.axicer.furryattack.entity.modelised.model.PartReader;

/**
 * Modelised entity class
 * @author Axicer
 *
 */
public class ModelisedEntity extends Entity{

	private Animation animation;
    private ModelPart rootPart;

    public ModelisedEntity(String partPath, String modelPath, String animPath) {
        super(Species.FOX);
    	this.animation = AnimationReader.create(this, animPath);
    	this.rootPart = ModelPartReader.create(PartReader.getNewPartHolder(partPath), modelPath);
    }

    public ModelPart getSpecificModelPart(int id) {
    	//TODO
    	return null;
    }
    
    public void update() {
    	this.animation.update();
    	this.rootPart.update();
    }
    
    public void render() {
    	this.rootPart.render();
    }
}