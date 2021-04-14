package fr.axicer.furryattack.client.render;

import fr.axicer.furryattack.util.NumberUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.List;

public class Loader {

    private static final List<Integer> vaoIds = new LinkedList<>();
    private static final List<Integer> vboIds = new LinkedList<>();

    public static RawModel loadToVAO(float[] position, int positionDimensions, float[] texCoords){
        int vao = createVAO();
        storeDataInAttributeList(0, NumberUtils.storeDataInFloatBuffer(position), positionDimensions, GL15.GL_STATIC_DRAW);
        storeDataInAttributeList(1, NumberUtils.storeDataInFloatBuffer(texCoords), 2, GL15.GL_STATIC_DRAW);
        unbindVAO();
        return new RawModel(vao, position.length/positionDimensions);
    }

    public static FrameModel loadFrameToVAO(FloatBuffer position, DoubleBuffer decorationTexCoord, DoubleBuffer borderTexCoord, DoubleBuffer texCoord){
        int vao = createVAO();
        int vertexVBOID = storeDataInAttributeList(0, position, 2, GL15.GL_DYNAMIC_DRAW);
        int texelVBOID = storeDataInAttributeList(1, texCoord, 2, GL15.GL_DYNAMIC_DRAW);
        int borderTexelVBOID = storeDataInAttributeList(2, borderTexCoord, 2, GL15.GL_DYNAMIC_DRAW);
        int decorationTexelVBOID = storeDataInAttributeList(3, decorationTexCoord, 2, GL15.GL_DYNAMIC_DRAW);
        unbindVAO();

        return new FrameModel(vao, position.capacity()/2, vertexVBOID, texelVBOID, borderTexelVBOID, decorationTexelVBOID);
    }

    public static void cleanUp(){
        vaoIds.forEach(GL30::glDeleteVertexArrays);
        vboIds.forEach(GL15::glDeleteBuffers);
    }

    public static int createVAO(){
        int vao = GL30.glGenVertexArrays();
        vaoIds.add(vao);
        GL30.glBindVertexArray(vao);
        return vao;
    }

    private static void unbindVAO(){
        GL30.glBindVertexArray(0);
    }

    public static int storeDataInAttributeList(int attributeNumber, FloatBuffer buffer, int dataDimensions, int bufferUsage){
        int vbo = GL15.glGenBuffers();
        vboIds.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, bufferUsage);
        GL20.glVertexAttribPointer(attributeNumber, dataDimensions, GL11.GL_FLOAT, false, 0 ,0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        return vbo;
    }

    public static int storeDataInAttributeList(int attributeNumber, DoubleBuffer buffer, int dataDimensions, int bufferUsage){
        int vbo = GL15.glGenBuffers();
        vboIds.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, bufferUsage);
        GL20.glVertexAttribPointer(attributeNumber, dataDimensions, GL11.GL_DOUBLE, false, 0 ,0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        return vbo;
    }

    public static void updateVBO(int vboID, FloatBuffer buffer, int offset){
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset, buffer);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public static void updateVBO(int vboID, DoubleBuffer buffer, int offset){
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, offset, buffer);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }
}
