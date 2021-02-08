package fr.axicer.furryattack.common.map.frame;

import fr.axicer.furryattack.client.FAClient;
import fr.axicer.furryattack.client.control.KeyPressedEvent;
import fr.axicer.furryattack.client.render.Loader;
import fr.axicer.furryattack.client.render.RawModel;
import fr.axicer.furryattack.client.render.Texture;
import fr.axicer.furryattack.client.render.TextureAtlas;
import fr.axicer.furryattack.common.entity.Removable;
import fr.axicer.furryattack.common.entity.Renderable;
import fr.axicer.furryattack.common.entity.Updatable;
import fr.axicer.furryattack.common.events.EventListener;
import fr.axicer.furryattack.common.map.layout.Layout;
import fr.axicer.furryattack.util.NumberUtils;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * A frame from the map generated
 */
public class Frame implements Renderable, Updatable, Removable, EventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Frame.class);
    public static final int FRAME_BLOCK_WIDTH = 40;
    public static final int FRAME_BLOCK_HEIGHT = 24;
    public static final float BLOCK_WIDTH = 1.0f / FRAME_BLOCK_WIDTH;
    public static final float BLOCK_HEIGHT = 1.0f / FRAME_BLOCK_HEIGHT;

    private final UUID eventListenerId;

    private static FrameShader shader;
    private final FrameBlock[][] blocks; //all blocks
    private RawModel model;
    private final Layout layout;

    private boolean showLightFromCursor = false;
    private boolean useDarkBackground = false;
    private float cursorLightRadius = 0.5f;
    private float darkFactor = 0.5f;

    public Frame(Layout layout) {
        blocks = new FrameBlock[FRAME_BLOCK_HEIGHT][FRAME_BLOCK_WIDTH];
        this.layout = layout;
        generateBlocks();
        eventListenerId = FAClient.getEventManager().addListener(this);
    }

    public void loadModel() {
        if (shader == null) shader = new FrameShader();

        //load vertices
        var vertexList = new LinkedList<Float>();
        var texCoords = new LinkedList<Float>();
        for (int y = 0; y < FRAME_BLOCK_HEIGHT; y++) {
            for (int x = 0; x < FRAME_BLOCK_WIDTH; x++) {
                if (blocks[y][x].isSolid()) {
                    addQuadFloats(vertexList, x, y);
                    Vector2f texOrigin = blocks[y][x].getTextureCoords();
                    addTextureFloats(texCoords, texOrigin.x, texOrigin.y);
                }
            }
        }

        model = Loader.loadToVAO(NumberUtils.toFloatArray(vertexList), 2,
                NumberUtils.toFloatArray(texCoords));
    }

    private void generateBlocks() {
        //generate blocks
        for (int y = 0; y < FRAME_BLOCK_HEIGHT; y++) {
            for (int x = 0; x < FRAME_BLOCK_WIDTH; x++) {
                var pixColor = new Color(layout.getPixels()[y][x]);
                if (pixColor.equals(Color.BLACK)) {
                    //solid block
                    blocks[y][x] = new FrameBlock(true);
                } else if (pixColor.equals(Color.WHITE)) {
                    //transparent block
                    blocks[y][x] = new FrameBlock(false);
                } else {
                    LOGGER.error("pixel {} is unknown", pixColor);
                }
            }
        }
        //compute neighbors solid
        for (int y = 0; y < FRAME_BLOCK_HEIGHT; y++) {
            for (int x = 0; x < FRAME_BLOCK_WIDTH; x++) {
                var top = (y != 0) ? blocks[y - 1][x] : null;
                blocks[y][x].setNeighbor(top, FrameBlock.FrameOrientation.TOP);
                var right = (x != FRAME_BLOCK_WIDTH - 1) ? blocks[y][x + 1] : null;
                blocks[y][x].setNeighbor(right, FrameBlock.FrameOrientation.RIGHT);
                var bottom = (y != FRAME_BLOCK_HEIGHT - 1) ? blocks[y + 1][x] : null;
                blocks[y][x].setNeighbor(bottom, FrameBlock.FrameOrientation.BOTTOM);
                var left = (x != 0) ? blocks[y][x - 1] : null;
                blocks[y][x].setNeighbor(left, FrameBlock.FrameOrientation.LEFT);

            }
        }
    }

    private void addQuadFloats(List<Float> vertexList, int x, int y) {
        //bottom left triangle
        vertexList.add(x * BLOCK_WIDTH);
        vertexList.add(y * BLOCK_HEIGHT);
        vertexList.add(x * BLOCK_WIDTH);
        vertexList.add(y * BLOCK_HEIGHT + BLOCK_HEIGHT);
        vertexList.add(x * BLOCK_WIDTH + BLOCK_WIDTH);
        vertexList.add(y * BLOCK_HEIGHT + BLOCK_HEIGHT);

        //second triangle
        vertexList.add(x * BLOCK_WIDTH + BLOCK_WIDTH);
        vertexList.add(y * BLOCK_HEIGHT + BLOCK_HEIGHT);
        vertexList.add(x * BLOCK_WIDTH + BLOCK_WIDTH);
        vertexList.add(y * BLOCK_HEIGHT);
        vertexList.add(x * BLOCK_WIDTH);
        vertexList.add(y * BLOCK_HEIGHT);
    }

    private void addTextureFloats(List<Float> textureCoords, float startX, float startY) {
        float[] coord = {
                //bottom left triangle
                startX, startY + TextureAtlas.ATLAS_RATIO_Y,
                startX, startY,
                startX + TextureAtlas.ATLAS_RATIO_X, startY,
                //top right triangle
                startX + TextureAtlas.ATLAS_RATIO_X, startY,
                startX + TextureAtlas.ATLAS_RATIO_X, startY + TextureAtlas.ATLAS_RATIO_Y,
                startX, startY + TextureAtlas.ATLAS_RATIO_Y
        };
        for (float f : coord) {
            textureCoords.add(f);
        }
    }

    @Override
    public void remove() {
        model.destroy();
        FAClient.getEventManager().removeListener(eventListenerId);
    }

    @Override
    public void render() {
        shader.bind();
        shader.setUniformInt("atlas", 0);
        shader.setUniformVec2f("cursorPos", FAClient.getCursorPos());
        shader.setUniformFloat("screenWidth", (float)FAClient.getRenderer().getWindowWidth());
        shader.setUniformFloat("screenHeight", (float)FAClient.getRenderer().getWindowHeight());
        shader.setUniformBoolean("useCursorLight", showLightFromCursor);
        shader.setUniformBoolean("useDarkBackground", useDarkBackground);
        shader.setUniformFloat("lightRadius", cursorLightRadius);
        shader.setUniformFloat("darkFactor", darkFactor);
        TextureAtlas.getAtlas().bind(0);
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
        Texture.unbind();
        shader.unbind();
    }

    @Override
    public void update() {
    }

    public void onKeyPressed(KeyPressedEvent ev){
        if(ev.getKey() == GLFW.GLFW_KEY_KP_ADD){
            cursorLightRadius += 0.1;
            if(cursorLightRadius > 1)cursorLightRadius = 1;
        }else if(ev.getKey() == GLFW.GLFW_KEY_KP_SUBTRACT){
            cursorLightRadius -= 0.1;
            if(cursorLightRadius < 0.1)cursorLightRadius = 0.1f;
        }else if(ev.getKey() == GLFW.GLFW_KEY_L){
            showLightFromCursor = !showLightFromCursor;
        }else if(ev.getKey() == GLFW.GLFW_KEY_P){
            useDarkBackground = !useDarkBackground;
        }
    }
}
