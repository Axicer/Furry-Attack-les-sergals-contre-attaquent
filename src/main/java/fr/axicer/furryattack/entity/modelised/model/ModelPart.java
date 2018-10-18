package fr.axicer.furryattack.entity.modelised.model;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.entity.modelised.ModelisedEntity;
import fr.axicer.furryattack.render.shaders.CharacterPartShader;
import fr.axicer.furryattack.render.textures.Texture;

/**
 * Model part class
 * @author Axicer
 *
 */
public class ModelPart {

	private static final int TMP_TILE_SIZE = 500;
	
	private ModelPart parent;
    private float rotation;
    private Vector3f translation;
    private Matrix4f modelMatrix;
    private float width;
    private float height;
    private Texture texture;
    private int partID;
    private List<ModelPart> childs;
    private int[] childsData;
    public PartHolder partholder;
	private ModelisedEntity entity;

    private CharacterPartShader shader;
    private int VBO;
    
    /**
     * Constructor of a modelPart
     * @param entity {@link ModelisedEntity} bound to this part
     * @param partID int id associated (unique !)
     * @param partholder {@link PartHolder} containing all {@link Part}s
     * @param width float
     * @param height float
     * @param rotation float
     * @param texture {@link Texture} associated
     * @param localBindTransform {@link Matrix4f} local bind transform
     * @param childs int[] data to inflate later
     */
    public ModelPart(ModelisedEntity entity, int partID, PartHolder partholder, float width, float height, float rotation, Texture texture, Vector3f translation, int... childs) {
        this.rotation = rotation;
        this.parent = null;
        this.translation = translation;
        this.modelMatrix = new Matrix4f().identity();
        this.width = width * TMP_TILE_SIZE;
        this.height = height * TMP_TILE_SIZE;
        this.texture = texture;
        this.partID = partID;
        this.partholder = partholder;
        this.entity = entity;
        this.childs = new ArrayList<>();
        this.childsData = childs;        
        this.shader = new CharacterPartShader();
        
    	//store shader data
		shader.bind();
		shader.setUniformMat4f("projectionMatrix", FurryAttack.getInstance().projectionMatrix);
		shader.setUniformMat4f("viewMatrix", FurryAttack.getInstance().viewMatrix);
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		//size is given as tile size
		shader.setUniformf("width", width);
		shader.setUniformf("height", height);
		shader.setUniformi("tex", 0);
		shader.setUniformvec4f("textureBounds", partholder.getPart(partID).getBounds());
		shader.unbind();
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(3);
		vertexBuffer.put(new float[] {0f,0f,0f});
		vertexBuffer.flip();
		this.VBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.VBO);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STREAM_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
    
    /**
     * Function to bind ids given in the constructor to the given {@link ModelPart}s
     * @param data {@link Collection} of {@link ModelPart} to bind
     */
    protected void inflate(Collection<ModelPart> data) {
    	//for each child to bind
    	for(int i = 0 ; i < childsData.length ; i++) {
    		//check each part given in data
    		for(ModelPart p : data) {
    			//if id matches
    			if(p.getID() == childsData[i]) {
    				//add this child
    				this.childs.add(p);
    			}
    		}
    	}
    }
    
    public int getID() {
    	return this.partID;
    }
    
    public float getRotation() {
        return this.rotation;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public Vector3f getTranslation() {
        return this.translation;
    }

    public Matrix4f getRootBindTransform() {
        return this.modelMatrix;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public List<ModelPart> getChilds() {
        return this.childs;
    }

    public void setRotation(float value) {
    	this.rotation = value;
    }

    public void setTranslation(Vector3f value) {
    	this.translation = value;
    }

    public void setTexture(Texture texture) {
    	this.texture = texture;
    }

    public void setWidth(float width) {
    	this.width = width;
    }

    public void setHeight(float height) {
    	this.height = height;
    }

    /**
     * Calculate the root bind transform depending on the parent transform and rotation
     * @param parentRoottransform {@link Matrix4f} parent transform
     * @param parentRotation float parent rotation
     */
    public void calculateRootBindTransform(ModelPart parent) {
    	this.parent = parent;
    	//get our new local translation rotated
    	Vector3f localTranslation= new Vector3f(translation).rotateZ(parent == null ? 0 :(float)Math.toRadians(parent.getRotation()));
		Vector3f newTranslation = localTranslation.add(parent == null ? new Vector3f() : parent.getTranslation()).mul(TMP_TILE_SIZE);
    	//calc model Matrix
		this.modelMatrix.identity().translate(newTranslation);
		
		if(partID == 7) {
			//TODO
			System.out.println(modelMatrix);
		}
		//recursively call calculation
		for(ModelPart part : childs) {
			part.calculateRootBindTransform(this);
		}
    }
    
    /**
     * Calculate all bind transforms and data
     */
    public void updateData() {
    	//update pos VBO
    	Vector2f pos = entity.getPosition();
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(3);
		vertexBuffer.put(new float[] {pos.x, pos.y, 0f});
		vertexBuffer.flip();
		
		//simply reset vertex not deleting VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, vertexBuffer);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		modelMatrix.rotateZ((float)Math.toRadians(rotation + (parent != null ? parent.getRotation() : 0f))); 
		
		//then update shader data
		shader.bind();
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		//size is given as tile size
		shader.setUniformf("width", width);
		shader.setUniformf("height", height);
		shader.setUniformvec4f("textureBounds", partholder.getPart(partID).getBounds());
		shader.unbind();
		
		//render recursively
		for(ModelPart child : childs)child.updateData();
    }
    
    public void render() {
    	//enable blending
		GL11.glEnable(GL11.GL_BLEND);
		//defines blend functions
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//bind the part texture at location 0 (default for this shader)
		texture.bind(0);
		//bind the shader
		shader.bind();
		//get the vertex attrib location ID from the shader
		int vertexAttribLocation = GL20.glGetAttribLocation(shader.program, "position");
		//bind the vertex attrib location for the shader
		GL20.glEnableVertexAttribArray(vertexAttribLocation);
		//bind the buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
		//push buffer's data into the vertex attrib location in the shader
		GL20.glVertexAttribPointer(vertexAttribLocation, 3, GL11.GL_FLOAT, false, 0, 0);
		//draw shapes
		GL11.glDrawArrays(GL11.GL_POINTS, 0, 1);
		//unbind vertex attrib location for the shader
		GL20.glDisableVertexAttribArray(vertexAttribLocation);
		//unbind buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		//unbind the shader
		shader.unbind();
		Texture.unbind();
		//unbind texture
		Texture.unbind();
		//disable blending
		GL11.glDisable(GL11.GL_BLEND);
		
		//render recursively
		for(ModelPart child : childs)child.render();
    }
    
    public ModelPart getPart(int id) {
    	//if we are the needed part then return this
    	if(this.partID == id)return this;
    	else {
    		//check for the part in childrens
    		for(ModelPart part : childs) {
    			ModelPart p =  part.getPart(id);
    			//if found then return it
    			if(p != null)return p;
    		}
    		//else return null
    		return null;
    	}
    }
}