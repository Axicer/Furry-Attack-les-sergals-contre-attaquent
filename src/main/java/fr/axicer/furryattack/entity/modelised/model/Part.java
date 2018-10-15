package fr.axicer.furryattack.entity.modelised.model;
import org.joml.Vector4f;

/**
 * Part class
 * @author Axicer
 *
 */
public class Part {

    private int ID;
    private Vector4f bounds;

    /**
     * constructor of a part
     * @param id int id where everything refers to
     * @param bounds {@link Vector4f} limit for texture
     */
    public Part(int id, Vector4f bounds) {
    	this.ID = id;
    	this.bounds = bounds;
    }

    public int getID() {
        return this.ID;
    }

    public Vector4f getBounds() {
        return this.bounds;
    }

}