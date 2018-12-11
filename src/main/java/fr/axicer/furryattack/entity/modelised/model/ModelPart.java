package fr.axicer.furryattack.entity.modelised.model;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import fr.axicer.furryattack.FurryAttack;
import fr.axicer.furryattack.entity.modelised.ModelisedEntity;
import fr.axicer.furryattack.render.shaders.CharacterPartShader;
import fr.axicer.furryattack.render.textures.Texture;
import fr.axicer.furryattack.util.Vectors;

/**
 * Model part class
 * @author Axicer
 *
 */
public class ModelPart {

	public static final int TMP_TILE_SIZE = 10;
	
	private List<ModelPart> childs;

	private float rotation;
	private float rootRotation;
    private Vector2f translation;
    private Vector2f rootTranslation;
    private float width;
    private float height;
    protected int partID;
    private Matrix4f modelMatrix;
    private int[] childsData;
    private ModelPartBoundHolder boundHolder;
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
    public ModelPart(ModelisedEntity entity, int partID, float width, float height, float rotation, Vector2f translation, ModelPartBoundHolder boundHolder, int... childs) {
    	this.entity = entity;
    	this.partID = partID;
    	this.width = width;
    	this.height = height;
        this.rotation = rotation;
        this.rootRotation = 0f;
        this.rootTranslation = new Vector2f();
        this.translation = translation;
        this.boundHolder = boundHolder;

        this.childs = new ArrayList<>();
        this.childsData = childs;        
        
        this.modelMatrix = new Matrix4f().identity();
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
		shader.setUniformvec4f("textureBounds", boundHolder.getBounds(partID));
		shader.unbind();
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(3);
		vertexBuffer.put(new float[] {0f,0f,0f});
		vertexBuffer.flip();
		this.VBO = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.VBO);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
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
    			if(p.partID == childsData[i]) {
    				//add this child
    				this.childs.add(p);
    			}
    		}
    	}
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

    public Vector2f getTranslation() {
        return this.translation;
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

    public void setRotation(float value) {
    	this.rotation = value;
    }

    public void setTranslation(Vector2f value) {
    	this.translation = value;
    }

    public void setWidth(float width) {
    	this.width = width;
    }

    public void setHeight(float height) {
    	this.height = height;
    }

    /**
     * Calculate this part data depending on the parent
     * @param parent {@link ModelPart} parent
     */
    public void updateRecursive(ModelPart parent) {
    	if(parent != null) {
    		rootTranslation = parent.rootTranslation.add(Vectors.rotateCopy(translation, (float)Math.toRadians(parent.rootRotation)), new Vector2f());
    		rootRotation = parent.rootRotation+rotation;
    	}else {
    		rootTranslation= translation;
    		rootRotation = rotation;
    	}
		//recursively call calculation
		for(ModelPart part : childs) {
			part.updateRecursive(this);
		}
    }
    
    /**
     * Calculate all bind transforms and data
     */
    public void updateData() {
    	/* ---------- Update shader -------------*/
    	Vector2f pos = entity.getPosition();
		modelMatrix.identity().translate(rootTranslation.x+pos.x, rootTranslation.y+pos.y, 0f);
		modelMatrix.rotateZ((float)Math.toRadians(rootRotation)); 
		//then update shader data
		shader.bind();
		shader.setUniformMat4f("modelMatrix", modelMatrix);
		//size is given as tile size
		shader.setUniformf("width", width);
		shader.setUniformf("height", height);
		shader.setUniformvec4f("textureBounds", boundHolder.getBounds(partID));
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
		entity.getTexture().bind(0);
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
}