package fr.axicer.furryattack.entity.character;

import org.joml.Matrix4f;

import fr.axicer.furryattack.entity.Entity;
import fr.axicer.furryattack.entity.Species;
import fr.axicer.furryattack.render.Destroyable;
import fr.axicer.furryattack.render.Renderable;
import fr.axicer.furryattack.render.Updateable;

/**
 * Main entity of the game
 * @author Axicer
 *
 */
public class Character extends Entity implements Updateable,Renderable,Destroyable{

	private String expression;
	private Matrix4f modelMatrix;
	private CharacterSkin skin;
	private ModelPart root;
	private CharacterAnimation animation;
	
	public Character(Species race, String skinpath, String animPath, String expression) {
		super(race);
		this.setExpression(expression);
		this.modelMatrix = new Matrix4f();
		
		//TODO load skin and animation
	}

	
	/**
	 * Get the character expression
	 * @return {@link String} expression
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * set the character exression
	 * @param expression {@link String}
	 */
	protected void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * Get the character modelMatrix
	 * @return {@link Matrix4f} modelMatrix
	 */
	public Matrix4f getModelMatrix() {
		return modelMatrix;
	}

	/**
	 * Set the modelMatrix
	 * @param modelMatrix {@link Matrix4f} modelMatrix
	 */
	protected void setModelMatrix(Matrix4f modelMatrix) {
		this.modelMatrix = modelMatrix;
	}

	/**
	 * reset the skin
	 */
	protected void refreshSkin() {
		//TODO
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		super.update();
	}
	
	@Override
	public void render() {
		// TODO Auto-generated method stub
		super.render();
	}
}
