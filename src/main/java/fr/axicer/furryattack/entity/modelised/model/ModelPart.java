package fr.axicer.furryattack.entity.modelised.model;

import java.util.List;
import java.util.Set;
import org.joml.Matrix4f;
import fr.axicer.furryattack.render.textures.Texture;

/**
 * Model part class
 * @author Axicer
 *
 */
public class ModelPart {

    private float rotation;
    private Matrix4f localBindTransform;
    private Matrix4f rootBindTransform;
    private float width;
    private float height;
    private Texture texture;
    private int partID;
    private List<ModelPart> childs;
    private PartHolder partholder;

    public ModelPart(int partID, PartHolder partholder, float width, float height, float rotation, Texture texture, Matrix4f localBindTransform, int... childs) {
        // TODO implement here
    }
    
    public int getID() {
    	return this.partID;
    }
    
    public float getRotation() {
        // TODO implement here
        return 0.0f;
    }

    public float getWidth() {
        // TODO implement here
        return 0.0f;
    }

    public float getHeight() {
        // TODO implement here
        return 0.0f;
    }

    public Matrix4f getLocalBindTransform() {
        // TODO implement here
        return null;
    }

    public Matrix4f getRootBindTransform() {
        // TODO implement here
        return null;
    }

    public Texture getTexture() {
        // TODO implement here
        return null;
    }

    public Set<ModelPart> getChilds() {
        // TODO implement here
        return null;
    }

    public void setRotation(float value) {
        // TODO implement here
    }

    public void setLocalBindTransform(Matrix4f value) {
        // TODO implement here
    }

    public void setTexture(Texture texture) {
        // TODO implement here
    }

    public void setWidth(float width) {
        // TODO implement here
    }

    public void setHeight(float height) {
        // TODO implement here
    }

    protected void calculateRootBindTransform(Matrix4f parentRoottransform, float parentRotation) {
        // TODO implement here
    }
    
    public void update() {
    	//TODO
    }
    
    public void render() {
    	//TODO
    }

}