package fr.axicer.furryattack.client.render;

import fr.axicer.furryattack.common.map.frame.Frame;
import fr.axicer.furryattack.common.map.frame.FrameBlock;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public class FrameModel extends RawModel{

    private final int vertexVBOID;
    private final int texelVBOID;
    private final int borderTexelVBOID;
    private final int decorationTexelVBOID;

    public FrameModel(int vaoID, int vertexCount, int vertexVBOID, int texelVBOID, int borderTexelVBOID, int decorationTexelVBOID) {
        super(vaoID, vertexCount);
        this.vertexVBOID = vertexVBOID;
        this.texelVBOID = texelVBOID;
        this.borderTexelVBOID = borderTexelVBOID;
        this.decorationTexelVBOID = decorationTexelVBOID;
    }

    public int getBorderTexelVBOID() {
        return borderTexelVBOID;
    }

    public int getTexelVBOID() {
        return texelVBOID;
    }

    public int getVertexVBOID() {
        return vertexVBOID;
    }

    public int getDecorationTexelVBOID() {
        return decorationTexelVBOID;
    }

    public void updateAll(FloatBuffer position, DoubleBuffer decorationTexCoord, DoubleBuffer borderTexCoord, DoubleBuffer texCoord){
        Loader.updateVBO(vertexVBOID, position, 0);
        Loader.updateVBO(texelVBOID, texCoord, 0);
        Loader.updateVBO(borderTexelVBOID, borderTexCoord, 0);
        Loader.updateVBO(decorationTexelVBOID, decorationTexCoord, 0);
    }

    public void update(int x, int y, Frame frame){
        var borderTexBuffer = BufferUtils.createDoubleBuffer(2 * 2 * 3);
        var decorationTexBuffer = BufferUtils.createDoubleBuffer(2 * 2 * 3);
        var texBuffer = BufferUtils.createDoubleBuffer(2 * 2 * 3);
        for(int dy = -1 ; dy <= 1 ; dy++){
            for(int dx = -1 ; dx <= 1 ; dx++){
                final var block = frame.getBlock(x + dx, y + dy);
                if(block == null) continue;
                borderTexBuffer.clear();
                decorationTexBuffer.clear();
                texBuffer.clear();

                block.addTextureFloats(borderTexBuffer, block.getBorderTexCoords(frame.getBorderAtlas()), frame.getBorderAtlas().getRatioX(), frame.getBorderAtlas().getRatioY());
                block.addTextureFloats(decorationTexBuffer, block.getDecorationTexCoord(frame.getDecorationAtlas()), frame.getDecorationAtlas().getRatioX(), frame.getDecorationAtlas().getRatioY());
                block.addTextureFloats(texBuffer, block.getTexCoord(frame.getTextureAtlas()), frame.getTextureAtlas().getRatioX(), frame.getTextureAtlas().getRatioY());

                borderTexBuffer.flip();
                decorationTexBuffer.flip();
                texBuffer.flip();

                int offset = (block.getPosition()[1] * frame.getLayer().getWidth() + block.getPosition()[0]) * 2 * 2 * 3 * 8; //a double is 8 bytes in java

                Loader.updateVBO(borderTexelVBOID, borderTexBuffer, offset);
                Loader.updateVBO(decorationTexelVBOID, decorationTexBuffer, offset);
                Loader.updateVBO(texelVBOID, texBuffer, offset);
            }
        }
    }
}
