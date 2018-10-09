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

	public static Matrix4f ROOT_TRANSFORM = new Matrix4f().translate(0,100,0);
	
	private String expression;
	private Matrix4f modelMatrix;
	private CharacterSkin skin;
	private CharacterPart root;
	private CharacterAnimation animation;
	
	public Character(Species race, String skinpath, String animPath, String expression) {
		super(race);
		this.setExpression(expression);
		this.modelMatrix = new Matrix4f();
		this.animation = new CharacterAnimation(animPath, this);
		this.skin = new CharacterSkin(skinpath, this);
		
		this.root = firstLoad();
	}

	/**
	 * Create the modelParts and set the pose to the first pose of animation
	 * @return {@link ModelPart} root part (should be body)
	 */
	private CharacterPart firstLoad() {
		//get the first pose
		CharacterAnimationPose firstPose = this.animation.getFirstPose();
		CharacterPart root = new CharacterPart(this, ModelPart.BODY, skin,
				firstPose.getLocalTransformationMatrix(ModelPart.BODY),
				firstPose.getRotation(ModelPart.BODY));
		//legs and head creation and binding to the body
		CharacterPart LL = new CharacterPart(this, ModelPart.LEFT_LEG, skin,
				firstPose.getLocalTransformationMatrix(ModelPart.LEFT_LEG),
				firstPose.getRotation(ModelPart.LEFT_LEG));
		CharacterPart RL = new CharacterPart(this, ModelPart.RIGHT_LEG, skin,
				firstPose.getLocalTransformationMatrix(ModelPart.RIGHT_LEG),
				firstPose.getRotation(ModelPart.RIGHT_LEG));
		CharacterPart head = new CharacterPart(this, ModelPart.HEAD, skin,
				firstPose.getLocalTransformationMatrix(ModelPart.HEAD),
				firstPose.getRotation(ModelPart.HEAD));
		root.addChildrens(LL);
		root.addChildrens(RL);
		root.addChildrens(head);
		//arms creation
		CharacterPart LA = new CharacterPart(this, ModelPart.LEFT_ARM, skin,
				firstPose.getLocalTransformationMatrix(ModelPart.LEFT_ARM),
				firstPose.getRotation(ModelPart.LEFT_ARM));
		CharacterPart RA = new CharacterPart(this, ModelPart.RIGHT_ARM, skin,
				firstPose.getLocalTransformationMatrix(ModelPart.RIGHT_ARM),
				firstPose.getRotation(ModelPart.RIGHT_ARM));
		root.addChildrens(LA);
		root.addChildrens(RA);
		//hand creation
		CharacterPart LH = new CharacterPart(this, ModelPart.LEFT_HAND, skin,
				firstPose.getLocalTransformationMatrix(ModelPart.LEFT_HAND),
				firstPose.getRotation(ModelPart.LEFT_HAND));
		CharacterPart RH = new CharacterPart(this, ModelPart.RIGHT_HAND, skin,
				firstPose.getLocalTransformationMatrix(ModelPart.RIGHT_HAND),
				firstPose.getRotation(ModelPart.RIGHT_HAND));
		LA.addChildrens(LH);
		RA.addChildrens(RH);
		
		//then calculate root bind transform recursively
		root.calculateRootBindTransform(ROOT_TRANSFORM);
		//and return root
		return root;
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

	/**
	 * Get the root part (body)
	 * @return {@link CharacterPart} part
	 */
	public CharacterPart getRoot() {
		return root;
	}

	@Override
	public void destroy() {
		super.destroy();
		this.root.destroy();
	}
	
	@Override
	public void update() {
		super.update();
		this.root.update();
	}
	
	@Override
	public void render() {
		super.render();
		this.root.render();
	}
}
