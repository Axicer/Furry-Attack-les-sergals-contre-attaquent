package fr.axicer.furryattack.client.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.List;

public class Loader {

    private static final List<Integer> vaoIds = new LinkedList<>();
    private static final List<Integer> vboIds = new LinkedList<>();

    public static RawModel loadToVAO(float[] position, int positionDimensions,
                                     float[] texCoords){
        int vao = createVAO();
        storeDataInAttributeList(0, position, positionDimensions);
        storeDataInAttributeList(1, texCoords, 2);
        unbindVAO();
        return new RawModel(vao, position.length/positionDimensions);
    }
    public static RawModel loadFrameToVAO(float[] position, int positionDimensions,
                                     float[] decorationTexCoord, float[] borderTexCoord, float[] stoneTexCoord){
        int vao = createVAO();
        storeDataInAttributeList(0, position, positionDimensions);
        storeDataInAttributeList(1, stoneTexCoord, 2);
        storeDataInAttributeList(2, borderTexCoord, 2);
        storeDataInAttributeList(3, decorationTexCoord, 2);
        unbindVAO();
        return new RawModel(vao, position.length/positionDimensions);
    }

    public static void clearAll(){
        vaoIds.forEach(GL30::glDeleteVertexArrays);
        vboIds.forEach(GL15::glDeleteBuffers);
    }

    private static int createVAO(){
        int vao = GL30.glGenVertexArrays();
        vaoIds.add(vao);
        GL30.glBindVertexArray(vao);
        return vao;
    }

    private static void unbindVAO(){
        GL30.glBindVertexArray(0);
    }

    private static void storeDataInAttributeList(int attributeNumber, float[] data, int dataDimensions){
        int vbo = GL15.glGenBuffers();
        vboIds.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, dataDimensions, GL11.GL_FLOAT, false, 0 ,0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private static FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
